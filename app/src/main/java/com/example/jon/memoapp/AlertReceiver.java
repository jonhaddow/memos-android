package com.example.jon.memoapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlertReceiver extends BroadcastReceiver {

    /**
     * This method is called an alert is triggered.
     * @param context Context where the alert was set
     * @param intent Intent sent with the alert
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        // Create notification with Memo Name
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Memo Alert")
                        .setContentText(intent.getStringExtra(MainActivity.INTENT_EXTRA_NAME))
                        .setAutoCancel(true);

        // Creates an explicit intent for the MainActivity when user clicks on notification
        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(intent.getIntExtra(MainActivity.INTENT_EXTRA_ID, 0), mBuilder.build());
    }
}
