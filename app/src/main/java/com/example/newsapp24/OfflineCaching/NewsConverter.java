package com.example.newsapp24.offlinecaching;

import com.example.newsapp24.Models.News;

import java.util.ArrayList;
import java.util.List;

public class NewsConverter {
    public static List<NewsCache> convert(List<News> newsList) {
        List<NewsCache> newsCacheList = new ArrayList<>();
        for (News news : newsList) {
            NewsCache newsCache = new NewsCache(
                    news.getSource().getId(),
                    news.getSource().getName(),
                    news.getAuthor(),
                    news.getTitle(),
                    news.getDescription(),
                    news.getUrl(),
                    news.getUrlToImage(),
                    news.getPublishedAt(),
                    news.getContent()
            );
            newsCacheList.add(newsCache);
        }

        return newsCacheList;
    }

}
