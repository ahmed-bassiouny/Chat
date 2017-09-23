package com.example.ahmed.chat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.helper.Constants;
import com.example.ahmed.chat.model.MyAccount;
import com.example.ahmed.chat.model.Session;
import com.example.ahmed.chat.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    ValueEventListener valueEventListener;
    String lastSession;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        valueEventListener=getListenerStatus();
        lastSession = getIntent().getStringExtra(Constants.SESSION_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addListenerChat();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.end_chat:
                updateUserstatus();
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeListenerChat();
    }
    private void addListenerChat(){
        FirebaseDatabase.getInstance().getReference(Constants.USER).child(MyAccount.getId()).addValueEventListener(valueEventListener);
    }
    private void removeListenerChat(){
        FirebaseDatabase.getInstance().getReference(Constants.USER).child(MyAccount.getId()).removeEventListener(valueEventListener);
    }

    private ValueEventListener getListenerStatus (){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                if(value !=null){
                    if(!value.inChat){
                        // TODO : i will goto rate dialog
                        // TODO : Doalog to rate user don't forget it

                        if(session != null && !session.firstPerson.isEmpty() && !session.secondPerson.isEmpty()){
                            Intent intent = new Intent(ChatActivity.this, RateActivity.class);
                            intent.putExtra(Constants.FIRST_PERSON_KEY,session.firstPerson);
                            intent.putExtra(Constants.SECOND_PERSON_KEY,session.secondPerson);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }
    private void updateUserstatus(){
        FirebaseDatabase.getInstance().getReference(Constants.SESSION).child(lastSession).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 session = dataSnapshot.getValue(Session.class);
                if(session !=null){
                    FirebaseDatabase.getInstance().getReference(Constants.USER).child(session.firstPerson).child("inChat").setValue(false);
                    FirebaseDatabase.getInstance().getReference(Constants.USER).child(session.secondPerson).child("inChat").setValue(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Sorry you must end chat first", Toast.LENGTH_SHORT).show();
    }
}
