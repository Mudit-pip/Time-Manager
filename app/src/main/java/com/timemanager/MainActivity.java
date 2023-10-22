package com.timemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(MainActivity.this, E3_Home.class));


//        bnv = findViewById(R.id.main_bnv);
//
//        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                int id = item.getItemId();
//
//                if(id == R.id.bnv_menu_list){
//                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
//                } else if(id == R.id.bnv_menu_chart){
//                    Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
//                }
//
//
//
//                return true;
//            }
//        });






//        txt1 = findViewById(R.id.main_btn1);
//        txt2 = findViewById(R.id.main_btn2);


//        startActivity(new Intent(MainActivity.this, E1_planner.class));
//
//        txt1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, E1_planner.class));
//            }
//        });
//
//        txt2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, E2_scheduler.class));
//            }
//        });



    }
}