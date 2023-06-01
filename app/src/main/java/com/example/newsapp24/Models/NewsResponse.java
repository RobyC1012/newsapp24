package com.example.newsapp24.Models;

import java.io.Serializable;
import java.util.List;

public class NewsResponse implements Serializable {
    String status = "";
    int totalResults = 0;
    List<News> articles = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<News> getArticles() {
        return articles;
    }

    public void setArticles(List<News> articles) {
        this.articles = articles;
    }
}
