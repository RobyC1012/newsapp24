package com.example.newsapp24;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.newsapp24.Interfaces.FetchData;
import com.example.newsapp24.Models.News;
import com.example.newsapp24.Models.NewsResponse;
import com.example.newsapp24.OfflineCaching.NewsCache;
import com.example.newsapp24.OfflineCaching.NewsConverter;
import com.example.newsapp24.OfflineCaching.NewsDatabase;

import java.util.List;

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

    NewsDatabase newsDatabase;

    public RequestController(Context context) {
        this.context = context;
        this.newsDatabase = NewsDatabase.getInstance(context);
    }

    public void getNews(FetchData listner, String category, String query){
        if(isInternetConnected()) {
            CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
            Call<NewsResponse> call = callNewsApi.getNews("us", category, query, context.getString(R.string.api_key));

            try {
                call.enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                            listner.errorData("Error!");
                        }
                        List<News> newsList = response.body().getArticles();
                        if (newsList != null && !newsList.isEmpty()) {
                            new InsertAsyncTask(newsDatabase).execute(newsList);
                        }
                        listner.fetchData(response.body().getArticles(), response.body().getStatus());
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        listner.errorData(t.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "No Internet Connection! Getting tasks from database...", Toast.LENGTH_SHORT).show();
            new GetCacheData(newsDatabase, listner).execute();
        }
    }

    private boolean isInternetConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    private static class InsertAsyncTask extends AsyncTask<List<News>, Void, Void>{
        private NewsDatabase newsDatabase;

        public InsertAsyncTask(NewsDatabase newsDatabase) {
            this.newsDatabase = newsDatabase;
        }

        @Override
        protected Void doInBackground(List<News>... lists) {
            newsDatabase.newsDao().deleteNews();
            newsDatabase.newsDao().insertNews((List<NewsCache>) NewsConverter.newsToCache(lists[0]));
            return null;
        }
    }

    private static class GetCacheData extends AsyncTask<Void, Void, List<News>> {
        private NewsDatabase newsDatabase;
        private FetchData listener;

        public GetCacheData(NewsDatabase newsDatabase, FetchData listener) {
            this.newsDatabase = newsDatabase;
            this.listener = listener;
        }

        @Override
        protected List<News> doInBackground(Void... voids) {
            List<NewsCache> cachedNews = newsDatabase.newsDao().getCNews();
            /*List<News> newsList = new ArrayList<>();

            for (NewsCache newsCache : cachedNews) {
                News news = new News();
                news.setAuthor(newsCache.getAuthor());
                news.setContent(newsCache.getContent());
                news.setDescription(newsCache.getDescription());
                news.setPublishedAt(newsCache.getPublishedAt());
                news.setSource(new Source(newsCache.getSourceId(), newsCache.getSourceName()));
                news.setTitle(newsCache.getTitle());
                news.setUrl(newsCache.getUrl());
                news.setUrlToImage(newsCache.getUrlToImage());
                newsList.add(news);
            }*/
            List<News> newsList = (List<News>) NewsConverter.cacheToNews(cachedNews);

            return newsList;
        }

        @Override
        protected void onPostExecute(List<News> newsList) {
            listener.fetchData(newsList, "ok");
        }
    }

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
