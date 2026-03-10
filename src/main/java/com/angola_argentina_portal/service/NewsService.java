package com.angola_argentina_portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.angola_argentina_portal.model.News;
import com.angola_argentina_portal.repository.NewsRepository;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public String save(News news) {
        newsRepository.save(news);
        return "News saved successfully";
    }

    public String update(News news, Long id) {
        newsRepository.save(news);
        return "News updated successfully";
    }

    public String delete(Long id) {
        newsRepository.deleteById(id);
        return "News deleted successfully";
    }

    public List<News> getAll() {
        return newsRepository.findAll();
    }

    public News getById(Long id) {
        return newsRepository.findById(id).orElse(null);
    }

    
}
