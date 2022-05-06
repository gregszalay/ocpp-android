package net.chargerevolutionapp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;

import net.chargerevolutionapp.chargers.Charger;
import net.chargerevolutionapp.chargers.ChargerRepository;

public abstract class AbstractRepository {
    private static final String LOG_TAG = ChargerRepository.class.getName();

    protected static class Insert extends AsyncTask<Charger, Void, Void> {
        private final CollectionReference collectionRef;

        Insert(CollectionReference collectionRef) {
            this.collectionRef = collectionRef;
        }

        @Override
        protected Void doInBackground(Charger... chargers) {
            this.collectionRef
                    .document()
                    .set(chargers[0])
                    .addOnSuccessListener(aVoid -> {
                        Log.d(LOG_TAG, "New charger saved!");
                    })
                    .addOnFailureListener(e -> Log.w(LOG_TAG, "Error writing document", e));
            return null;
        }
    }

    protected static class Update extends AsyncTask<Charger, Void, Void> {
        private final CollectionReference collectionRef;

        Update(CollectionReference collectionRef) {
            this.collectionRef = collectionRef;
        }

        @Override
        protected Void doInBackground(Charger... chargers) {
            this.collectionRef
                    .document(chargers[0].getName())
                    .set(chargers[0])
                    .addOnSuccessListener(aVoid -> {
                        Log.d(LOG_TAG, "Charger updated!");
                    })
                    .addOnFailureListener(e -> Log.w(LOG_TAG, "Error updating document", e));
            return null;
        }
    }

    protected static class Delete extends AsyncTask<Charger, Void, Void> {
        private final CollectionReference collectionRef;

        Delete(CollectionReference collectionRef) {
            this.collectionRef = collectionRef;
        }

        @Override
        protected Void doInBackground(Charger... chargers) {
            this.collectionRef
                    .document(chargers[0].getName())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(LOG_TAG, "Charger deleted!");
                    })
                    .addOnFailureListener(e -> Log.w(LOG_TAG, "Error deleting document", e));
            return null;
        }
    }

}
