package com.angola_argentina_portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angola_argentina_portal.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findByReferenceTypeAndReferenceId(String referenceType, String referenceId);

}
