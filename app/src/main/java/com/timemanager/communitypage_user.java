package com.timemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class communitypage_user extends AppCompatActivity {

    TextView comname_txt, comdes_txt, comowner_txt;

    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseAuth auth;

    ArrayList<String> comowner_ar, comdes_ar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communitypage_user);

        comname_txt = findViewById(R.id.comm_user_name);
        comdes_txt = findViewById(R.id.comm_user_description);
        comowner_txt = findViewById(R.id.comm_user_owner);

        Intent intent = getIntent();
        String Community_name = intent.getStringExtra("commname");

        if (Community_name.isEmpty()) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Community's name is Empty", Toast.LENGTH_SHORT).show();
        }

        comname_txt.setText(Community_name);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        ref = db.getReference().child("Community").child(Community_name).child("description");

        comowner_ar = new ArrayList<>();
        comdes_ar = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                community_class com = snapshot.getValue(community_class.class);
                if (com.getCommunitydes().isEmpty()) {
                    comdes_txt.setVisibility(View.GONE);
                } else {
                    comdes_txt.setText(com.getCommunitydes());
                }
                comowner_txt.setText("-"+com.getOwner());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}