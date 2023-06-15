package com.example.newsapp24.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
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

public class SaveForLaterActivity extends AppCompatActivity implements DetailsListner {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    ProgressDialog loadingDialog;
    RequestController controller;
    Button btn_delete_all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_for_later);

        recyclerView = findViewById(R.id.recycler_saved);
        btn_delete_all = findViewById(R.id.delete_button);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("Loading Saved News...");
        loadingDialog.show();
        controller = new RequestController(this);
        controller.getSavedNews(listner);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_save_for_later);

        btn_delete_all.setOnClickListener(view -> {
            controller.deleteAllSavedNews();
            Toast.makeText(this, "All saved news deleted!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SaveForLaterActivity.this, SaveForLaterActivity.class));
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_nav_home:
                        startActivity(new Intent(SaveForLaterActivity.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_search:
                        startActivity(new Intent(SaveForLaterActivity.this, SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.bottom_nav_save_for_later:
                        return true;
                    case R.id.bottom_nav_settings:
                        startActivity(new Intent(SaveForLaterActivity.this, SettingsActivity.class));
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
            if(newsList == null){
                Toast.makeText(SaveForLaterActivity.this, "No saved news found!!", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                return;
            }else {
            showNews(newsList);
            loadingDialog.dismiss();}
        }
        @Override
        public void errorData(String message) {
            Toast.makeText(SaveForLaterActivity.this, "An error occured!!", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
        }
    };

    @Override
    public void onNewsClick(News news) {
        Intent intent = new Intent(SaveForLaterActivity.this, DetailsActivity.class);
        intent.putExtra("news", news);
        startActivity(intent);
    }
    @Override
    public void onNewsLongPressed(News news){
    //create a dialog to confirm the deletion of the news
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirm delete");
        alert.setMessage("Are you sure you want to delete this news?");
        alert.setPositiveButton("Yes", (dialogInterface, i) -> {
            Toast.makeText(this, "News deleted!!", Toast.LENGTH_SHORT).show();
            controller.deleteSavedNews(news);
            startActivity(new Intent(SaveForLaterActivity.this, SaveForLaterActivity.class));
        });
        alert.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        alert.show();
    }



    private void showNews(List<News> newsList) {
        recyclerView = findViewById(R.id.recycler_saved);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new NewsAdapter(this, newsList, this);
        recyclerView.setAdapter(adapter);
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SaveForLaterActivity.this, MainActivity.class));
        finish();
    }
}