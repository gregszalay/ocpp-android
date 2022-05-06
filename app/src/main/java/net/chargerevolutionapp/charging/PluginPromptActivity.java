package net.chargerevolutionapp.charging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.chargerevolutionapp.HomeActivity;
import net.chargerevolutionapp.R;
import net.chargerevolutionapp.charging.ChargingActivity;

public class PluginPromptActivity extends AppCompatActivity {

    private TextView chargerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_prompt);
        this.chargerName = findViewById(R.id.chargerName);
        Bundle bundle = getIntent().getExtras();
        this.chargerName.setText(bundle.getString("ChargerName"));
    }


    public void cablePluggedInOK(View view) {
        Intent intent = new Intent(this, ChargingActivity.class);
        intent.putExtra("ChargerName", this.chargerName.getText().toString());
        startActivity(intent);
    }

    public void cancelChargingSession(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}