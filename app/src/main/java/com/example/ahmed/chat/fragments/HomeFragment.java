package com.example.ahmed.chat.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.helper.Constants;
import com.example.ahmed.chat.helper.Utils;
import com.example.ahmed.chat.model.MyAccount;
import com.example.ahmed.chat.model.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static HomeFragment mInstance;
    private TextView btnListen, btnTalk, btnCancelRequest;
    private DatabaseReference myRef;
    private MKLoader mkLoader;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment getInstance() {
        if (mInstance == null) {
            mInstance = new HomeFragment();
        }
        return mInstance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
        initObject();
    }

    private void initObject() {
        myRef = FirebaseDatabase.getInstance().getReference(Constants.WAITING_LIST);
    }


    private void initView(View view) {
        btnTalk = view.findViewById(R.id.btn_talk);
        btnListen = view.findViewById(R.id.btn_listen);
        btnCancelRequest = view.findViewById(R.id.btn_cancelrequest);
        mkLoader = view.findViewById(R.id.mkloader);
        Utils.setFont(getContext(),btnTalk);
        Utils.setFont(getContext(),btnListen);
        Utils.setFont(getContext(),btnCancelRequest);
    }

    private void initEvent() {
        btnTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(MyAccount.getId()).setValue(true);
                makeRequest(false);
            }
        });
        btnCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(MyAccount.getId()).setValue(false, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        makeRequest(true);
                        Toast.makeText(getActivity(), "You Canceled Request", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest(false);
                getListOfUserWantTalk();
            }
        });
    }

    private void makeRequest(boolean status) {
        btnListen.setEnabled(status);
        btnTalk.setEnabled(status);
        btnCancelRequest.setEnabled(!status);
        if (status) {
            mkLoader.setVisibility(View.INVISIBLE);
        } else {
            mkLoader.setVisibility(View.VISIBLE);
        }
    }

    private void getStatus() {
        // i can't find this useless
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean value = dataSnapshot.getValue(Boolean.class);
                if (value) {
                    makeRequest(false);
                } else {
                    makeRequest(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListOfUserWantTalk() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean result;
                boolean foundSomeone=false;
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    result = item.getValue(Boolean.class);
                    if(result){
                        foundSomeone=true;
                        findPerson(item.getKey());
                        break;
                    }
                }
                if(!foundSomeone){
                    makeRequest(true);
                    Toast.makeText(getActivity(), R.string.cant_find_one, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void findPerson(final String personKey){

        // set user in waiting list false
        myRef.child(personKey).setValue(false);

        // generate new key for new session
        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child(Constants.SESSION).push();
        final String sessionKey = newRef.getKey();
        // update my account with key session
        FirebaseDatabase.getInstance().getReference(Constants.USER).child(MyAccount.getId()).child("lastSession").setValue(sessionKey, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                // update status my account in chat true
                FirebaseDatabase.getInstance().getReference(Constants.USER).child(MyAccount.getId()).child("inChat").setValue(true);
                // update another user with key session
                FirebaseDatabase.getInstance().getReference(Constants.USER).child(personKey).child("lastSession").setValue(sessionKey, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // update status user in chat true
                        FirebaseDatabase.getInstance().getReference(Constants.USER).child(personKey).child("inChat").setValue(true);
                        //TODO : create Session
                        Session session = new Session();
                        session.setFirstPerson(MyAccount.getId());
                        session.setSecondPerson(personKey);
                        FirebaseDatabase.getInstance().getReference(Constants.SESSION).child(sessionKey).setValue(session);
                    }
                });
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        makeRequest(true);
    }
}
