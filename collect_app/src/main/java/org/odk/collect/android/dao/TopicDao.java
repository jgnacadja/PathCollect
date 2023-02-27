package org.odk.collect.android.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.odk.collect.android.adapters.model.Topic;

import java.util.ArrayList;

public class TopicDao {
    private final DatabaseReference topicsRef;

    public TopicDao() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        topicsRef = database.getReference("topics");
    }

    public void addTopic(Topic topic) {
        // Generate a new key for the topic
        String topicId = topicsRef.push().getKey();
        // Set the topic ID as the key
        topic.setId(topicId);
        // Add the topic to the database
        topicsRef.child(topicId).setValue(topic);
    }

    public void getAll(ValueEventListener listener){
        // Set up the Firebase query
        Query query = topicsRef.orderByChild("timestamp");

        // Add a listener to update the adapter when the data changes
        query.addValueEventListener(listener);
    }

    // Other methods for updating and deleting topics as needed...
}
