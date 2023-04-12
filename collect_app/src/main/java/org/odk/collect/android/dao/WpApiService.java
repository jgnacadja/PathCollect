package org.odk.collect.android.dao;

import org.odk.collect.android.adapters.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WpApiService {
    @GET("posts")
    Call<List<Post>> getPosts(@Query("status") String status);
}

