package com.example.newsapp24;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp24.Models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private Context context;
    private List<News> newsList;

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.title.setText(newsList.get(position).getTitle());
        holder.source.setText(newsList.get(position).getSource().getName());

        if( newsList.get(position).getUrlToImage() != null)
        {
            Picasso.get()
                    .load(newsList.get(position).getUrlToImage())
                    .into(holder.img_news);
        }

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
