package org.odk.collect.android.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.odk.collect.android.adapters.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    private final DatabaseReference commentsRef;
    private final DatabaseReference discussionsRef;

    public CommentDao() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        commentsRef = database.getReference("comments");
        discussionsRef = database.getReference("discussions");
    }

    public void getCommentsByDiscussionId(String discussionId, ValueEventListener listener) {
        // Query the comments that match the topicId
        Query query = commentsRef.orderByChild("discussionId").equalTo(discussionId);
        // Attach the listener to the query results
        query.addValueEventListener(listener);
    }

    public void addComment(Comment comment) {
        // Generate a new key for the comment
        String commentId = commentsRef.push().getKey();
        // Set the comment ID as the key
        comment.setId(commentId);
        comment.setTimestamp(System.currentTimeMillis());

        // Add the comment to the database
        commentsRef.child(commentId).setValue(comment);

        // Update the comment count and last comment date in the discussion dao
        String discussionId = comment.getDiscussionId();
        DatabaseReference discussionRef = discussionsRef.child(discussionId);
        discussionRef.child("commentCount").runTransaction(new Transaction.Handler() {
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

        // Set the last comment date to the current time
        discussionRef.child("lastCommentTimestamp").setValue(System.currentTimeMillis());
    }

    public void updateCommentLikesCount(Comment comment, String installID, boolean liked) {
        // Update the comment like count and last comment date in the discussion dao
        commentsRef.child(comment.getId()).child("likes").setValue(comment.getLikes());
        List<String> users;
        if(comment.getLikedUsers() != null){
            users = comment.getLikedUsers();
        } else {
            users = new ArrayList<>();
        }
        if(liked){
            users.add(installID);
        } else {
            users.remove(installID);
        }
        commentsRef.child(comment.getId()).child("likedUsers").setValue(users);
    }

    // Other methods for updating and deleting comments as needed...
}