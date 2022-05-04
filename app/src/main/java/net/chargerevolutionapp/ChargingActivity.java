package net.chargerevolutionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChargingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging);
    }

    public void endChargingSession(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}