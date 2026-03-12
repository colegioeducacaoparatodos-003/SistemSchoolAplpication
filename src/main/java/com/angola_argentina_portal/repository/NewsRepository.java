package com.angola_argentina_portal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



import com.angola_argentina_portal.dto.NewsTableDTO;
import com.angola_argentina_portal.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {

    // -------------------------------
    // Método para Lazy Loading sem filtros
    // -------------------------------
    @Query("SELECT new com.angola_argentina_portal.dto.NewsTableDTO("
            + "n.id, n.title, n.subtitle, n.author, n.category, n.status, n.createdAt) "
            + "FROM News n")
    Page<NewsTableDTO> findAllForTable(Pageable pageable);

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
