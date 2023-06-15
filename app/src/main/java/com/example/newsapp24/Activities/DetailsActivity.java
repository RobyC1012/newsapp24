package com.example.newsapp24.Activities;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp24.Interfaces.FetchData;
import com.example.newsapp24.Models.News;
import com.example.newsapp24.Models.NewsResponse;
import com.example.newsapp24.OfflineCaching.NewsConverter;
import com.example.newsapp24.OfflineCaching.NewsDatabase;
import com.example.newsapp24.R;
import com.example.newsapp24.Utils.RequestController;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    News news;
    TextView title, description, content, author, publishedAt;
    ImageView imageView;
    Button saveButton;
    NewsDatabase newsDatabase;
    RequestController controller;
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        newsDatabase = NewsDatabase.getInstance(this);
        controller = new RequestController(this);

        loadingDialog  = new ProgressDialog(this);

        title = findViewById(R.id.details_title);
        description = findViewById(R.id.details_description);
        content = findViewById(R.id.details_content);
        author = findViewById(R.id.details_author);
        publishedAt = findViewById(R.id.details_date);
        imageView = findViewById(R.id.details_image);
        saveButton = findViewById(R.id.save_for_later_button);

        news = (News) getIntent().getSerializableExtra("news");

        title.setText(news.getTitle());
        description.setText(news.getDescription());
        content.setText(news.getContent());
        author.setText(news.getAuthor());
        publishedAt.setText(news.getPublishedAt());
        Picasso.get().load(news.getUrlToImage()).into(imageView);

        saveButton.setOnClickListener(v -> {
            loadingDialog.setTitle("Saving article...");
            controller.getSavedNews(listner);
        });

    }

    private final FetchData<NewsResponse> listner = new FetchData<NewsResponse>() {
        @Override
        public void fetchData(List<News> newsList, String message) {

            if(newsList == null){
                new InsertAsyncTask(newsDatabase).execute((News) getIntent().getSerializableExtra("news"));
                return;
            }else {
                News article = (News) getIntent().getSerializableExtra("news");
                for (News news : newsList) {
                    if (news.getTitle().equals(article.getTitle())) {
                        Toast.makeText(DetailsActivity.this, "Already saved", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        return;
                    }
                }
                new InsertAsyncTask(newsDatabase).execute((News) getIntent().getSerializableExtra("news"));
                Toast.makeText(DetailsActivity.this, "Saved for later", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        }
        @Override
        public void errorData(String message) {
            Toast.makeText(DetailsActivity.this, "An error occured!!", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
        }
    };


    private static class InsertAsyncTask extends AsyncTask<News, Void, Void>{
        private NewsDatabase newsDatabase;

        public InsertAsyncTask(NewsDatabase newsDatabase) {
            this.newsDatabase = newsDatabase;
        }

        @Override
        protected Void doInBackground(News... news) {

            newsDatabase.newsDao().insertSavedNews(NewsConverter.newsToSaved(news[0]));
            return null;
        }
    }


}