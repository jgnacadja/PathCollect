package org.odk.collect.android.dao;

import org.odk.collect.android.adapters.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiGatewayService {
    @GET("wp-apis")
    Call<List<Post>> getWpPosts();
}
