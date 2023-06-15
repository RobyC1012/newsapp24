package com.example.newsapp24.OfflineCaching;

import com.example.newsapp24.Models.News;
import com.example.newsapp24.Models.NewsCache;
import com.example.newsapp24.Models.SavedForLaterNews;
import com.example.newsapp24.Models.Source;

import java.util.ArrayList;
import java.util.List;

public class NewsConverter {
    public static List<NewsCache> newsToCache(List<News> newsList) {
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

    public static List<News> cacheToNews(List<NewsCache> newsList){
        List<News> newsCacheList = new ArrayList<>();
        for (NewsCache news : newsList) {
            News newsCache = new News();
            newsCache.setSource(new Source(news.getSourceId(), news.getSourceName()));
            newsCache.setAuthor(news.getAuthor());
            newsCache.setTitle(news.getTitle());
            newsCache.setDescription(news.getDescription());
            newsCache.setUrl(news.getUrl());
            newsCache.setUrlToImage(news.getUrlToImage());
            newsCache.setPublishedAt(news.getPublishedAt());
            newsCache.setContent(news.getContent());
            newsCacheList.add(newsCache);
        }

        return newsCacheList;
    }

    public static SavedForLaterNews newsToSaved ( News news){
        SavedForLaterNews savedForLaterNews = new SavedForLaterNews(
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
        return savedForLaterNews;
    }

    public static List<News> savedToNews ( List<SavedForLaterNews> savedForLaterNews){
           List<News> news = new ArrayList<>();
              for (SavedForLaterNews saved : savedForLaterNews){
                News news1 = new News();
                news1.setSource(new Source(saved.getSourceId(), saved.getSourceName()));
                news1.setAuthor(saved.getAuthor());
                news1.setTitle(saved.getTitle());
                news1.setDescription(saved.getDescription());
                news1.setUrl(saved.getUrl());
                news1.setUrlToImage(saved.getUrlToImage());
                news1.setPublishedAt(saved.getPublishedAt());
                news1.setContent(saved.getContent());
                news.add(news1);
              }
        return news;
    }

}
