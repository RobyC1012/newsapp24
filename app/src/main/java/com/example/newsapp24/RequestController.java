package com.example.newsapp24;

import android.content.Context;
import android.widget.Toast;

import com.example.newsapp24.Models.NewsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestController {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getNews(FetchData listner, String category, String query){
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
        Call<NewsResponse> call = callNewsApi.getNews("us", category, query, context.getString(R.string.api_key));

        try {
            call.enqueue(new Callback<NewsResponse>() {
                @Override
                public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                    listner.fetchData(response.body().getArticles(), response.body().getStatus());
                }

                @Override
                public void onFailure(Call<NewsResponse> call, Throwable t) {
                    listner.errorData(t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public RequestController(Context context) { this.context = context; }

    public interface CallNewsApi {
        @GET("top-headlines")
        Call<NewsResponse> getNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("q") String q,
            @Query("apiKey") String api_key
        );
    }
}
