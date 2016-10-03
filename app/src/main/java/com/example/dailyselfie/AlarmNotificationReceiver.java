package com.example.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class AlarmNotificationReceiver extends BroadcastReceiver {

    private static final int MY_NOTIFICATION_ID = 1;

    private final CharSequence tickerText = "It's selfie time";
    private final CharSequence contentTitle = "DAILY SELFIE";
    private final CharSequence contentText = "Time for another selfie!";

    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;

    private final Uri soundURI = Uri.parse("android.resource://com.example.dailyselfie/"+R.raw.alarm_rooster);
    @Override
    public void onReceive(Context context, Intent intent) {

        mNotificationIntent = new Intent(context,MainActivity.class);
        mContentIntent = PendingIntent.getActivity(context,0,mNotificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(context);
        notificationBuilder.setTicker(tickerText);
        notificationBuilder.setContentTitle(contentTitle);
        notificationBuilder.setContentText(contentText);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.cameraicon);
        notificationBuilder.setSound(soundURI);
        notificationBuilder.setContentIntent(mContentIntent);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(MY_NOTIFICATION_ID,notificationBuilder.build());
    }
}
