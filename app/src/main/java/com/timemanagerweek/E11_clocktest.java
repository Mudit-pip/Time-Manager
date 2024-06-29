package com.timemanagerweek;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

public class E11_clocktest extends AppCompatActivity {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Calendar cal;
    private static final String TAG = "E11_clocktest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e11_clocktest);


    }


    public void button(View view) {
        setAlarm();
    }



    private void setAlarm() {
        cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);


        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(E11_clocktest.this, AlarmRecevier.class);
        intent.putExtra("desc", "task1 task1 task1 task1 task1");
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        long nextTrigger = cal.getTimeInMillis();
        AlarmManager.AlarmClockInfo ac = new AlarmManager.AlarmClockInfo(nextTrigger, null);
        alarmManager.setAlarmClock(ac, pendingIntent);

        Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();
    }

}