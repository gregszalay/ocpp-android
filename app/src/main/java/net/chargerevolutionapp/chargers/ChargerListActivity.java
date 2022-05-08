package net.chargerevolutionapp.chargers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.chargerevolutionapp.R;
import net.chargerevolutionapp.profiles.UserProfile;
import net.chargerevolutionapp.profiles.UserProfileRepository;

public class ChargerListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ChargerListActivity.class.getName();
    private final int gridNumber = 1;
    // ProgressDialog progressDialog;
    private RecyclerView chargerRecyclerView;
    private ChargerAdapter chargerAdapter;
    private ChargerListViewModel chargerListViewModel;
    private UserProfileRepository userProfileRepository;
    private FirebaseUser currentUser;
    private Button newChargerButton;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_station_list);
        this.userProfileRepository = new UserProfileRepository();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.newChargerButton = findViewById(R.id.newChargerButton);
        //this.userProfileRepository.getLoggedInFirebaseUser();

        //ProgressDialog
//        this.progressDialog = new ProgressDialog(this);
//        this.progressDialog.setTitle("Loading ...");
//        this.progressDialog.show();

        //Layout
        this.chargerRecyclerView = findViewById(R.id.recyclerView);
        this.chargerRecyclerView.setLayoutManager(new GridLayoutManager(this, this.gridNumber));

        if (this.currentUser != null && this.currentUser.getEmail() != null) {
            this.userProfileRepository.getUserProfileMutableLiveData(this.currentUser.getEmail())
                    .observe(this, userProfile -> setUpChargerListObserver(userProfile));
        } else {
            Log.i(LOG_TAG, "Setting up charger list for example user!");
            setUpChargerListObserver(new UserProfile(
                    false,
                    "Type2",
                    "ExampleEV",
                    "example@example.com"
            ));
        }

        this.userProfileRepository.getUserProfileMutableLiveData(this.currentUser.getEmail())
                .observe(
                        this, profile -> {
                            if (profile != null && profile.getIsAdmin()) {
                                Log.i(LOG_TAG, "setting update button visible...");
                                newChargerButton.setVisibility(View.VISIBLE);
                            }
                        }
                );



    }



    private void setUpChargerListObserver(UserProfile userProfile) {
        this.chargerListViewModel = new ChargerListViewModel(this.getApplication(), userProfile);
        //Set up charger list
        this.chargerListViewModel.getAllChargersLive().observe(this, chargerList -> {
            // progressDialog.dismiss();
            this.chargerAdapter = new ChargerAdapter(this, chargerList);
            this.chargerRecyclerView.setAdapter(this.chargerAdapter);
            this.chargerAdapter.notifyDataSetChanged();
        });


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void newCharger(View view) {
        Intent intent = new Intent(this, ChargerFormActivity.class);
        intent.putExtra("ChargerName", "");
        startActivity(intent);
    }
}