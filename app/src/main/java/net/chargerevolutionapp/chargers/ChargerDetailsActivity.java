package net.chargerevolutionapp.chargers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import net.chargerevolutionapp.HomeActivity;
import net.chargerevolutionapp.charging.PluginPromptActivity;
import net.chargerevolutionapp.R;

public class ChargerDetailsActivity extends AppCompatActivity {

    private TextView detailsItemName;
    private TextView detailsAddress;
    private TextView detailsConnectors;
    private TextView detailsMaxPower;

    private Charger mCharger;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

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
        detailsItemName.setText(bundle.getString("ChargerName"));

        Log.i("details", "ChargerName");
        Log.i("details", bundle.getString("ChargerName"));

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("chargingStations");
        mItems.whereEqualTo("name", "RevolutionCharger1").limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Log.i("details", "entered for()");
                mCharger = document.toObject(Charger.class);
                detailsAddress.setText(mCharger.getAddress());
                detailsConnectors.setText(mCharger.getConnectorTypes());
                detailsMaxPower.setText(String.valueOf(mCharger.getMaxPowerInkW()));
            }
        });


    }

    public void startCharging(View view) {
        Intent intent = new Intent(this, PluginPromptActivity.class);
        intent.putExtra("ChargerName", this.mCharger.getName());
        startActivity(intent);
    }

    public void reserveNow(View view) {
        Toast.makeText(ChargerDetailsActivity.this, "Charger reserved until 10:35!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


}