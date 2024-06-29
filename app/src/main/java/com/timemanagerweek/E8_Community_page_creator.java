package com.timemanagerweek;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class E8_Community_page_creator extends AppCompatActivity {

    EditText comm_name, comm_desc;

    Button make_comm_public;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e8_community_page_creator);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(E8_Community_page_creator.this);

        make_comm_public = findViewById(R.id.E8_makepublic_btn);
        comm_name = findViewById(R.id.E8_communityname_txt);
        comm_desc = findViewById(R.id.E8_comm_description);

        make_comm_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                if (comm_name.getText().toString().trim().length() != 0) {
                    ref = db.getReference().child("Community");
                    ref.orderByKey().equalTo(comm_name.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(E8_Community_page_creator.this, "Community name is already taken", Toast.LENGTH_SHORT).show();
                            } else {
                                add_data_to_firbase(comm_name.getText().toString().trim(), comm_desc.getText().toString().trim(), acct.getDisplayName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    Toast.makeText(E8_Community_page_creator.this, "Can't add empty Community Name", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void add_data_to_firbase(String name, String desc, String owner) {
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        community_class data = new community_class(name, desc, owner);
        if (user != null) {
            try {
                ref = db.getReference().child("Creator").child(sanitizeEmail(user.getEmail())).child("Community").child(name);
                ref.push().setValue(data);

                ref = db.getReference().child("Community").child(name).child("description");
                ref.setValue(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Toast.makeText(this, "Community is now Public", Toast.LENGTH_SHORT).show();
    }

    private String sanitizeEmail(String email) {
        return email != null ? email.replace(".", "_dot_").replace("@", "_at_") : "error";
    }

}