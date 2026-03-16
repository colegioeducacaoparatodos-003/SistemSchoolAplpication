package com.angola_argentina_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angola_argentina_portal.model.Government;

@Repository
public interface GovernmentRepository extends JpaRepository<Government, Long>{

}
