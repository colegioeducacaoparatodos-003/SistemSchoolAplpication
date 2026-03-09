package com.angola_argentina_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angola_argentina_portal.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
        // Custom query methods can be defined here
}
