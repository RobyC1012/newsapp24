package com.example.newsapp24.OfflineCaching;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.newsapp24.Interfaces.NewsDao;
import com.example.newsapp24.Models.NewsCache;
import com.example.newsapp24.Models.SavedForLaterNews;

@Database(entities = {NewsCache.class, SavedForLaterNews.class}, version = 2, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {

    private static NewsDatabase instance;

    public abstract NewsDao newsDao();

    public static synchronized NewsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NewsDatabase.class, "news_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }



}

