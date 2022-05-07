package net.chargerevolutionapp.chargers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_station_list);
        this.userProfileRepository = new UserProfileRepository();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
                    "Type2",
                    "ExampleEV",
                    "example@example.com"
            ));
        }

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
//        chargerListViewModel.removeUserFilter();
//        //unregisterReceiver(powerReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        chargerListViewModel.removeUserFilter();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (this.currentUser != null && this.currentUser.getEmail() != null) {
//            UserProfile profileOfCurrentUser =
//                    this.userProfileRepository.getUserProfile(this.currentUser.getEmail());
//            chargerListViewModel.addUserFilter(profileOfCurrentUser);
//        } else finish();
    }

    private void filterChargersForCurrentUser() {
        //Filter chargers for user
//        if (this.currentUser != null && this.currentUser.getEmail() != null) {
//            UserProfile profileOfCurrentUser =
//                    this.userProfileRepository.getUserProfile(this.currentUser.getEmail());
//            this.chargerListViewModel.addUserFilter(profileOfCurrentUser);
//        } else finish();
    }

}