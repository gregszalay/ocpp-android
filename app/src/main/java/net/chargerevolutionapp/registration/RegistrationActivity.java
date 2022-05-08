package net.chargerevolutionapp.registration;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import net.chargerevolutionapp.EVs.EVModel;
import net.chargerevolutionapp.EVs.EVModelRepository;
import net.chargerevolutionapp.HomeActivity;
import net.chargerevolutionapp.R;
import net.chargerevolutionapp.chargers.ChargerListActivity;
import net.chargerevolutionapp.profiles.UserProfile;
import net.chargerevolutionapp.profiles.UserProfileRepository;

import java.util.List;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    EditText userNameEditText;
    EditText userEmailEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;
    EditText connectorType;
    Spinner evModelSpinner;
    CheckBox isAdminCheckBox;
    private SharedPreferences preferences;
    private FirebaseAuth firebaseAuth;
    private UserProfileRepository userProfileRepository;
    private EVModel currentSelectedEVModel;
    private int currentEVIndex = 0;
    private EVModelRepository evModelRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Log.i(LOG_TAG, "onCreate()");

        firebaseAuth = FirebaseAuth.getInstance();
        this.userProfileRepository = new UserProfileRepository();
        this.connectorType = findViewById(R.id.connectorType);
        this.evModelSpinner = findViewById(R.id.evModelSpinner);
        this.isAdminCheckBox = findViewById(R.id.isAdminCheckBox);

        this.evModelRepository = new EVModelRepository();

        userNameEditText = findViewById(R.id.userNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordAgainEditText = findViewById(R.id.passwordAgainEditText);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        String password = preferences.getString("password", "");

        userNameEditText.setText(userName);
        passwordEditText.setText(password);
        passwordAgainEditText.setText(password);

        this.evModelRepository.getEVListMutableLiveData()
                .observe(this, evModelList -> setUpSpinner(evModelList));

    }

    private void setUpSpinner(List<EVModel> evModelList) {
        evModelSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<EVModel> evModelAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, evModelList);
        evModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        evModelSpinner.setAdapter(evModelAdapter);
        if (evModelList.size() > 0) {
            this.evModelSpinner.setSelection(0);
            Log.i(LOG_TAG, "Set selection: " + 0);
            this.currentEVIndex = 0;
        } else Log.w(LOG_TAG, "EV model list is empty!");

    }

    public void register(View view) {
        String userName = userNameEditText.getText().toString();
        String email = userEmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();

        if (!password.equals(passwordAgain)) {
            Log.i(LOG_TAG, "Nem egyezik a jelszó!");
            return;
        }

        Log.i(LOG_TAG, "Regisztrált: " + userName + ", email: " + email);
        //TODO
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(LOG_TAG, "User created successfully");
                saveProfile(view);
                goToHomeScreen();
            } else {
                Log.d(LOG_TAG, "User wasn't created successfully:", task.getException());
                Toast.makeText(RegistrationActivity.this, "User was't created successfully:", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void cancel(View view) {
        finish();
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void saveProfile(View view) {
        if (this.currentSelectedEVModel == null) {
            Log.w(LOG_TAG, "Save failed, no ev model selected!");
            return;
        }
        this.userProfileRepository.insert(new UserProfile(
                this.isAdminCheckBox.isChecked(),
                this.currentSelectedEVModel.getConnector(),
                this.currentSelectedEVModel.toString(),
                this.userEmailEditText.getText().toString())
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart()");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO
        EVModel selectedItem = (EVModel) adapterView.getItemAtPosition(i);
        String selectedItemString = selectedItem.toString();
        Log.i(LOG_TAG, selectedItemString);
        connectorType.setText(selectedItem.getConnector());
        this.currentSelectedEVModel = selectedItem;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        this.evModelSpinner.setSelection(this.currentEVIndex);
    }
}