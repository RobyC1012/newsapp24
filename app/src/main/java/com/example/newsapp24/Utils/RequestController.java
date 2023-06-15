package com.example.newsapp24.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.preference.PreferenceManager;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.newsapp24.Interfaces.FetchData;
import com.example.newsapp24.Models.News;
import com.example.newsapp24.Models.NewsResponse;
import com.example.newsapp24.Models.NewsCache;
import com.example.newsapp24.OfflineCaching.NewsConverter;
import com.example.newsapp24.OfflineCaching.NewsDatabase;
import com.example.newsapp24.Models.SavedForLaterNews;
import com.example.newsapp24.R;

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

    public void getNews(FetchData listner, String language, String query){

        if(isInternetConnected()) {
            CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
            Call<NewsResponse> call = callNewsApi.getNews(query, language,"publishedAt",context.getString(R.string.api_key));

            try {
                call.enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                            listner.errorData("Error!");
                        }
                        else {
                            List<News> newsList = response.body().getArticles();
                            /*if (newsList != null && !newsList.isEmpty()) {
                                new InsertAsyncTask(newsDatabase).execute(newsList);
                            }*/
                            listner.fetchData(response.body().getArticles(), response.body().getStatus());
                        }

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


    public void getTopLines(FetchData listner, String country ,String category){

        if(isInternetConnected()) {
            CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
            Call<NewsResponse> call = callNewsApi.getTopHeadlines(country,category,context.getString(R.string.api_key));

            try {
                call.enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                            listner.errorData("Error!");
                        }
                        else {
                            List<News> newsList = response.body().getArticles();
                            if (newsList != null && !newsList.isEmpty()) {
                                new InsertAsyncTask(newsDatabase).execute(newsList);
                            }
                            listner.fetchData(response.body().getArticles(), response.body().getStatus());
                        }

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

    public void getSavedNews(FetchData listner){
        new GetSavedNews(newsDatabase, listner).execute();
    }

    public void deleteAllSavedNews(){
        new DeleteAllSavedNews(newsDatabase).execute();
    }

    public void deleteSavedNews(News news){
        new DeleteSavedNews(newsDatabase, news).execute();
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
            newsDatabase.newsDao().insertNews( NewsConverter.newsToCache(lists[0]));
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
            List<News> newsList = NewsConverter.cacheToNews(cachedNews);

            return newsList;
        }

        @Override
        protected void onPostExecute(List<News> newsList) {
            listener.fetchData(newsList, "ok");
        }
    }
    private static class GetSavedNews extends AsyncTask<Void, Void, List<News>> {
        private NewsDatabase newsDatabase;
        private FetchData listener;

        public GetSavedNews(NewsDatabase newsDatabase, FetchData listener) {
            this.newsDatabase = newsDatabase;
            this.listener = listener;
        }

        @Override
        protected List<News> doInBackground(Void... voids) {
            List<SavedForLaterNews> cachedNews = newsDatabase.newsDao().getSavedNews();
            List<News> newsSList = NewsConverter.savedToNews(cachedNews);
            return newsSList;
        }

        @Override
        protected void onPostExecute(List<News> newsSList) {
            listener.fetchData(newsSList, "ok");
        }
    }

    private static class DeleteAllSavedNews extends AsyncTask<Void, Void, Void> {
        private NewsDatabase newsDatabase;

        public DeleteAllSavedNews(NewsDatabase newsDatabase) {
            this.newsDatabase = newsDatabase;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            newsDatabase.newsDao().deleteAllSavedNews();
            return null;
        }
    }

    public static class DeleteSavedNews extends AsyncTask<News, Void, Void>{
        private NewsDatabase newsDatabase;
        private News deletedNews;

        public DeleteSavedNews(NewsDatabase newsDatabase, News news) {
            this.newsDatabase = newsDatabase;
            this.deletedNews = news;
        }

        @Override
        protected Void doInBackground(News... news) {
            newsDatabase.newsDao().deleteSavedNews(deletedNews.getTitle());
            return null;
        }
    }

    public interface CallNewsApi {
        @GET("everything")
        Call<NewsResponse> getNews(
            //@Query("country") String country,
            //@Query("category") String category,
            @Query("qInTitle") String q,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String api_key
        );

        @GET("top-headlines")
        Call<NewsResponse> getTopHeadlines(
                @Query("country") String country,
                @Query("category") String category,
                @Query("apiKey") String api_key
        );
    }
}
