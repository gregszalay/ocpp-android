package net.chargerevolutionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChargingStationDetailsActivity extends AppCompatActivity {

    private TextView detailsItemName;
    private TextView detailsAddress;
    private TextView detailsConnectors;
    private TextView detailsMaxPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_station_details);

        // Initialize the views.
        detailsItemName = findViewById(R.id.detailsItemName);
        detailsAddress = findViewById(R.id.detailsAddress);
        detailsConnectors = findViewById(R.id.detailsConnectors);
        detailsMaxPower = findViewById(R.id.detailsMaxPower);

        Bundle bundle = getIntent().getExtras();

        detailsItemName.setText(bundle.getString("ItemName"));
        detailsAddress.setText(bundle.getString("Address"));
        detailsConnectors.setText(bundle.getString("Connectors"));
        detailsMaxPower.setText(bundle.getString("MaxPower"));

        Log.i("details", "ItemName");
        Log.i("details", bundle.getString("ItemName"));


    }

    public void startCharging(View view) {
        Intent intent = new Intent(this, ChargingActivity.class);
        startActivity(intent);
    }

    public void reserveNow(View view) {
        Toast.makeText(ChargingStationDetailsActivity.this, "Charger reserved until 10:35!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }
}