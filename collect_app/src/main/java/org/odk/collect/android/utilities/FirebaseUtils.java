package org.odk.collect.android.utilities;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FirebaseUtils {
    private static final String TAG = FirebaseUtils.class.getSimpleName();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CollectionReference getCollection(String collectionName) {
        return db.collection(collectionName);
    }

    public DocumentReference getDocument(String collectionName, String documentId) {
        return db.collection(collectionName).document(documentId);
    }

    public Query getCollectionQuery(String collectionName, String field, Object value) {
        return db.collection(collectionName).whereEqualTo(field, value);
    }
}
