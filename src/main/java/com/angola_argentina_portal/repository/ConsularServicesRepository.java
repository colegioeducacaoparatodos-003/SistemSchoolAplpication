package com.angola_argentina_portal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.angola_argentina_portal.interfaces.ServiceTableProjection;
import com.angola_argentina_portal.model.ConsularServices;

@Repository
public interface ConsularServicesRepository extends JpaRepository<ConsularServices, Long> {

    @Query(value = """
            SELECT s.service_name AS serviceName,
                   s.requirements AS requirements,
                   s.fees AS fees,
                   s.price AS price,
                   s.details AS details,
                   s.status AS status,
                   s.available_days AS availableDays,
                   s.online_booking AS onlineBooking
            FROM service s
            """, countQuery = "SELECT COUNT(*) FROM service", nativeQuery = true)
    Page<ServiceTableProjection> findAllForTable(Pageable pageable);
}
