package com.example.newsapp24.Interfaces;

import com.example.newsapp24.Models.News;

import java.util.List;

public interface FetchData<NewsResponse>{
    void fetchData(List<News> newsList, String message);
    void errorData(String message);
}
