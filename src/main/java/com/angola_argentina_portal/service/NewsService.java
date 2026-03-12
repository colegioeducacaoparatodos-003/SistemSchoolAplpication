package com.angola_argentina_portal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.angola_argentina_portal.dto.CreateNewsDTO;
import com.angola_argentina_portal.dto.ResponseNewsDTO;
import com.angola_argentina_portal.dto.UpdateNewsDTO;
import com.angola_argentina_portal.mapper.NewsMapper;
import com.angola_argentina_portal.model.News;
import com.angola_argentina_portal.repository.NewsRepository;

@Service
public class NewsService {


    private NewsRepository repository;

    public NewsService(NewsRepository newsRepository){
        this.repository = newsRepository;
    }


    public void save(CreateNewsDTO dto){

        News news = NewsMapper.toEntity(dto);

        repository.save(news);
    }


    public void update(UpdateNewsDTO dto){

        News news = repository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("News not found"));

        news.setTitle(dto.getTitle());
        news.setSubtitle(dto.getSubtitle());
        news.setSummary(dto.getSummary());
        news.setContent(dto.getContent());
        news.setImageUrl(dto.getImageUrl());
        news.setThumbnailUrl(dto.getThumbnailUrl());
        news.setAuthor(dto.getAuthor());
        news.setCategory(dto.getCategory());
        news.setStatus(dto.getStatus());
        news.setUpdatedAt(LocalDateTime.now());

        repository.save(news);
    }


    public void delete(Long id){
        repository.deleteById(id);
    }


   public List<ResponseNewsDTO> getAllNews(){

        List<Object[]> results = repository.findNewsDTO();

        return results.stream()
                .map(obj -> {

                    ResponseNewsDTO dto = new ResponseNewsDTO();

                    dto.setId(((Number)obj[0]).longValue());
                    dto.setTitle((String) obj[1]);
                    dto.setSubtitle((String) obj[2]);
                    dto.setSummary((String) obj[3]);
                    dto.setAuthor((String) obj[7]);
                    dto.setCategory((String) obj[8]);
                    dto.setViews(((Number)obj[13]).longValue());

                    return dto;

                }).toList();
    }

}
