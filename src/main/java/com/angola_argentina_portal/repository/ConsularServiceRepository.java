package com.angola_argentina_portal.repository;

import com.angola_argentina_portal.interfaces.ConsularServiceTableProjection;
import com.angola_argentina_portal.model.ConsularService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ConsularServiceRepository
        extends JpaRepository<ConsularService, Integer> {

    @Query(value = """
            SELECT
                cs.pk_service AS pkService,
                cs.name AS name,
                cs.requirements AS requirements,
                cs.fees AS fees,
                cs.price AS price,
                cs.details AS details,
                cs.status AS status,
                cs.available_days AS availableDays,
                cs.online_booking AS onlineBooking
            FROM consular_service cs
            """, countQuery = "SELECT COUNT(*) FROM consular_service", nativeQuery = true)
    Page<ConsularServiceTableProjection> findAllForTable(Pageable pageable);
}