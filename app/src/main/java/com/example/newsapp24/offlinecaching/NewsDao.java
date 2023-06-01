package com.example.newsapp24.offlinecaching;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.newsapp24.Models.News;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("select * from news_cache")
    List<NewsCache> getCNews();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNews(List<NewsCache> newsCacheList);

    @Query("delete from news_cache")
    void deleteNews();


}
