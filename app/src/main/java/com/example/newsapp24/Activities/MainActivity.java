package com.example.newsapp24;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsapp24.Models.News;
import com.example.newsapp24.Models.NewsResponse;
import com.example.newsapp24.offlinecaching.NewsCache;
import com.example.newsapp24.offlinecaching.NewsConverter;
import com.example.newsapp24.offlinecaching.NewsDao;
import com.example.newsapp24.offlinecaching.NewsDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DetailsListner{
    RecyclerView recyclerView;
    NewsAdapter adapter;
    ProgressDialog loadingDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("Loading News...");
        loadingDialog.show();

        RequestController controller = new RequestController(this);
        controller.getNews(listner, "general", null);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    loadingDialog = new ProgressDialog(MainActivity.this);
                    loadingDialog.setTitle("Loading News...");
                    loadingDialog.show();
                    RequestController controller = new RequestController(MainActivity.this);
                    controller.getNews(listner, "general", null);
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private final FetchData<NewsResponse> listner = new FetchData<NewsResponse>() {
        @Override
        public void fetchData(List<News> newsList, String message) {
            showNews(newsList);
            //newsDao.insertNews((List<NewsCache>) NewsConverter.convert(newsList));
            loadingDialog.dismiss();
        }

        @Override
        public void errorData(String message) {

        }
    };
    private void showNews(List<News> newsList) {
        recyclerView = findViewById(R.id.recycler_main);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new NewsAdapter(this, newsList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNewsClick(News news) {
        startActivity(new Intent(MainActivity.this, DetailsActivity.class).putExtra("news", news));
    }
}