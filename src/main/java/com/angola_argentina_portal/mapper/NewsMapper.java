package com.angola_argentina_portal.mapper;

import java.time.LocalDateTime;

import com.angola_argentina_portal.dto.CreateNewsDTO;
import com.angola_argentina_portal.model.News;

public class NewsMapper {

        public static News toEntity(CreateNewsDTO dto){

        News news = new News();

        news.setTitle(dto.getTitle());
        news.setSubtitle(dto.getSubtitle());
        news.setSummary(dto.getSummary());
        news.setContent(dto.getContent());
        news.setImageUrl(dto.getImageUrl());
        news.setThumbnailUrl(dto.getThumbnailUrl());
        news.setAuthor(dto.getAuthor());
        news.setCategory(dto.getCategory());
        news.setStatus(dto.getStatus());
        news.setCreatedAt(LocalDateTime.now());
        news.setViews(0L);

        return news;
    }
}
