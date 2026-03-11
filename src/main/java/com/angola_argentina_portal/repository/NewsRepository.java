package com.angola_argentina_portal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.angola_argentina_portal.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query(value = """
        SELECT 
            n.id,
            n.title,
            n.subtitle,
            n.summary,
            n.author,
            n.category,
            n.status,
            n.views,
            n.published_at
        FROM news n
        ORDER BY n.created_at DESC
        """,
        countQuery = "SELECT COUNT(*) FROM news",
        nativeQuery = true)
    Page<Object[]> findAllForTable(Pageable pageable);


    @Query(value = """
        SELECT *
        FROM news
        WHERE status = 'PUBLISHED'
        ORDER BY published_at DESC
        """, nativeQuery = true)
    List<News> findPublishedNews();


    @Query(value = """
        SELECT 
            id,
            title,
            subtitle,
            summary,
            content,
            image_url,
            thumbnail_url,
            author,
            category,
            created_at,
            updated_at,
            published_at,
            status,
            views
        FROM news
        """, nativeQuery = true)
    List<Object[]> findNewsDTO();



}
