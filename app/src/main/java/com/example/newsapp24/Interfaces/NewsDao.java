package com.example.newsapp24.Interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.newsapp24.Models.NewsCache;
import com.example.newsapp24.Models.SavedForLaterNews;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("select * from news_cache")
    List<NewsCache> getCNews();

    @Query("select * from saved_for_later_news")
    List<SavedForLaterNews> getSavedNews();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNews(List<NewsCache> newsCacheList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSavedNews(SavedForLaterNews savedForLaterNews);


    @Query("delete from saved_for_later_news where title = :title")
    void deleteSavedNews(String title);

    @Query("delete from saved_for_later_news")
    void deleteAllSavedNews();

    @Query("delete from news_cache")
    void deleteNews();


}
