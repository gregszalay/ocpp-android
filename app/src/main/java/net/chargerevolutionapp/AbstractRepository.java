package net.chargerevolutionapp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import net.chargerevolutionapp.chargers.Charger;
import net.chargerevolutionapp.chargers.ChargerRepository;

public abstract class AbstractRepository {
    private static final String LOG_TAG = AbstractRepository.class.getName();
    protected FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public FirebaseUser getLoggedInFirebaseUser() {
        if(loggedInFirebaseUser == null) Log.i(LOG_TAG, "Unauthenticated user!");
        return loggedInFirebaseUser;
    }

    protected FirebaseUser loggedInFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public static class Insert extends AsyncTask<FirestoreModel, Void, Void> {
        private final CollectionReference collectionRef;

        public Insert(CollectionReference collectionRef) {
            this.collectionRef = collectionRef;
        }

        @Override
        protected Void doInBackground(FirestoreModel... firestoreModels) {
            this.collectionRef
                    .document()
                    .set(firestoreModels[0])
                    .addOnSuccessListener(aVoid -> {
                        Log.d(LOG_TAG, "New Document saved!");
                    })
                    .addOnFailureListener(e -> Log.w(LOG_TAG, "Error writing document", e));
            return null;
        }
    }

    public static class Update extends AsyncTask<FirestoreModel, Void, Void> {
        private final CollectionReference collectionRef;

        public Update(CollectionReference collectionRef) {
            this.collectionRef = collectionRef;
        }

        @Override
        protected Void doInBackground(FirestoreModel... firestoreModels) {
            if(firestoreModels[0]._getDocID() == null){
                Log.w(LOG_TAG, "Error updating document: could not find document ID");
                return null;
            }
            this.collectionRef
                    .document(firestoreModels[0]._getDocID())
                    .set(firestoreModels[0])
                    .addOnSuccessListener(aVoid -> {
                        Log.d(LOG_TAG, "Document updated!");
                    })
                    .addOnFailureListener(e -> Log.w(LOG_TAG, "Error updating document", e));
            return null;
        }
    }

    public static class Delete extends AsyncTask<FirestoreModel, Void, Void> {
        private final CollectionReference collectionRef;

        public Delete(CollectionReference collectionRef) {
            this.collectionRef = collectionRef;
        }

        @Override
        protected Void doInBackground(FirestoreModel... firestoreModels) {
            this.collectionRef
                    .document(firestoreModels[0]._getDocID())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(LOG_TAG, "Document deleted!");
                    })
                    .addOnFailureListener(e -> Log.w(LOG_TAG, "Error deleting document", e));
            return null;
        }
    }

}
