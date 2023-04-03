package org.odk.collect.android.dao;

import org.odk.collect.android.adapters.model.Article;
import org.odk.collect.android.adapters.model.Hospital;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiGatewayService {
    @POST("hospitals/search")
    Call<List<Hospital>> searchHospitals(@Body RequestBody request);

    @GET("wp-apis")
    Call<List<Article>> getWpPosts();
}
