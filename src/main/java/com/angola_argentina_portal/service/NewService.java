package com.angola_argentina_portal.service;

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
        return newsRepository.findAll()
                .stream()
                .map(NewsMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Notícias por status
    public List<NewsDTO> getNewsByStatus(NewsStatus status) {
        return newsRepository.findAllByStatus(status)
                .stream()
                .map(NewsMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Pesquisar notícias pelo título
    public List<NewsDTO> searchNewsByTitle(String keyword) {
        return newsRepository.searchByTitle(keyword)
                .stream()
                .map(NewsMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Top notícias por visualizações
    public List<NewsDTO> getTopNewsByViews() {
        return newsRepository.findTopByViews()
                .stream()
                .map(NewsMapper::toDTO)
                .collect(Collectors.toList());
    }
    
}
