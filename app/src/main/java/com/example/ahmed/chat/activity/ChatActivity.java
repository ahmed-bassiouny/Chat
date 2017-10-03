package com.example.ahmed.chat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.adapters.ItemChatAdapter;
import com.example.ahmed.chat.helper.Constants;
import com.example.ahmed.chat.model.Message;
import com.example.ahmed.chat.model.MyAccount;
import com.example.ahmed.chat.model.Session;
import com.example.ahmed.chat.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ValueEventListener valueEventListener; // for end session
    ChildEventListener childEventListener; // for get new message
    String lastSession;
    Session session;
    RecyclerView recyclerView;
    ItemChatAdapter itemChatAdapter;
    EditText etNewMsg;
    ImageView ivSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        valueEventListener = getListenerStatus();
        childEventListener = getListenerForNewMessage();
        lastSession = getIntent().getStringExtra(Constants.SESSION_KEY);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        etNewMsg = (EditText) findViewById(R.id.et_new_msg);
        ivSend = (ImageView) findViewById(R.id.iv_send);
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNewMsg.getText().toString().trim().isEmpty())
                    return;
                else {
                    Message message = new Message();
                    message.setFromId(MyAccount.getId());
                    message.setMessageText(etNewMsg.getText().toString());
                    String key = FirebaseDatabase.getInstance().getReference(Constants.SESSION).child(lastSession).child(Constants.MESSAGE).push().getKey();
                    FirebaseDatabase.getInstance().getReference(Constants.SESSION).child(lastSession).child(Constants.MESSAGE).child(key).setValue(message);
                    etNewMsg.setText("");
                }
            }
        });
        FirebaseDatabase.getInstance().getReference(Constants.SESSION).child(lastSession).child(Constants.MESSAGE).addChildEventListener(childEventListener);
        loadSession();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference(Constants.USER).child(MyAccount.getId()).addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.end_chat:
                updateUserstatus();
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference(Constants.USER).child(MyAccount.getId()).removeEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference(Constants.SESSION).child(lastSession).child(Constants.MESSAGE).removeEventListener(childEventListener);
    }

    private ValueEventListener getListenerStatus() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                if (value != null) {
                    if (!value.inChat) {
                        // TODO : i will goto rate dialog
                        // TODO : Doalog to rate user don't forget it

                        if (session != null && !session.getFirstPerson().isEmpty() && !session.getSecondPerson().isEmpty()) {
                            Intent intent = new Intent(ChatActivity.this, RateActivity.class);
                            intent.putExtra(Constants.FIRST_PERSON_KEY, session.getFirstPerson());
                            intent.putExtra(Constants.SECOND_PERSON_KEY, session.getSecondPerson());
                            finish();
                            startActivity(intent);
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

    private void updateUserstatus() {
        FirebaseDatabase.getInstance().getReference(Constants.USER).child(session.getFirstPerson()).child("inChat").setValue(false, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                FirebaseDatabase.getInstance().getReference(Constants.USER).child(session.getSecondPerson()).child("inChat").setValue(false, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // TODO remove chat
                        // FirebaseDatabase.getInstance().getReference(Constants.SESSION).child(lastSession).removeValue();
                    }
                });
            }
        });

    }
    private void loadSession(){
        FirebaseDatabase.getInstance().getReference(Constants.SESSION).child(lastSession).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                session = dataSnapshot.getValue(Session.class);
                if (session == null) {
                    Toast.makeText(ChatActivity.this, "Something Happened", Toast.LENGTH_SHORT).show();
                    finish();
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

    /*private void loadChat() {
        FirebaseDatabase.getInstance().getReference(Constants.SESSION).child(lastSession).child(Constants.MESSAGE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Message> messages = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Message message = item.getValue(Message.class);
                    if (message == null)
                        return;
                    messages.add(message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
*/
    private ChildEventListener getListenerForNewMessage() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if (itemChatAdapter == null) {
                    itemChatAdapter = new ItemChatAdapter();
                    recyclerView.setAdapter(itemChatAdapter);
                }
                itemChatAdapter.addMessage(message);
                recyclerView.scrollToPosition(itemChatAdapter.getItemCount()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}
