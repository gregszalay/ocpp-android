package net.chargerevolutionapp.profiles;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import net.chargerevolutionapp.AbstractRepository;

import java.util.ArrayList;
import java.util.List;

public class UserProfileRepository extends AbstractRepository {

    private static final String LOG_TAG = net.chargerevolutionapp.chargers.ChargerRepository.class.getName();
    private final MutableLiveData<UserProfile> userProfileMutableLiveData;
    private final CollectionReference userProfilesCollectionRef;

    public UserProfileRepository() {
        this.userProfileMutableLiveData = new MutableLiveData<>();
        this.userProfilesCollectionRef = super.mFirestore.collection("userProfiles");
    }

    //CREATE
    public void insert(UserProfile userProfile) {
        new Insert(this.userProfilesCollectionRef).execute(userProfile);
    }

    //READ
    public MutableLiveData<UserProfile> getUserProfileMutableLiveData(String userEmail) {
        Log.i(LOG_TAG, "getUserProfileMutableLiveData() ");
        super.mFirestore.collection("userProfiles")
                .whereEqualTo("userEmail", userEmail)
                .limit(1)
                .addSnapshotListener((value, error) -> {
                    List<UserProfile> useProfileList = new ArrayList<>();
                    assert value != null;
                    for (QueryDocumentSnapshot doc : value) {
                        if (doc != null) {
                            UserProfile userProfileItem = doc.toObject(UserProfile.class);
                            userProfileItem.setID(doc.getId());
                            useProfileList.add(userProfileItem);
                        }
                    }
                    this.userProfileMutableLiveData.postValue(useProfileList.get(0));
                });
        return this.userProfileMutableLiveData;
    }

    //UPDATE
    public void update(UserProfile userProfile) {
        new Update(this.userProfilesCollectionRef).execute(userProfile);
    }

    //DELETE
    public void delete(UserProfile userProfile) {
        new Delete(this.userProfilesCollectionRef).execute(userProfile);
    }

}


