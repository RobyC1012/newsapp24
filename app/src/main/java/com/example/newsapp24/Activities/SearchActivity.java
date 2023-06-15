package com.example.newsapp24.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.newsapp24.Interfaces.DetailsListner;
import com.example.newsapp24.Interfaces.FetchData;
import com.example.newsapp24.Models.News;
import com.example.newsapp24.Models.NewsResponse;
import com.example.newsapp24.R;
import com.example.newsapp24.Utils.NewsAdapter;
import com.example.newsapp24.Utils.RequestController;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements DetailsListner{

    BottomNavigationView bottomNavigationView;
    SearchView searchView;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    ProgressDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_search);
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recycler_view);


        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("Loading News...");
        loadingDialog.show();

        RequestController controller = new RequestController(this);
        controller.getNews(listner, "en", "a");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { // when user press search button
                loadingDialog.setTitle("Searching News of " + query);
                loadingDialog.show();
                RequestController controller = new RequestController(SearchActivity.this);
                controller.getNews(listner, "en", (String) query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) { // when user type something
                return false;
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_nav_home:
                        startActivity(new Intent(SearchActivity.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_search:
                        return true;
                    case R.id.bottom_nav_save_for_later:
                        startActivity(new Intent(SearchActivity.this, SaveForLaterActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_settings:
                        startActivity(new Intent(SearchActivity.this, SettingsActivity.class));
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
            if(newsList.isEmpty()){
                Toast.makeText(SearchActivity.this, "No news found!!", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            } else {
                showNews(newsList);
                loadingDialog.dismiss();
            }
        }
        @Override
        public void errorData(String message) {
            Toast.makeText(SearchActivity.this, "An error occured!!", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
        }
    };
    private void showNews(List<News> newsList) {
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new NewsAdapter(this, newsList, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onNewsClick(News news) {
        startActivity(new Intent(SearchActivity.this, DetailsActivity.class).putExtra("news", news));
    }
    @Override
    public void onNewsLongPressed(News news){

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SearchActivity.this, MainActivity.class));
        finish();
    }
}