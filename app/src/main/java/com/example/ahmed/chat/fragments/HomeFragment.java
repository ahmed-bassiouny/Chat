package com.example.ahmed.chat.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.helper.Constants;
import com.example.ahmed.chat.model.MyAccount;
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
    private Button btnListen, btnTalk, btnCancelRequest;
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
        myRef = FirebaseDatabase.getInstance().getReference(Constants.WAITINGLIST);
    }


    private void initView(View view) {
        btnTalk = view.findViewById(R.id.btn_talk);
        btnListen = view.findViewById(R.id.btn_listen);
        btnCancelRequest = view.findViewById(R.id.btn_cancelrequest);
        mkLoader = view.findViewById(R.id.mkloader);
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
                myRef.child(MyAccount.getId()).setValue(false);
                makeRequest(true);
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

}
