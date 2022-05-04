package net.chargerevolutionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ChargingActivity extends AppCompatActivity {

    private TextView mChargerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging);
        this.mChargerName = findViewById(R.id.sessionChargerName);
        Bundle bundle = getIntent().getExtras();
        this.mChargerName.setText(bundle.getString("ChargerName"));
    }

    public void endChargingSession(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}