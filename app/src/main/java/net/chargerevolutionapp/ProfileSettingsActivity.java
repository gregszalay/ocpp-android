package net.chargerevolutionapp;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = ProfileSettingsActivity.class.getName();
    TextView profileSettingsTextView;
    EditText connectorType;
    Spinner evModelSpinner;
    private ArrayList<EVModel> mEvModelItemsData = new ArrayList<>();
    private ArrayList<UserProfile> mProfileItemsData = new ArrayList<>();
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private CollectionReference evModelsCollectionRef;
    private CollectionReference profilesCollectionRef;
    private EVModel currentSelectedEVModel;
    private int currentEVIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        //Set up layout
        this.profileSettingsTextView = findViewById(R.id.profileSettingsTextView);
        this.connectorType = findViewById(R.id.connectorType);
        this.evModelSpinner = findViewById(R.id.evModelSpinner);

        // Query all evModels
        this.mFirestore = FirebaseFirestore.getInstance();
        this.evModelsCollectionRef = mFirestore.collection("evModels");
        this.profilesCollectionRef = mFirestore.collection("userProfiles");

        //Get user info
        this.user = FirebaseAuth.getInstance().getCurrentUser();

        // Get profile of current user
        if (this.user != null && this.user.getEmail() != null) {
            this.profilesCollectionRef.whereEqualTo("userEmail", this.user.getEmail()).limit(1)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    UserProfile profile = document.toObject(UserProfile.class);
                    this.mProfileItemsData.add(profile);
                }
                this.mEvModelItemsData.clear();
                this.evModelsCollectionRef.orderBy("manufacturer").limit(100).get().addOnSuccessListener(queryDocumentSnapshots2 -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots2) {
                        EVModel model = document.toObject(EVModel.class);
                        this.mEvModelItemsData.add(model);
                    }
                    //Set up spinner
                    evModelSpinner.setOnItemSelectedListener(this);
                    ArrayAdapter<EVModel> evModelAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, mEvModelItemsData);
                    evModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    evModelSpinner.setAdapter(evModelAdapter);
                    if (this.mProfileItemsData.size() > 0) {
                        for (int i = 0; i < this.mEvModelItemsData.size(); i++) {
                            if (this.mProfileItemsData.get(0).getCarModel().equals(this.mEvModelItemsData.get(i).toString())) {
                                this.evModelSpinner.setSelection(i);
                                this.currentEVIndex = i;

                            }
                        }
                    }
                });
            });
        }

    }

    public void saveProfile(View view) {
        if (this.currentSelectedEVModel == null) {
            Log.w(LOG_TAG, "Save failed, no ev model selected!");
            return;
        }

        if (this.user == null || this.user.getEmail() == null) {
            Log.w(LOG_TAG, "Save failed, no user info available!");
            return;
        }

        Map<String, Object> newProfile = new HashMap<>();
        newProfile.put("carConnector", this.currentSelectedEVModel.getConnector());
        newProfile.put("carModel", this.currentSelectedEVModel.toString());
        newProfile.put("userEmail", this.user.getEmail());

        this.profilesCollectionRef
                .document(this.user.getEmail())
                .set(newProfile)
                .addOnSuccessListener(aVoid -> {
                    Log.d(LOG_TAG, "User EV profile saved!");
                    Toast.makeText(this, "Profil mentve!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, HomeActivity.class);
                    this.startActivity(intent);
                })
                .addOnFailureListener(e -> Log.w(LOG_TAG, "Error writing document", e));

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