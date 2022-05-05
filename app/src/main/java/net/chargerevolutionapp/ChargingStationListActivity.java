package net.chargerevolutionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ChargingStationListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ChargingStationListActivity.class.getName();
    private FirebaseUser user;

    private FrameLayout redCircle;
    private TextView countTextView;
    private int cartItems = 0;
    private int gridNumber = 1;
    private Integer itemLimit = 5;

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<ChargingStation> mItemsData;
    private ChargingStationAdapter mAdapter;
    private ArrayList<UserProfile> mProfileItemsData = new ArrayList<>();

    private CollectionReference profilesCollectionRef;


    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private SharedPreferences preferences;

    private boolean viewRow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_station_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        // preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        // if(preferences != null) {
        //     cartItems = preferences.getInt("cartItems", 0);
        //     gridNumber = preferences.getInt("gridNum", 1);
        // }

        // recycle view
        mRecyclerView = findViewById(R.id.recyclerView);
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));
        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();
        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new ChargingStationAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();

        mItems = mFirestore.collection("chargingStations");
        this.profilesCollectionRef = mFirestore.collection("userProfiles");

        mItemsData.clear();


        if (this.user != null && this.user.getEmail() != null) {
            this.profilesCollectionRef.whereEqualTo("userEmail", this.user.getEmail()).limit(1)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {



                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    UserProfile profile = document.toObject(UserProfile.class);
                    this.mProfileItemsData.add(profile);
                }

                mItems.orderBy("name").limit(100).get().addOnSuccessListener(queryDocumentSnapshots2 -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots2) {
                        ChargingStation item = document.toObject(ChargingStation.class);
                        //Filter based on user EV profile
                        if (item.getConnectorTypes().contains(this.mProfileItemsData.get(0).getCarConnector())) {
                            mItemsData.add(item);
                        }
                    }

                    if (mItemsData.size() == 0) {
                        Toast.makeText(this, "A profilodnak megfelelő töltő nem elérhető!", Toast.LENGTH_LONG).show();
                    } else {
                        // Notify the adapter of the change.
                    }
                    mAdapter.notifyDataSetChanged();
                });


            });
        }

//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_POWER_CONNECTED);
//        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
//        this.registerReceiver(powerReceiver, filter);

        // Intent intent = new Intent("CUSTOM_MOBALKFEJL_BROADCAST");
        // LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
//
//    BroadcastReceiver powerReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String intentAction = intent.getAction();
//
//            if (intentAction == null)
//                return;
//
//            switch (intentAction) {
//                case Intent.ACTION_POWER_CONNECTED:
//                    itemLimit = 10;
//                    queryData();
//                    break;
//                case Intent.ACTION_POWER_DISCONNECTED:
//                    itemLimit = 5;
//                    queryData();
//                    break;
//            }
//        }
//    };

    private void initializeData() {
        // Get the resources from the XML file.

       /*
        String[] itemsList = getResources()
                .getStringArray(R.array.shopping_item_names);
        String[] itemsInfo = getResources()
                .getStringArray(R.array.shopping_item_desc);
        String[] itemsPrice = getResources()
                .getStringArray(R.array.shopping_item_price);
        TypedArray itemsImageResources =
                getResources().obtainTypedArray(R.array.shopping_item_images);
        TypedArray itemRate = getResources().obtainTypedArray(R.array.shopping_item_rates);

        // Create the ArrayList of Sports objects with the titles and
        // information about each sport.
        for (int i = 0; i < itemsList.length; i++) {
            mItems.add(new ChargingStation(
                    itemsList[i],
                    itemsInfo[i],
                    itemsPrice[i],
                    itemRate.getFloat(i, 0),
                    itemsImageResources.getResourceId(i, 0)));
        }

        // Recycle the typed array.
        itemsImageResources.recycle();
        */

    }

    private void queryData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        switch (item.getItemId()) {
////            case R.id.log_out_button:
////                Log.d(LOG_TAG, "Logout clicked!");
////                FirebaseAuth.getInstance().signOut();
////                finish();
////                return true;
////            case R.id.settings_button:
////                Log.d(LOG_TAG, "Setting clicked!");
////                FirebaseAuth.getInstance().signOut();
////                finish();
////                return true;
////            case R.id.cart:
////                Log.d(LOG_TAG, "Cart clicked!");
////                return true;
////            case R.id.view_selector:
////                if (viewRow) {
////                    changeSpanCount(item, R.drawable.ic_view_grid, 1);
////                } else {
////                    changeSpanCount(item, R.drawable.ic_view_row, 2);
////                }
////                return true;
////            default:
////                return super.onOptionsItemSelected(item);
////        }
//        return null;
//    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
//        viewRow = !viewRow;
//        item.setIcon(drawableId);
//        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
//        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon() {
        cartItems = (cartItems + 1);
        if (0 < cartItems) {
            countTextView.setText(String.valueOf(cartItems));
        } else {
            countTextView.setText("");
        }

        redCircle.setVisibility((cartItems > 0) ? VISIBLE : GONE);
    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(powerReceiver);
//    }

    // @Override
    // protected void onPause() {
    //     super.onPause();

    //     SharedPreferences.Editor editor = preferences.edit();
    //     editor.putInt("cartItems", cartItems);
    //     editor.putInt("gridNum", gridNumber);
    //     editor.apply();

    //     Log.i(LOG_TAG, "onPause");
    // }
}