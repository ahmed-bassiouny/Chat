package com.example.ahmed.chat.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.activity.LoginActivity;
import com.example.ahmed.chat.helper.Constants;
import com.example.ahmed.chat.model.MyAccount;
import com.example.ahmed.chat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private static ProfileFragment mInstance;
    private ImageView profileImage;
    private TextView tvName,tvEmail,tvChatNumber,tvRate,tvSignOut;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment getInstance() {
        if (mInstance == null) {
            mInstance = new ProfileFragment();
        }
        return mInstance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        setData();
        return view;
    }

    private void setData() {
        tvName.setText(MyAccount.getUserName());
        tvEmail.setText(MyAccount.getEmail());
        Glide.with(getContext())
                .load(MyAccount.getImage())
                .placeholder(R.drawable.unknow)
                .into(profileImage);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handleEvent();
    }

    private void handleEvent() {
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                startActivity(new Intent(getActivity(),LoginActivity.class));

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            loadData();
        }
    }

    private void initView(View view) {
        tvName=view.findViewById(R.id.tv_name);
        tvEmail=view.findViewById(R.id.tv_email);
        tvRate=view.findViewById(R.id.tv_rate);
        tvChatNumber=view.findViewById(R.id.tv_number_of_chat);
        profileImage=view.findViewById(R.id.profile_image);
        tvSignOut=view.findViewById(R.id.btn_signout);
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    private void loadData(){
        FirebaseDatabase.getInstance().getReference().child(Constants.USER).child(MyAccount.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                if (value !=null) {

                    tvChatNumber.setText(String.valueOf(value.chatWith));
                    tvRate.setText(calcRate(value.chatWith , value.rate)+" %");
                }else {
                    FirebaseDatabase.getInstance().getReference().child(Constants.USER).child(MyAccount.getId()).setValue(new User());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calcRate(int chatwith , double rate){
        if(chatwith == 0)
            return 0;
        return (rate / (chatwith * 10))*100;
    }
}
