package com.timemanagerweek;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmRecevier extends BroadcastReceiver {
    private static final String TAG = "AlarmRecevier";
//    private static Ringtone ringtone;

    private static MediaPlayer mediaPlayer;

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        String desc = intent.getStringExtra("desc");
        Intent i = new Intent(context, E6_studentlogin_page.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, i, PendingIntent.FLAG_IMMUTABLE);

        Uri customRingtoneUri = Uri.parse("android.resource://" + context.getApplicationContext().getPackageName() + "/" + R.raw.ringtonesound1);

        // Inflate the custom notification layout
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);
        remoteViews.setTextViewText(R.id.notification_title, "Task Pending !"); // Set the description text
        remoteViews.setTextViewText(R.id.notification_text, desc); // Set the description text
        remoteViews.setOnClickPendingIntent(R.id.stop_button, getStopPendingIntent(context)); // Set click action for the stop button
//        remoteViews.setOnClickPendingIntent(R.id.see_details_button, getSeeDetailsPendingIntent(context)); // Set click action for the see details button
//getSeeDetailsPendingIntent


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarmchannel")
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle("Task Time")
                .setContentText("Weekly Planner")
                .setAutoCancel(false) // see this
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews);


        playAlarmSound(context, customRingtoneUri);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());

    }

    private void playAlarmSound(Context context, Uri alarmUri) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, alarmUri);
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PendingIntent getStopPendingIntent(Context context) {
        Intent intent = new Intent(context, StopRingtoneReceiver.class);
        intent.setAction("STOP_RINGTONE_ACTION"); // Set custom action for the intent
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    public static void stopRingtone() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
