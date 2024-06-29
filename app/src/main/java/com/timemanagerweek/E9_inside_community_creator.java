package com.timemanagerweek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class E9_inside_community_creator extends AppCompatActivity {
    Button new_meet_btn;
    TextView com_name, com_des;

    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e9_inside_community_creator);

        new_meet_btn = findViewById(R.id.E9_newmeet_btn);
        com_name = findViewById(R.id.E9_comm_name_creator);
        com_des = findViewById(R.id.E9_comm_description_creator);

        Intent intent = getIntent();
        String s = intent.getStringExtra("comname");
        com_name.setText(s);


        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        ref = db.getReference().child("Community").child(s).child("description");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                community_class com = snapshot.getValue(community_class.class);
                if (com.getCommunitydes().isEmpty()) {
                    com_des.setVisibility(View.GONE);
                } else {
                    com_des.setText(com.getCommunitydes());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });////description added



        new_meet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(E9_inside_community_creator.this, E10_Schedule_new_meet_under_e9.class);
                intent1.putExtra("comname", s);
                startActivity(intent1);
            }
        });


    }
}