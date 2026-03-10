package com.angola_argentina_portal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.angola_argentina_portal.model.News;
import com.angola_argentina_portal.repository.NewsRepository;

@Service
public class NewsService {

    private final NewsRepository repository;

    public NewsService(NewsRepository repository) {
        this.repository = repository;
    }

    /**
     * Buscar todas as notícias publicadas
     */
    public List<News> getPublishedNews() {
        return repository.findPublishedNews();
    }

    /**
     * Buscar notícias por categoria
     */
    public List<News> getNewsByCategory(String category) {
        return repository.findByCategory(category);
    }

    /**
     * Buscar notícia por ID
     */
    public Optional<News> getNewsById(Long id) {
        return repository.findNewsById(id);
    }

    /**
     * Buscar últimas notícias
     */
    public List<News> getLatestNews() {
        return repository.findLatestNews();
    }

    
}
