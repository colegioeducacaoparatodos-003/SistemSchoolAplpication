package com.angola_argentina_portal.mapper;

import com.angola_argentina_portal.dto.NewsDTO;
import com.angola_argentina_portal.model.News;

public class NewsMapper {

    public static NewsDTO toDTO(News news) {
        if (news == null) return null;

        return new NewsDTO(
                news.getId(),
                news.getTitle(),
                news.getSubtitle(),
                news.getSummary(),
                news.getContent(),
                news.getImageUrl(),
                news.getThumbnailUrl(),
                news.getAuthor(),
                news.getCategory(),
                news.getCreatedAt(),
                news.getUpdatedAt(),
                news.getPublishedAt(),
                news.getStatus(),
                news.getViews()
        );
    }

    public static News toEntity(NewsDTO dto) {
        if (dto == null) return null;

        News news = new News();
        news.setId(dto.getId());
        news.setTitle(dto.getTitle());
        news.setSubtitle(dto.getSubtitle());
        news.setSummary(dto.getSummary());
        news.setContent(dto.getContent());
        news.setImageUrl(dto.getImageUrl());
        news.setThumbnailUrl(dto.getThumbnailUrl());
        news.setAuthor(dto.getAuthor());
        news.setCategory(dto.getCategory());
        news.setCreatedAt(dto.getCreatedAt());
        news.setUpdatedAt(dto.getUpdatedAt());
        news.setPublishedAt(dto.getPublishedAt());
        news.setStatus(dto.getStatus());
        news.setViews(dto.getViews());

        return news;
    }
}
