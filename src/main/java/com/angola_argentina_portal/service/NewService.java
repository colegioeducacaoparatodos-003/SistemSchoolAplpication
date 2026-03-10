package com.angola_argentina_portal.service;

import org.slf4j.Logger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.angola_argentina_portal.dto.NewsDTO;
import com.angola_argentina_portal.dto.NewsStatus;
import com.angola_argentina_portal.mapper.NewsMapper;
import com.angola_argentina_portal.model.News;
import com.angola_argentina_portal.repository.NewsRepository;

@Service
public class NewService {

@Autowired
    private NewsRepository newsRepository;

    // Retorna todas as notícias
    public List<NewsDTO> getAllNews() {

        List<News> news = newsRepository.findAll();
        
        return newsRepository.findAll()
                .stream()
                .map(NewsMapper::toDTO)
                .collect(Collectors.toList());
    }   
}
