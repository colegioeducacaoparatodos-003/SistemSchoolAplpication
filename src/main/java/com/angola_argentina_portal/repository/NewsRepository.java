package com.angola_argentina_portal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.angola_argentina_portal.model.News;

import jakarta.transaction.Transactional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

        @Query(value = """
                        SELECT *
                        FROM news
                        WHERE status = 'PUBLISHED'
                        ORDER BY published_at DESC
                        """, nativeQuery = true)
        List<News> findPublishedNews();

        @Query(value = """
                        SELECT *
                        FROM news
                        WHERE category = :category
                        AND status = 'PUBLISHED'
                        ORDER BY published_at DESC
                        """, nativeQuery = true)
        List<News> findByCategory(@Param("category") String category);

        @Query(value = """
                        SELECT *
                        FROM news
                        WHERE id = :id
                        """, nativeQuery = true)
        Optional<News> findNewsById(@Param("id") Long id);

        @Query(value = """
                        SELECT *
                        FROM news
                        WHERE status = 'PUBLISHED'
                        ORDER BY published_at DESC
                        LIMIT 5
                        """, nativeQuery = true)
        List<News> findLatestNews();

        @Modifying
        @Transactional
        @Query(value = """
                        UPDATE news
                        SET views = views + 1
                        WHERE id = :id
                        """, nativeQuery = true)
        void incrementViews(@Param("id") Long id);
}
