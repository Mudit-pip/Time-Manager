package com.timemanagerweek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class E10_Schedule_new_meet_under_e9 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e10_schedule_new_meet_under_e9);

        Intent intent = getIntent();
        String comunityname_int = intent.getStringExtra("comname");

    }
}