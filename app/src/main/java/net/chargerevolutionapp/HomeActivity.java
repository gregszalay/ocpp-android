package net.chargerevolutionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void openChargingStationList(View view) {
        Intent intent = new Intent(this, ChargingStationListActivity.class);
        startActivity(intent);
    }

    public void scanQRCode(View view) {
    }

    public void editProfile(View view) {
    }
}