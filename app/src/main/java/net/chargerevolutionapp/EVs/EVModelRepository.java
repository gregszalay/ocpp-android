package net.chargerevolutionapp.EVs;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import net.chargerevolutionapp.AbstractRepository;

import java.util.ArrayList;
import java.util.List;

public class EVModelRepository extends AbstractRepository {
    private static final String LOG_TAG = net.chargerevolutionapp.chargers.ChargerRepository.class.getName();
    private final MutableLiveData<List<EVModel>> evListMutableLiveData;
    private final CollectionReference evModelCollectionRef;

    public EVModelRepository() {
        this.evListMutableLiveData = new MutableLiveData<>();
        this.evModelCollectionRef = super.mFirestore.collection("evModels");
    }

    //CREATE
    public void insert(EVModel evModel) {
        new Insert(this.evModelCollectionRef).execute(evModel);
    }

    //READ
    public MutableLiveData<List<EVModel>> getEVListMutableLiveData() {
        Log.i(LOG_TAG, "getChargerListMutableLiveData() ");
        this.evModelCollectionRef.addSnapshotListener((value, error) -> {
            List<EVModel> evModelList = new ArrayList<>();
            assert value != null;
            for (QueryDocumentSnapshot doc : value) {
                if (doc != null) {
                    EVModel evModelItem = doc.toObject(EVModel.class);
                    evModelItem.setID(doc.getId());
                    evModelList.add(evModelItem);
                }
            }
            evListMutableLiveData.postValue(evModelList);
        });
        return evListMutableLiveData;
    }

    //UPDATE
    public void update(EVModel evModel) {
        new Update(this.evModelCollectionRef).execute(evModel);
    }

    //DELETE
    public void delete(EVModel evModel) {
        new Delete(this.evModelCollectionRef).execute(evModel);
    }

}


