package com.example.newsapp24;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    TextView title, source;
    ImageView img_news;
    CardView cardView;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        source = itemView.findViewById(R.id.source);
        img_news = itemView.findViewById(R.id.img_news);
        cardView = itemView.findViewById(R.id.main_container);
    }

}
