package com.example.newsapp24;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp24.Models.News;
import com.example.newsapp24.Models.NewsResponse;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NewsAdapter adapter;
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("Loading News...");
        loadingDialog.show();

        RequestController controller = new RequestController(this);
        controller.getNews(listner, "general", null);
    }

    private final FetchData<NewsResponse> listner = new FetchData<NewsResponse>() {
        @Override
        public void fetchData(List<News> newsList, String message) {
            showNews(newsList);
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
        adapter = new NewsAdapter(this, newsList);
        recyclerView.setAdapter(adapter);
    }
}