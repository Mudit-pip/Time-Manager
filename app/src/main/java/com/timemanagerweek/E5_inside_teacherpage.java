package com.timemanagerweek;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class E5_inside_teacherpage extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    CircleImageView profilepic;
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseDatabase db_fire;

    ListView list;
    ArrayList<String> com_name, com_des;
    ProgressDialog pg;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e5_inside_teacherpage);

        profilepic = findViewById(R.id.E5_profilepic);
        list = findViewById(R.id.E5_list);

        com_name = new ArrayList<>();
        com_des = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(E5_inside_teacherpage.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, com_name);
        list.setAdapter(adapter);

        pg = new ProgressDialog(E5_inside_teacherpage.this);
        pg.setMessage("Loading");
        pg.show();
        pg.setCancelable(false);


        db_fire = FirebaseDatabase.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(E5_inside_teacherpage.this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(E5_inside_teacherpage.this);

        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri uri = acct.getPhotoUrl();
            Picasso.get().load(uri).into(profilepic);
        }


        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(E5_inside_teacherpage.this, profilepic);
                popupMenu.getMenuInflater().inflate(R.menu.accountpopupmenu_forsettings, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.details) {
                            Intent inte = new Intent(E5_inside_teacherpage.this, E7_accountdetails.class);
                            inte.putExtra("uri", acct.getPhotoUrl().toString());
                            inte.putExtra("name", acct.getDisplayName());
                            inte.putExtra("email", acct.getEmail());
                            startActivity(inte);
                        } else if (item.getItemId() == R.id.signout) {
                            SharedPreferences pref = getSharedPreferences("pref_login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("user", 2);
                            editor.commit();
                            signOut();
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

////////listing all communities made by user
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        ref = db_fire.getReference().child("Creator").child(sanitizeEmail(user.getEmail())).child("Community");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com_name.clear();
                com_des.clear();

                for (DataSnapshot mydata : snapshot.getChildren()) {
                    community_class um = mydata.getValue(community_class.class);
                    if (um != null) {
                        com_name.add(mydata.getKey());
                    }
                }

                adapter.notifyDataSetChanged();
                pg.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ///////////////////listing over

        //on click for list
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView a = ((TextView)view);
                String com = a.getText().toString().trim();

                Intent intent = new Intent(E5_inside_teacherpage.this, E9_inside_community_creator.class);
                intent.putExtra("comname", com);
                startActivity(intent);

            }
        });


    }


    private String sanitizeEmail(String email) {
        return email != null ? email.replace(".", "_dot_").replace("@", "_at_") : "error";
    }


    void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                startActivity(new Intent(E5_inside_teacherpage.this, E6_studentlogin_page.class));
                finish();
            }
        });
    }

    public void add_community_btn(View view) {
        startActivity(new Intent(E5_inside_teacherpage.this, E8_Community_page_creator.class));
    }
}