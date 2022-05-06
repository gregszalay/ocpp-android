package net.chargerevolutionapp.chargers;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import net.chargerevolutionapp.AbstractRepository;

import java.util.ArrayList;
import java.util.List;

public class ChargerRepository extends AbstractRepository {

    private static final String LOG_TAG = ChargerRepository.class.getName();
    private final MutableLiveData<List<Charger>> chargerListMutableLiveData;
    private final MutableLiveData<Charger> chargerMutableLiveData;
    private final CollectionReference chargingStationsCollectionRef;
    private final FirebaseFirestore mFirestore;

    public ChargerRepository() {
        this.chargerListMutableLiveData = new MutableLiveData<>();
        this.mFirestore = FirebaseFirestore.getInstance();
        this.chargingStationsCollectionRef = mFirestore.collection("chargingStations");
        chargerMutableLiveData = new MutableLiveData<>();
    }

    //CREATE
    public void insert(Charger charger) {
        new Insert(this.chargingStationsCollectionRef).execute(charger);
    }

    //READ
    public MutableLiveData<List<Charger>> getChargerListMutableLiveData() {
        Log.i(LOG_TAG, "getChargerListMutableLiveData() ");
        mFirestore.collection("chargingStations").addSnapshotListener((value, error) -> {
            List<Charger> chargerList = new ArrayList<>();
            assert value != null;
            for (QueryDocumentSnapshot doc : value) {
                if (doc != null) {
                    chargerList.add(doc.toObject(Charger.class));
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

