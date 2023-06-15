package com.example.newsapp24.Interfaces;

import com.example.newsapp24.Models.News;

public interface DetailsListner {
    void onNewsClick(News news);
    void onNewsLongPressed(News news);
}
