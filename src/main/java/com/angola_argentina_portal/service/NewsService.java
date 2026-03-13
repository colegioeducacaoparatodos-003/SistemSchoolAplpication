package com.angola_argentina_portal.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.angola_argentina_portal.dto.CreateNewsDTO;
import com.angola_argentina_portal.dto.NewsTableDTO;
import com.angola_argentina_portal.dto.ResponseNewsDTO;
import com.angola_argentina_portal.dto.UpdateNewsDTO;
import com.angola_argentina_portal.interfaces.NewsTableProjetion;
import com.angola_argentina_portal.io.Assistant;
import com.angola_argentina_portal.io.FileImage;
import com.angola_argentina_portal.mapper.NewsMapper;
import com.angola_argentina_portal.model.News;
import com.angola_argentina_portal.repository.NewsRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NewsService {

    private final NewsRepository repository;

    public NewsService(NewsRepository newsRepository) {
        this.repository = newsRepository;
    }

    public News save(News news) throws IOException {

        FileImage acessImage = new FileImage();
        Assistant assistant = new Assistant();
        String newNameFile = "default.png"; // Default image nam

        if (news.getImageUrlUtil() != null) {
            newNameFile = "0" + assistant.novoNome(news.getImageUrlUtil().getContentType());
            acessImage.salvarArquivo(news.getImageUrlUtil(), "news_images", newNameFile);
        } else {
            news.setImageUrlUtil(null);
        }

        news.setImageUrl(newNameFile);
        return repository.save(news);
    }

    // Atualizar notícia
    public void update(UpdateNewsDTO dto) {
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

    // Deletar notícia
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // Buscar todas as notícias simplificadas
    public List<ResponseNewsDTO> getAllNews() {
        List<Object[]> results = repository.findNewsDTO();

        return results.stream()
                .map(obj -> {
                    ResponseNewsDTO dto = new ResponseNewsDTO();
                    dto.setId(((Number) obj[0]).longValue());
                    dto.setTitle((String) obj[1]);
                    dto.setSubtitle((String) obj[2]);
                    dto.setSummary((String) obj[3]);
                    dto.setAuthor((String) obj[7]);
                    dto.setCategory((String) obj[8]);
                    dto.setViews(((Number) obj[13]).longValue());
                    return dto;
                }).toList();
    }

    // ---------------------
    // MÉTODOS PARA LAZY LOADING
    // ---------------------

    public Page<NewsTableDTO> findLazy(int page, int size, Sort sort, Map<String, Object> filters) {

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<NewsTableProjetion> projections = repository.findAllForTable(pageable);

        return projections.map(p -> new NewsTableDTO(
                p.getId(),
                p.getViews(),
                p.getTitle(),
                p.getSubtitle(),
                p.getSummary(),
                p.getAuthor(),
                p.getCategory(),
                p.getCreatedAt(),
                p.getPublishedAt(),
                p.getStatus()));
    }
}