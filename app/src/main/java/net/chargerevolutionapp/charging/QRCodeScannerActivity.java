package net.chargerevolutionapp.charging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.SkuDetails;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

import net.chargerevolutionapp.R;
import net.chargerevolutionapp.chargers.Charger;
import net.chargerevolutionapp.chargers.ChargerRepository;

public class QRCodeScannerActivity extends AppCompatActivity {

    private static final String LOG_TAG = QRCodeScannerActivity.class.getName();
    private CodeScanner mCodeScanner;
    private String scannedChargerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "Creating QRCodeScannerActivity instance");
        setContentView(R.layout.activity_qrcode_scanner);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            Toast.makeText(QRCodeScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(QRCodeScannerActivity.this, ChargerDetailsActivity.class);
            // intent.putExtra("ChargerName", result.getText());
            scannedChargerName = result.getText();
                        startCharging(scannerView);

            // startActivity(intent);
            //QRCodeScannerActivity.this.startActivity(intent);
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());

    }

    public void startCharging(View view) {
        Intent intent = new Intent(this, PluginPromptActivity.class);
        intent.putExtra("ChargerName", scannedChargerName);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}