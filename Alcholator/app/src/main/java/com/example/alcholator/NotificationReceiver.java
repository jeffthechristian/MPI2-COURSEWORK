package com.example.alcholator;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the ID of the notification from the intent
        int notificationId = intent.getIntExtra("notification_id", 0);

        // Create the pending intent to launch the ResultActivity when the notification is clicked
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, ResultActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "my_notification_channel")
                .setSmallIcon(R.drawable.drunk_man)
                .setContentTitle("You are sober!")
                .setContentText("Congratulations, you are sober now and can drive!")
                .setContentIntent(pendingIntent);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}

