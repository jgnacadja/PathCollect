package org.odk.collect.android.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.odk.collect.android.adapters.model.Discussion;

import java.util.ArrayList;
import java.util.List;

public class DiscussionDao {
    private final DatabaseReference discussionsRef;
    private final DatabaseReference topicsRef;

    public DiscussionDao() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        discussionsRef = database.getReference("discussions");
        topicsRef = database.getReference("topics");
    }

    public void getDiscussionsByTopicId(String topicId, ValueEventListener listener) {
        // Query the discussions that match the topicId
        Query query = discussionsRef.orderByChild("topicId").equalTo(topicId);
        // Attach the listener to the query results
        query.addValueEventListener(listener);
    }

    public void get(String id, ValueEventListener listener) {
        // Query the discussions that match the topicId
        Query query = discussionsRef.child(id);
        // Attach the listener to the query results
        query.addValueEventListener(listener);
    }

    public void addDiscussion(Discussion discussion) {
        // Generate a new key for the discussion
        String discussionId = discussionsRef.push().getKey();
        // Set the discussion ID as the key
        discussion.setId(discussionId);
        discussion.setTimestamp(System.currentTimeMillis());

        // Add the discussion to the database
        discussionsRef.child(discussionId).setValue(discussion);

        // Update the discussion count in the topic model
        topicsRef.child(discussion.getTopicId()).child("discussionCount").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer count = mutableData.getValue(Integer.class);
                if (count == null) {
                    count = 0;
                }
                mutableData.setValue(count + 1);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Handle the completion of the transaction
            }
        });
    }

    public void updateDiscussionLikesCount(Discussion discussion, String installID, boolean liked) {
        // Update the comment like count and last comment date in the discussion dao
        discussionsRef.child(discussion.getId()).child("likes").setValue(discussion.getLikes());

        List<String> users;
        if(discussion.getLikedUsers() != null){
            users = discussion.getLikedUsers();
        } else {
            users = new ArrayList<>();
        }
        if(liked){
            users.add(installID);
        } else {
            users.remove(installID);
        }
        discussionsRef.child(discussion.getId()).child("likedUsers").setValue(users);
    }

    public void updateDiscussionViewsCount(Discussion discussion, String installID) {
        // Update the comment like count and last comment date in the discussion dao
        discussionsRef.child(discussion.getId()).child("views").setValue(discussion.getViews());

        List<String> users;
        if(discussion.getViewedUsers() != null){
            users = discussion.getViewedUsers();
        } else {
            users = new ArrayList<>();
        }
        users.add(installID);
        discussionsRef.child(discussion.getId()).child("viewedUsers").setValue(users);
    }

    // Other methods for updating and deleting discussions as needed...
}
