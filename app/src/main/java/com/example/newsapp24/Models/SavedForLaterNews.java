package com.example.newsapp24.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "saved_for_later_news")
public class SavedForLaterNews implements Serializable{
    @PrimaryKey(autoGenerate = true)
    Integer id;
    @ColumnInfo(name = "sourceId")
    String sourceId = "";
    @ColumnInfo(name = "sourceName")
    String sourceName = "";
    @ColumnInfo(name = "author")
    String author = "";
    @ColumnInfo(name = "title")
    String title = "";
    @ColumnInfo(name = "description")
    String description = "";
    @ColumnInfo(name = "url")
    String url = "";
    @ColumnInfo(name = "urlToImage")
    String urlToImage = "";
    @ColumnInfo(name = "publishedAt")
    String publishedAt = "";
    @ColumnInfo(name = "content")
    String content = "";

    public SavedForLaterNews(String sourceId, String sourceName, String author, String title, String description, String url, String urlToImage, String publishedAt, String content) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
