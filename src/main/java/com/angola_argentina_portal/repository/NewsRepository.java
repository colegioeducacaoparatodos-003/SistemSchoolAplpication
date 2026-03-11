package com.angola_argentina_portal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.angola_argentina_portal.dto.NewsStatus;
import com.angola_argentina_portal.model.News;

import jakarta.transaction.Transactional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /*
     * Buscar todas as notícias de um determinado status
     * 
     * @Query(value = "SELECT * FROM news", nativeQuery = true)
     * List<News> findAll();
     * 
     * // Buscar notícias por autor
     * 
     * @Query("SELECT n FROM News n WHERE n.author = :author ORDER BY n.createdAt DESC"
     * )
     * List<News> findAllByAuthor(@Param("author") String author);
     * 
     * // Buscar notícias por categoria
     * 
     * @Query("SELECT n FROM News n WHERE n.category = :category ORDER BY n.createdAt DESC"
     * )
     * List<News> findAllByCategory(@Param("category") String category);
     * 
     * // Buscar notícias com título contendo uma palavra (LIKE)
     * 
     * @Query("SELECT n FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY n.createdAt DESC"
     * )
     * List<News> searchByTitle(@Param("keyword") String keyword);
     * 
     * // Buscar as notícias mais vistas
     * 
     * @Query("SELECT n FROM News n ORDER BY n.views DESC")
     * List<News> findTopByViews();
     * 
     * // Custom query nativa (SQL) caso queira usar SQL direto
     * 
     * @Query(value =
     * "SELECT * FROM news n WHERE n.status = :status ORDER BY n.published_at DESC",
     * nativeQuery = true)
     * List<News> findAllByStatusNative(@Param("status") String status);
     */
}
