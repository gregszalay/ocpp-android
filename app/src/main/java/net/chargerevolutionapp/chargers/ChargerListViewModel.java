package net.chargerevolutionapp.chargers;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChargerListViewModel extends AndroidViewModel {
    ChargerRepository chargerRepository;
    MutableLiveData<List<Charger>> chargerListMutableLiveData;
    FirebaseFirestore mFirestore;

    public ChargerListViewModel(Application application) {
        super(application);
        chargerRepository = new ChargerRepository();
        chargerListMutableLiveData =  chargerRepository.getChargerListMutableLiveData();
        mFirestore = FirebaseFirestore.getInstance();
    }

    //CREATE
    public void insert(Charger charger) {
        this.chargerRepository.insert(charger);
    }

    //READ
    public MutableLiveData<List<Charger>> getAllChargersLive() {
        return chargerListMutableLiveData;
    }

    //UPDATE
    public void update(Charger charger) {
        this.chargerRepository.update(charger);
    }

    //DELETE
    public void delete(Charger charger) {
        this.chargerRepository.delete(charger);
    }

}
