package com.timemanager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class E4_teacherspage extends AppCompatActivity {

    EditText time, day;

    FirebaseDatabase db;
    DatabaseReference ref;
    ListView list;
    ValueEventListener listener;
    String courselist[];


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e4_teacherspage);

        db = FirebaseDatabase.getInstance();

//        time = findViewById(R.id.E4_teachers_time);
//        day=findViewById(R.id.E4_teachers_day);
        list = findViewById(R.id.E4_list);

        ref = db.getReference("coursenames");
        listener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter =0;
                for(DataSnapshot mydata : snapshot.getChildren()){
                    counter++;
                }
                courselist = new String[counter];
                int i=0;
                for(DataSnapshot mydata : snapshot.getChildren()){
                    coursenameclass data = mydata.getValue(coursenameclass.class);
                    courselist[i] = data.getcoursename();
                    i++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(E4_teacherspage.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, courselist);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(E4_teacherspage.this, E5_inside_teacherpage.class);
                intent.putExtra("subjectname", courselist[position]);
                startActivity(intent);
            }
        });


    }

    public void btn_addcourse(View view) {
        ref = db.getReference("coursenames");


        AlertDialog.Builder ab = new AlertDialog.Builder(E4_teacherspage.this);

        ab.setTitle("Add new Course");

        EditText ed = new EditText(E4_teacherspage.this);
        ab.setView(ed);

        ab.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                coursenameclass data = new coursenameclass(ed.getText().toString().trim());
                ref.push().setValue(data);
            }
        });

        ab.setNeutralButton("Cancel", null);

        ab.create().show();

    }

//    public void btn_teacher(View view) {
//        db = FirebaseDatabase.getInstance();
//        ref = db.getReference("teachersadd");
//
//        dataclass data = new dataclass(time.getText().toString(), day.getText().toString());
//
//        ref.push().setValue(data);
//    }
}