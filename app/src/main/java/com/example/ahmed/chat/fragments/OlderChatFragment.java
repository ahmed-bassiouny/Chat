package com.example.ahmed.chat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.chat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OlderChatFragment extends Fragment {


    private static OlderChatFragment mInstance;

    public OlderChatFragment() {
        // Required empty public constructor
    }

    public static OlderChatFragment getInstance() {
        if (mInstance == null) {
            mInstance = new OlderChatFragment();
        }
        return mInstance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_older_chat, container, false);
    }

}
