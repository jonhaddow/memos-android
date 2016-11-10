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
     *
     * @param context Context where the alert was set
     * @param intent  Intent sent with the alert
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        DbHelper dbHelper = DbHelper.getInstance(context);

        int memoId = intent.getIntExtra(MainActivity.INTENT_EXTRA_ID, 0);
        String memoName = intent.getStringExtra(MainActivity.INTENT_EXTRA_NAME);

        // Check that alert still exists (hasn't been cancelled)
        Alert alert = dbHelper.getAlert(memoId);
        if (alert == null) {
            System.out.println("no alert found (receiver)");
            return;
        }


        // Create notification with Memo Name
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Memo Alert")
                        .setContentText(memoName)
                        .setAutoCancel(true);

        // Creates an explicit intent for the MainActivity when user clicks on notification
        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(memoId, mBuilder.build());

        // Delete alert from table
        dbHelper.removeAlert(memoId);

    }
}
