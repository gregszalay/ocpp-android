package net.chargerevolutionapp.chargers;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import net.chargerevolutionapp.AbstractRepository;
import net.chargerevolutionapp.profiles.UserProfile;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChargerRepository extends AbstractRepository {

    private static final String LOG_TAG = ChargerRepository.class.getName();
    private final MutableLiveData<List<Charger>> chargerListMutableLiveData;
    private final CollectionReference chargingStationsCollectionRef;

    public ChargerRepository() {
        this.chargerListMutableLiveData = new MutableLiveData<>();
        this.chargingStationsCollectionRef = super.mFirestore.collection("chargingStations");
    }

    //CREATE
    public void insert(Charger charger) {
        new Insert(this.chargingStationsCollectionRef).execute(charger);
    }

    //READ
    public MutableLiveData<List<Charger>> getChargerListMutableLiveData(UserProfile profile) {
        Log.i(LOG_TAG, "getChargerListMutableLiveData() ");
        this.chargingStationsCollectionRef.addSnapshotListener((value, error) -> {
            List<Charger> chargerList = new ArrayList<>();
            assert value != null;
            for (QueryDocumentSnapshot doc : value) {
                if (doc != null) {
                    Charger chargerItem = doc.toObject(Charger.class);
                    if (chargerItem.getConnectorTypes().contains(profile.getCarConnector())) {
                        chargerItem.setID(doc.getId());
                        chargerList.add(chargerItem);
                    }
                }
            }
            chargerListMutableLiveData.postValue(chargerList);
        });
        return chargerListMutableLiveData;
    }

    //UPDATE
    public void update(Charger charger) {
        new Update(this.chargingStationsCollectionRef).execute(charger);
    }

    //DELETE
    public void delete(Charger charger) {
        new Delete(this.chargingStationsCollectionRef).execute(charger);
    }

}

