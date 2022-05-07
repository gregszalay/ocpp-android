package net.chargerevolutionapp.chargers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.chargerevolutionapp.charging.PluginPromptActivity;
import net.chargerevolutionapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChargerDetailsActivity extends AppCompatActivity {

    private TextView detailsItemName;
    private TextView detailsAddress;
    private TextView detailsConnectors;
    private TextView detailsMaxPower;
    private TextView reservationUntil;
    private Button reserveBtn;
    private Button cancelReserveBtn;
    private ChargerRepository chargerRepository;
    private Charger charger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_station_details);

        // Initialize the views.
        detailsItemName = findViewById(R.id.detailsItemName);
        detailsAddress = findViewById(R.id.detailsAddress);
        detailsConnectors = findViewById(R.id.detailsConnectors);
        detailsMaxPower = findViewById(R.id.detailsMaxPower);
        reservationUntil = findViewById(R.id.reservationUntil);
        reserveBtn = findViewById(R.id.reserveBtn);
        cancelReserveBtn = findViewById(R.id.cancelReserveBtn);

        Bundle bundle = getIntent().getExtras();
        String chargerName = bundle.getString("ChargerName");
        detailsItemName.setText(chargerName);

        chargerRepository = new ChargerRepository();

        this.chargerRepository.getChargerMutableLiveData(chargerName)
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
                        SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm:ss");
                        String formattedDate = fmtOut.format(new Date(charger.getReservedUntil()));
                        this.reservationUntil.setText("Lefoglalva " + formattedDate + "-ig");
                    }
                });
    }

    public void startCharging(View view) {
        Intent intent = new Intent(this, PluginPromptActivity.class);
        intent.putExtra("ChargerName", this.charger.getName());
        startActivity(intent);
    }

    public void reserveNow(View view) {
        long newEndOfReservationMillis = System.currentTimeMillis() + 600000;
        this.cancelReserveBtn.setVisibility(View.VISIBLE);
        this.reserveBtn.setVisibility(View.GONE);
        this.reservationUntil.setVisibility(View.VISIBLE);
        this.charger.setReserved(true);
        this.charger.setReservedUntil(newEndOfReservationMillis);
        this.charger.setReservedByUserEmail(this.chargerRepository.getLoggedInFirebaseUser().getEmail());
        this.chargerRepository.update(this.charger);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = fmtOut.format(new Date(newEndOfReservationMillis));
        this.reservationUntil.setText("Lefoglalva " + formattedDate + "-ig");
    }


    public void cancelReservation(View view) {
        this.reserveBtn.setVisibility(View.VISIBLE);
        this.cancelReserveBtn.setVisibility(View.GONE);
        this.reservationUntil.setVisibility(View.GONE);
        this.charger.setReserved(false);
        this.charger.setReservedUntil(0);
        this.charger.setReservedByUserEmail("");
        this.chargerRepository.update(this.charger);
    }
}