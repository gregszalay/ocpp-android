package net.chargerevolutionapp.charging;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.chargerevolutionapp.HomeActivity;
import net.chargerevolutionapp.R;
import net.chargerevolutionapp.chargers.Charger;
import net.chargerevolutionapp.chargers.ChargerRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChargingActivity extends AppCompatActivity {

    private TextView mChargerName;
    private Charger charger;
        private ChargerRepository chargerRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging);
        this.mChargerName = findViewById(R.id.sessionChargerName);
        Bundle bundle = getIntent().getExtras();
        this.mChargerName.setText(bundle.getString("ChargerName"));
        chargerRepository = new ChargerRepository();

        this.chargerRepository.getChargerMutableLiveData(bundle.getString("ChargerName"))
                .observe(this, charger -> {
                    this.charger = charger;
                });

    }

    public void endChargingSession(View view) {
        if(this.charger != null) {
            this.charger.setCharging(false);
            this.charger.setWhoIsChargingEmail("");
            this.chargerRepository.update(this.charger);
        }
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }
}