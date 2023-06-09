package com.example.newsapp24;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapp24.Models.News;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    News news;
    TextView title, description, content, author, publishedAt;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        title = findViewById(R.id.details_title);
        description = findViewById(R.id.details_description);
        content = findViewById(R.id.details_content);
        author = findViewById(R.id.details_author);
        publishedAt = findViewById(R.id.details_date);
        imageView = findViewById(R.id.details_image);

        news = (News) getIntent().getSerializableExtra("news");

        title.setText(news.getTitle());
        description.setText(news.getDescription());
        content.setText(news.getContent());
        author.setText(news.getAuthor());
        publishedAt.setText(news.getPublishedAt());
        Picasso.get().load(news.getUrlToImage()).into(imageView);

    }
}