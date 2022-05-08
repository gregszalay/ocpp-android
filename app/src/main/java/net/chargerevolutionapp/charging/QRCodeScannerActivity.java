package net.chargerevolutionapp.charging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import net.chargerevolutionapp.R;

public class QRCodeScannerActivity extends AppCompatActivity {

    private static final String LOG_TAG = QRCodeScannerActivity.class.getName();
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "Creating QRCodeScannerActivity instance");
        setContentView(R.layout.activity_qrcode_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QRCodeScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(QRCodeScannerActivity.this, PluginPromptActivity.class);
                        intent.putExtra("ChargerName", result.getText());
                        QRCodeScannerActivity.this.startActivity(intent);
                    }
                });
            }
        });
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
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