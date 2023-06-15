package com.example.newsapp24.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsapp24.Interfaces.DetailsListner;
import com.example.newsapp24.Interfaces.FetchData;
import com.example.newsapp24.Models.News;
import com.example.newsapp24.Models.NewsResponse;
import com.example.newsapp24.Utils.NewsAdapter;
import com.example.newsapp24.R;
import com.example.newsapp24.Utils.RequestController;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DetailsListner {

    RecyclerView recyclerView;
    NewsAdapter adapter;
    ProgressDialog loadingDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String prefCountry = sharedPreferences.getString("country", "us");
        String prefCategory = sharedPreferences.getString("category", "general");

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("Loading News...");
        loadingDialog.show();

        RequestController controller = new RequestController(this);
        controller.getTopLines(listner, prefCountry, prefCategory);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    loadingDialog = new ProgressDialog(MainActivity.this);
                    loadingDialog.setTitle("Loading News...");
                    loadingDialog.show();
                    RequestController controller = new RequestController(MainActivity.this);
                    controller.getTopLines(listner,prefCountry, prefCategory);
                    swipeRefreshLayout.setRefreshing(false);
            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()){
                    case R.id.bottom_nav_home:
                        loadingDialog = new ProgressDialog(MainActivity.this);
                        loadingDialog.setTitle("Loading News...");
                        loadingDialog.show();
                        RequestController controller = new RequestController(MainActivity.this);
                        controller.getTopLines(listner, prefCountry, prefCategory);
                        return true;
                    case R.id.bottom_nav_search:
                        startActivity(new Intent(MainActivity.this, SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_save_for_later:
                        startActivity(new Intent(MainActivity.this, SaveForLaterActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_settings:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private final FetchData<NewsResponse> listner = new FetchData<NewsResponse>() {
        @Override
        public void fetchData(List<News> newsList, String message) {
            showNews(newsList);
            loadingDialog.dismiss();
        }
        @Override
        public void errorData(String message) {
            Toast.makeText(MainActivity.this, "An error occured!!", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
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
    protected void onDestroy() {
        super.onDestroy();
        loadingDialog.dismiss();
    }

    @Override
    public void onNewsClick(News news) {
        startActivity(new Intent(MainActivity.this, DetailsActivity.class).putExtra("news", news));
    }

    @Override
    public void onNewsLongPressed(News news){
    }
}