package com.timemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class E6_studentlogin_page extends AppCompatActivity {

    SignInButton btn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient mgoogleSignInClient;

    TextView creator_btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e6_studentlogin_page);

        btn = findViewById(R.id.E6_signinbtn_students);
        creator_btn = findViewById(R.id.E6_creator_btn_txt);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mgoogleSignInClient = GoogleSignIn.getClient(E6_studentlogin_page.this, gso);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin(1);
            }
        });

        creator_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin(0);
            }
        });



        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            SharedPreferences pref = getSharedPreferences("pref_login", MODE_PRIVATE);
            int z = pref.getInt("user", 2);
            if(z==1) {
                finish();
                Intent intent = new Intent(E6_studentlogin_page.this, E3_Home.class);
                startActivity(intent);
            } else if(z==0) {
                finish();
                Intent intent = new Intent(E6_studentlogin_page.this, E5_inside_teacherpage.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
        }

    }



    int RC_SIGN_IN=40;
    int RC_SIGN_IN_teacher=30;
    public void signin(int i) {
        SharedPreferences pref = getSharedPreferences("pref_login", MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();

        if(i==1) {
            editor.putInt("user", 1);
            editor.commit();
            Intent intent = mgoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SIGN_IN);
        } else if(i==0){
            editor.putInt("user", 0);
            editor.commit();
            Intent intent = mgoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SIGN_IN_teacher);
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseauth(account.getIdToken(), "Users");
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == RC_SIGN_IN_teacher){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseauth(account.getIdToken(), "Creator");
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void firebaseauth(String idToken, String desi) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();

                    users us = new users();
                    us.setName(user.getDisplayName());
                    us.setUserid(user.getUid());
                    us.setProfile(user.getPhotoUrl().toString());


//                    DatabaseReference refrence = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                    DatabaseReference refrence = FirebaseDatabase.getInstance().getReference().child(desi).child(sanitizeEmail(user.getEmail()));
                    refrence.child(user.getUid()).setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(E6_studentlogin_page.this, "Succesfully Loged in", Toast.LENGTH_SHORT).show();

                                if(desi.equals("Users")) {
                                    Intent intent = new Intent(E6_studentlogin_page.this, E3_Home.class);
                                    startActivity(intent);
                                    finish();
                                } else if(desi.equals("Creator")) {
                                    Intent intent = new Intent(E6_studentlogin_page.this, E5_inside_teacherpage.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(E6_studentlogin_page.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private String sanitizeEmail(String email) {
        return email != null ? email.replace(".", "_dot_").replace("@", "_at_") : "error";
    }


}