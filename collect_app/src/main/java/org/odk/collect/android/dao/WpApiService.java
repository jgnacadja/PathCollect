package org.odk.collect.android.dao;

import org.odk.collect.android.adapters.model.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WpApiService {
    @GET("posts")
    Call<List<Article>> getPosts(@Query("status") String status);
}

