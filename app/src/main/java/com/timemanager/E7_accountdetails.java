package com.timemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class E7_accountdetails extends AppCompatActivity {

    CircleImageView pic;
    TextView name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e7_accountdetails);

        pic = findViewById(R.id.E7_pic);
        name = findViewById(R.id.E7_name);
        email = findViewById(R.id.E7_email);

        Intent intent = getIntent();

        String s = intent.getStringExtra("uri");
        Uri uri = Uri.parse(s);

        Picasso.get().load(uri).into(pic);

        s = intent.getStringExtra("name");
        name.setText(s);

        s = intent.getStringExtra("email");
        email.setText(s);
    }
}