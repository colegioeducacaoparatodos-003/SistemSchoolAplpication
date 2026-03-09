package com.angola_argentina_portal.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.angola_argentina_portal.model.News;
import com.angola_argentina_portal.service.NewsService;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    public String save(@RequestBody News news) {
        try {
            newsService.save(news);
            return "News saved successfully!";
        } catch (Exception e) {
            return "Error saving news: " + e.getMessage();
        }
    }

    public String update(@RequestBody News news) {
        try {
            newsService.update(news, news.getId());
            return "News updated successfully!";
        } catch (Exception e) {
            return "Error updating news: " + e.getMessage();
        }
    }

    public String delete(Long id) {
        try {
            newsService.delete(id);
            return "News deleted successfully!";
        } catch (Exception e) {
            return "Error deleting news: " + e.getMessage();
        }
    }

    public ResponseEntity<News> findById(Long id) {
        try {
            News news = newsService.getById(id);
            if (news != null) {
                return ResponseEntity.ok(news);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    public ResponseEntity<Iterable<News>> findAll() {
        try {
            Iterable<News> newsList = newsService.getAll();
            return ResponseEntity.ok(newsList);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


}
