package net.chargerevolutionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.chargerevolutionapp.chargers.ChargerListActivity;
import net.chargerevolutionapp.charging.QRCodeScannerActivity;
import net.chargerevolutionapp.profiles.ProfileSettingsActivity;

public class HomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = HomeActivity.class.getName();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    TextView titleTextView;
    TextView emailInfoTextView;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        titleTextView = findViewById(R.id.homeTitle);
        emailInfoTextView = findViewById(R.id.emailInfo);
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        titleTextView.setText("Üdv!");
        emailInfoTextView.setText(this.currentUser.getEmail());

    }

    public void openChargingStationList(View view) {
        Intent intent = new Intent(this, ChargerListActivity.class);
        Log.i(LOG_TAG, "starting activity: ChargingStationListActivity.class");
        startActivity(intent);
    }

    public void scanQRCode(View view) {
        Log.i(LOG_TAG, "entered scanQRCode() method");
        checkUserPermissionAndOpenCamera();
    }

    public void editProfile(View view) {
        Intent intent = new Intent(this, ProfileSettingsActivity.class);
        startActivity(intent);
    }

    void checkUserPermissionAndOpenCamera() {
        Log.i(LOG_TAG, "entered checkUserPermissionAndOpenCamera() method");
        if (Build.VERSION.SDK_INT >= 23) {
            Log.i(LOG_TAG, "calling checkSelfPermission");
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.i(LOG_TAG, "calling requestPermissions");
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
        startQRScanner();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startQRScanner();
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startQRScanner() {
        Intent intent = new Intent(this, QRCodeScannerActivity.class);
        startActivity(intent);
    }

}
