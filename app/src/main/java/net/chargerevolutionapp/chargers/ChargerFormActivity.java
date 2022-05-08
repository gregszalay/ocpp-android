package net.chargerevolutionapp.chargers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import net.chargerevolutionapp.R;

public class ChargerFormActivity extends AppCompatActivity {

    private static final String LOG_TAG = ChargerFormActivity.class.getName();


    private EditText name;
    private EditText country;
    private EditText postCode;
    private EditText address;
    private EditText connectorTypes;
    private EditText maxPowerInkW;
    private ChargerRepository chargerRepository;
    private String updatedChargerName;
    private Charger updatedCharger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_form);

        chargerRepository = new ChargerRepository();

        name = findViewById(R.id.name);
        country = findViewById(R.id.country);
        postCode = findViewById(R.id.postCode);
        address = findViewById(R.id.address);
        connectorTypes = findViewById(R.id.connectorTypes);
        maxPowerInkW = findViewById(R.id.maxPowerInkW);

        Bundle bundle = getIntent().getExtras();
        Log.i(LOG_TAG, "valami");

        if (!bundle.getString("ChargerName").equals("")) {
            this.chargerRepository.getChargerMutableLiveData(this.updatedChargerName).observe(
                    this, charger -> {
                        this.updatedCharger = charger;
                        name.setText(charger.getName());
                        country.setText(charger.getCountry());
                        postCode.setText(charger.getPostCode());
                        address.setText(charger.getAddress());
                        connectorTypes.setText(charger.getConnectorTypes());
                        maxPowerInkW.setText(String.valueOf(charger.getMaxPowerInkW()));
                    }
            );
        }
    }

    public void saveCharger(View view) {
        if (this.updatedCharger != null) {
            this.updatedCharger.setName(name.getText().toString());
            this.updatedCharger.setCountry(country.getText().toString());
            this.updatedCharger.setPostCode(postCode.getText().toString());
            this.updatedCharger.setAddress(address.getText().toString());
            this.updatedCharger.setConnectorTypes(connectorTypes.getText().toString());
            this.updatedCharger.setMaxPowerInkW(Integer.parseInt(maxPowerInkW.getText().toString()));
            this.chargerRepository.update(this.updatedCharger);
        } else {
            this.chargerRepository.insert(new Charger(
                    name.getText().toString(),
                    country.getText().toString(),
                    postCode.getText().toString(),
                    address.getText().toString(),
                    connectorTypes.getText().toString(),
                    Integer.parseInt(maxPowerInkW.getText().toString())
            ));
        }
        Intent intent = new Intent(this, ChargerDetailsActivity.class);
        intent.putExtra("ChargerName", name.getText().toString());
        startActivity(intent);
    }
}