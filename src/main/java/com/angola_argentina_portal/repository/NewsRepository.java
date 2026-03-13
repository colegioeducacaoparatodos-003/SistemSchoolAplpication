package com.angola_argentina_portal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.angola_argentina_portal.dto.NewsTableDTO;
import com.angola_argentina_portal.interfaces.NewsTableProjetion;
import com.angola_argentina_portal.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {

        // -------------------------------
        // Método para Lazy Loading sem filtros
        // -------------------------------

        @Query(value = """
                        SELECT n.id AS id,
                               n.views AS views,
                               n.title AS title,
                               n.subtitle AS subtitle,
                               n.summary AS summary,
                               n.content AS content,
                               n.image_url AS imageUrl,
                               n.thumbnail_url AS thumbnailUrl,
                               n.author AS author,
                               n.category AS category,
                               n.created_at AS createdAt,
                               n.updated_at AS updatedAt,
                               n.published_at AS publishedAt,
                               n.status AS status
                        FROM news n
                        """, countQuery = "SELECT COUNT(*) FROM news", nativeQuery = true)
        Page<NewsTableProjetion> findAllForTable(Pageable pageable);

        // -------------------------------
        // Método para getAllNews do service
        // -------------------------------
        @Query(value = """
                        SELECT n.id, n.title, n.subtitle, n.summary, n.content, n.imageUrl, n.thumbnailUrl,
                               n.author, n.category, n.status, n.createdAt, n.updatedAt, n.views
                        FROM News n
                        """)
        List<Object[]> findNewsDTO();

}
