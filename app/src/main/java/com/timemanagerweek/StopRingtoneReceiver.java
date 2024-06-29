package com.timemanagerweek;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

public class StopRingtoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("STOP_RINGTONE_ACTION".equals(intent.getAction())) {
            AlarmRecevier.stopRingtone();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(1);
        }
//        else if ("SEE_DETAILS_ACTION".equals(intent.getAction())) {
//            AlarmRecevier.stopRingtone();
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//            notificationManager.cancel(1);
//
//            // Start the E6_studentlogin_page activity directly
//            Intent i = new Intent(context, E6_studentlogin_page.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            context.startActivity(i);
//        }
    }
}
