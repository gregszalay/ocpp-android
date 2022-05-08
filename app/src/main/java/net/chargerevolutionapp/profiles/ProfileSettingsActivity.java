package net.chargerevolutionapp.profiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.chargerevolutionapp.EVs.EVModel;
import net.chargerevolutionapp.EVs.EVModelRepository;
import net.chargerevolutionapp.HomeActivity;
import net.chargerevolutionapp.R;

import java.util.List;

public class ProfileSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = ProfileSettingsActivity.class.getName();
    TextView profileSettingsTextView;
    EditText connectorType;
    Spinner evModelSpinner;
    private EVModel currentSelectedEVModel;
    private int currentEVIndex = 0;
    private EVModelRepository evModelRepository;
    private UserProfileRepository userProfileRepository;
    private FirebaseUser currentUser;
    private UserProfile currentUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        this.profileSettingsTextView = findViewById(R.id.profileSettingsTextView);
        this.connectorType = findViewById(R.id.connectorType);
        this.evModelSpinner = findViewById(R.id.evModelSpinner);

        this.evModelRepository = new EVModelRepository();
        this.userProfileRepository = new UserProfileRepository();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();

//        this.currentUser = this.userProfileRepository.getLoggedInFirebaseUser();

        if (this.currentUser != null && this.currentUser.getEmail() != null) {
            this.userProfileRepository.getUserProfileMutableLiveData(this.currentUser.getEmail())
                    .observe(this, userProfile -> {
                        this.currentUserProfile = userProfile;
                        Log.i(LOG_TAG, "[onCreate observer] this.currentUser.getEmail() : " + this.currentUser.getEmail());
                        Log.i(LOG_TAG, "[onCreate observer] userProfile.getID : " + userProfile._getDocID());
                        Log.i(LOG_TAG, "[onCreate observer] userProfile.getemail : " + userProfile.getUserEmail());

                        this.evModelRepository.getEVListMutableLiveData()
                                .observe(this, evModelList -> setUpSpinner(evModelList, userProfile));
                    });
        } else finish();

    }

    private void setUpSpinner(List<EVModel> evModelList, UserProfile userProfile) {
        if (userProfile == null) {
            Log.w(LOG_TAG, "User profile is null!");
            return;
        }
        evModelSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<EVModel> evModelAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, evModelList);
        evModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        evModelSpinner.setAdapter(evModelAdapter);
        if (evModelList.size() > 0) {
            for (int i = 0; i < evModelList.size(); i++) {
                if (userProfile.getCarModel().equals(evModelList.get(i).toString())) {
                    this.evModelSpinner.setSelection(i);
                    Log.i(LOG_TAG, "Set selection: " + i);
                    this.currentEVIndex = i;
                }
            }
        } else Log.w(LOG_TAG, "EV model list is empty!");

    }

    public void saveProfile(View view) {
        if (this.currentSelectedEVModel == null) {
            Log.w(LOG_TAG, "Save failed, no ev model selected!");
            return;
        }
        Log.i(LOG_TAG, "[saveProfile] this.currentUser.getEmail() : " + this.currentUser.getEmail());

        if (this.currentUserProfile == null) {
            this.userProfileRepository.insert(new UserProfile(
                    false, this.currentSelectedEVModel.getConnector(),
                    this.currentSelectedEVModel.toString(),
                    this.currentUser.getEmail())
            );
        } else {
            this.currentUserProfile.setCarModel(this.currentSelectedEVModel.toString());
            this.currentUserProfile.setCarConnector(this.currentSelectedEVModel.getConnector());
            this.userProfileRepository.update(this.currentUserProfile);
        }
        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);

    }

    public void cancelProfileSettings(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
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