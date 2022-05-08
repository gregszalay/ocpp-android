package net.chargerevolutionapp.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.chargerevolutionapp.chargers.ChargerDetailsActivity;

public class ReservationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        new NotificationExecutor(context).send("A foglalásod hamarosan lejár!", ChargerDetailsActivity.class);
    }
}