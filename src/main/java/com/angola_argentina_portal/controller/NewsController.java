package com.angola_argentina_portal.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.angola_argentina_portal.model.News;
import com.angola_argentina_portal.service.NewsService;

@Controller
public class NewsController {

    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping
    public List<News> getAllPublishedNews() {
        return service.getPublishedNews();
    }

    @GetMapping("/category/{category}")
    public List<News> getNewsByCategory(@PathVariable String category) {
        return service.getNewsByCategory(category);
    }

    @GetMapping("/{id}")
    public News getNewsById(@PathVariable Long id) {
        return service.getNewsById(id).orElseThrow();
    }

    @GetMapping("/latest")
    public List<News> getLatestNews() {
        return service.getLatestNews();
    }

}
