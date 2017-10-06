package com.example.ahmed.chat.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.helper.Utils;
import com.example.ahmed.chat.model.MyAccount;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    private final int RC_SIGN_IN = 100;
    SignInButton btnGoogle;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;

    // animation
    Animation animationImage;
    Animation animationText;
    ImageView logo;
    TextView description,txVersionNumber;
    boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initView();
        configrationFirebase();
        handleEvent();
        startAnimation();
    }

    private void configrationFirebase() {
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void handleEvent() {
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void initView() {
        btnGoogle = (SignInButton) findViewById(R.id.btn_google);
        logo = (ImageView)findViewById(R.id.logo);
        description = (TextView)findViewById(R.id.description);
        txVersionNumber = (TextView) findViewById(R.id.version_number);
        animationImage = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move);
        animationText = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.opacity);
        logo.startAnimation(animationImage);
        description.setVisibility(View.INVISIBLE);
        btnGoogle.setVisibility(View.INVISIBLE);
        Utils.setFont(this,description);
        Utils.setFont(this,txVersionNumber);
        txVersionNumber.setText(Utils.getVersionNumber(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            updateUI(currentUser);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        MyAccount.setId(user.getUid());
        MyAccount.setUserName(user.getDisplayName());
        MyAccount.setEmail(user.getEmail());
        MyAccount.setImage(user.getPhotoUrl());
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
    public void startAnimation(){
        animationImage.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                description.setVisibility(View.VISIBLE);
                description.startAnimation(animationText);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnGoogle.setVisibility(View.VISIBLE);
                        btnGoogle.startAnimation(animationText);
                        firstTime = false;
                    }
                }, 2000);
            }
        });
    }

}
