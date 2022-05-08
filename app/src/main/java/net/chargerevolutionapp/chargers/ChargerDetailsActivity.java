package net.chargerevolutionapp.chargers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.chargerevolutionapp.charging.PluginPromptActivity;
import net.chargerevolutionapp.R;
import net.chargerevolutionapp.notifications.ReservationAlarmReceiver;
import net.chargerevolutionapp.notifications.NotificationExecutor;
import net.chargerevolutionapp.profiles.UserProfileRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChargerDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = ChargerDetailsActivity.class.getName();
    private NotificationExecutor notificationExecutor;

    private TextView detailsItemName;
    private TextView detailsAddress;
    private TextView detailsConnectors;
    private TextView detailsMaxPower;
    private TextView reservationUntil;
    private Button reserveBtn;
    private Button cancelReserveBtn;
    private Button updateButton;
    private Button deleteBtn;
    private ChargerRepository chargerRepository;
    private UserProfileRepository userProfileRepository;
    private Charger charger;
    private AlarmManager alarmManager;
    private PendingIntent reservationAlarmIntent;

    private String chargerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_station_details);

        this.userProfileRepository = new UserProfileRepository();

        detailsItemName = findViewById(R.id.detailsItemName);
        detailsAddress = findViewById(R.id.detailsAddress);
        detailsConnectors = findViewById(R.id.detailsConnectors);
        detailsMaxPower = findViewById(R.id.detailsMaxPower);
        reservationUntil = findViewById(R.id.reservationUntil);
        reserveBtn = findViewById(R.id.reserveBtn);
        cancelReserveBtn = findViewById(R.id.cancelReserveBtn);
        updateButton = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        Bundle bundle = getIntent().getExtras();
        this.chargerName = bundle.getString("ChargerName");
        detailsItemName.setText(chargerName);

        chargerRepository = new ChargerRepository();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.i(LOG_TAG, "user is null");
            return;
        }
        this.userProfileRepository.getUserProfileMutableLiveData(user.getEmail())
                .observe(
                        this, profile -> {
                            if (profile != null && profile.getIsAdmin()) {
                                Log.i(LOG_TAG, "setting update button visible...");
                                updateButton.setVisibility(View.VISIBLE);
                                deleteBtn.setVisibility(View.VISIBLE);
                            }
                        }
                );

        this.chargerRepository.getChargerMutableLiveData(this.chargerName)
                .observe(this, charger -> {
                    this.charger = charger;
                    detailsAddress.setText(charger.getAddress());
                    detailsConnectors.setText(charger.getConnectorTypes());
                    detailsMaxPower.setText(String.valueOf(charger.getMaxPowerInkW()));
                    if (charger.getReservedUntil() > System.currentTimeMillis()) {
                        if (this.chargerRepository.getLoggedInFirebaseUser().getEmail().equals(
                                charger.getReservedByUserEmail()
                        )) {
                            this.cancelReserveBtn.setVisibility(View.VISIBLE);
                        }
                        this.reserveBtn.setVisibility(View.GONE);
                        this.reservationUntil.setVisibility(View.VISIBLE);
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
                        String formattedDate = fmtOut.format(new Date(charger.getReservedUntil()));
                        this.reservationUntil.setText("Lefoglalva " + formattedDate + "-ig");


                    }
                });

        Log.i(LOG_TAG, "Setting up notification & alarm");
        notificationExecutor = new NotificationExecutor(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private PendingIntent setReservationAlarm(Intent intent, long repeatInterval) {
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Log.i(LOG_TAG, "Setting alarm");

        alarmManager.setExact(
                AlarmManager.ELAPSED_REALTIME,
                triggerTime,
                pendingIntent);

        return pendingIntent;
    }


    public void startCharging(View view) {
        Intent intent = new Intent(this, PluginPromptActivity.class);
        intent.putExtra("ChargerName", this.charger.getName());
        startActivity(intent);
        if (this.reservationAlarmIntent != null) alarmManager.cancel(this.reservationAlarmIntent);
        this.charger.setCharging(true);
        this.charger.setWhoIsChargingEmail(this.chargerRepository.getLoggedInFirebaseUser().getEmail());
        this.chargerRepository.update(this.charger);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void reserveNow(View view) {
        long newEndOfReservationMillis = System.currentTimeMillis() + 600000;

        this.reservationAlarmIntent = setReservationAlarm(
                new Intent(this, ReservationAlarmReceiver.class),
                10 * 1000
        );

        this.cancelReserveBtn.setVisibility(View.VISIBLE);
        this.reserveBtn.setVisibility(View.GONE);
        this.reservationUntil.setVisibility(View.VISIBLE);

        this.charger.setReserved(true);
        this.charger.setReservedUntil(newEndOfReservationMillis);
        this.charger.setReservedByUserEmail(this.chargerRepository.getLoggedInFirebaseUser().getEmail());
        this.chargerRepository.update(this.charger);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
        String formattedDate = fmtOut.format(new Date(newEndOfReservationMillis));
        this.reservationUntil.setText("Lefoglalva " + formattedDate + "-ig");

        notificationExecutor.send(this.charger.getName() + " lefoglalva " + formattedDate + "-ig");

    }


    public void cancelReservation(View view) {

        if (this.reservationAlarmIntent != null) alarmManager.cancel(this.reservationAlarmIntent);

        this.reserveBtn.setVisibility(View.VISIBLE);
        this.cancelReserveBtn.setVisibility(View.GONE);
        this.reservationUntil.setVisibility(View.GONE);

        this.charger.setReserved(false);
        this.charger.setReservedUntil(0);
        this.charger.setReservedByUserEmail("");
        this.chargerRepository.update(this.charger);

        notificationExecutor.cancel();

    }

    public void updateCharger(View view) {
        Intent intent = new Intent(this, ChargerFormActivity.class);
        intent.putExtra("ChargerName", this.chargerName);
        startActivity(intent);
    }

    public void deleteCharger(View view) {
        if (this.reservationAlarmIntent != null) alarmManager.cancel(this.reservationAlarmIntent);
        notificationExecutor.cancel();

        this.chargerRepository.delete(this.charger);
        Intent intent = new Intent(this, ChargerListActivity.class);
        startActivity(intent);

    }
}