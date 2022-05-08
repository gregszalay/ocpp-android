package net.chargerevolutionapp.charging;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.chargerevolutionapp.HomeActivity;
import net.chargerevolutionapp.R;
import net.chargerevolutionapp.chargers.Charger;
import net.chargerevolutionapp.chargers.ChargerDetailsActivity;
import net.chargerevolutionapp.chargers.ChargerRepository;
import net.chargerevolutionapp.notifications.NotificationExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

import kotlin.text.UStringsKt;

public class ChargingActivity extends AppCompatActivity {

    private TextView chargerNameTextView;
    private String chargerName;
    private Charger charger;
    private ChargerRepository chargerRepository;
    private NotificationExecutor notificationExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging);
        this.chargerNameTextView = findViewById(R.id.sessionChargerName);
        Bundle bundle = getIntent().getExtras();
        this.chargerNameTextView.setText(bundle.getString("ChargerName"));
        this.chargerName = bundle.getString("ChargerName");
        chargerRepository = new ChargerRepository();

        this.chargerRepository.getChargerMutableLiveData(bundle.getString("ChargerName"))
                .observe(this, charger -> {
                    this.charger = charger;
                });

        notificationExecutor = new NotificationExecutor(this);

    }

    public void endChargingSession(View view) {
        if (this.charger != null) {
            this.charger.setCharging(false);
            this.charger.setWhoIsChargingEmail("");
            this.chargerRepository.update(this.charger);
        }
        notificationExecutor.cancel();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        notificationExecutor.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.charger != null && this.charger.isCharging()) {
            notificationExecutor.send(
                    this.chargerName + ": töltés folyamatban!",
                    ChargingActivity.class
            );
        }
    }

}