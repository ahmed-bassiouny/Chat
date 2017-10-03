package com.example.ahmed.chat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.helper.Constants;
import com.example.ahmed.chat.model.MyAccount;
import com.example.ahmed.chat.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateActivity extends AppCompatActivity {

    String userId;
    double currentRate;
    int numberOfChat;
    RatingBar rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rating = (RatingBar) findViewById(R.id.rating);
        this.setFinishOnTouchOutside(false);
        if(MyAccount.getId().equals(getIntent().getStringExtra(Constants.FIRST_PERSON_KEY))){
            userId = getIntent().getStringExtra(Constants.SECOND_PERSON_KEY);
        }else {
            userId = getIntent().getStringExtra(Constants.FIRST_PERSON_KEY);
        }
        getUserData();
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentRate += rating.getRating();
                FirebaseDatabase.getInstance().getReference(Constants.USER).child(userId).child("rate").setValue(currentRate);
                MainActivity.chatting=false;
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void getUserData(){
        FirebaseDatabase.getInstance().getReference(Constants.USER).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                currentRate = user.rate;
                FirebaseDatabase.getInstance().getReference(Constants.USER).child(userId).child("chatWith").setValue((user.chatWith+1));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
