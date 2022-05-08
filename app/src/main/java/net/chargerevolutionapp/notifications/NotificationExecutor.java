package net.chargerevolutionapp.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import net.chargerevolutionapp.R;
import net.chargerevolutionapp.chargers.ChargerListActivity;

public class NotificationExecutor {
    private static final String CHANNEL_ID = "chargerrevolution_notification_channel";
    private final int NOTIFICATION_ID = 0;
    private final NotificationManager notificationManager;
    private final Context context;

    public NotificationExecutor(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel
                (CHANNEL_ID, "Chargerrevolution Notification", NotificationManager.IMPORTANCE_HIGH);

        channel.enableLights(true);
        channel.setLightColor(Color.YELLOW);
        channel.enableVibration(true);
        channel.setDescription("Notifications from the Chargerrevolution application");

        notificationManager.createNotificationChannel(channel);
    }

    public void send(String message) {
        Intent intent = new Intent(context, ChargerListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                NOTIFICATION_ID,
                intent
                ,
                PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Chargerrevolution Application")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}