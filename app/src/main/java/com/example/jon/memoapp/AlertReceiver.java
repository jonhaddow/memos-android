package com.example.jon.memoapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class handles an alert broadcast.
 * It checks the broadcast with the original alert sent,
 * then notifies the user of the "urgent" memo.
 */
public class AlertReceiver extends BroadcastReceiver {

    /**
     * This method is called when an alert broadcast is received.
     *
     * @param context Context where the alert was set.
     * @param intent  Intent sent with the alert.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        // Get the singleton database instance.
        DbHelper dbHelper = DbHelper.getInstance(context);

        // Get the memoId from intent.
        int memoId = intent.getIntExtra(MainActivity.INTENT_EXTRA_ID, 0);

        // Check that alert still exists (hasn't been cancelled).
        Alert alert = dbHelper.getAlert(memoId);
        if (alert == null) {
            return;
        }

        // Get current memo associated with the alert.
        Memo memo = dbHelper.getMemo(memoId);

        // Create notification with Memo Name as main text.
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(memo.getName())
                        .setAutoCancel(true); // Dismiss on click.

        // Create an explicit intent for the MainActivity when user clicks on notification.
        Intent resultIntent = new Intent(context, MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT
                );

        // Attach intent to notification.
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Notify user.
        mNotificationManager.notify(memoId, mBuilder.build());

        // Delete alert from alerts table.
        dbHelper.removeAlert(memoId);
    }
}
