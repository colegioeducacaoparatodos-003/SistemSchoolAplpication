package com.angola_argentina_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.angola_argentina_portal.interfaces.BookingTableProjection;
import com.angola_argentina_portal.model.Booking;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = """
            SELECT
                b.pk_booking AS pkBooking,
                cs.name AS serviceName,
                b.full_name AS fullName,
                b.email AS email,
                b.phone AS phone,
                b.booking_date AS bookingDate,
                b.booking_time AS bookingTime,
                b.status AS status
            FROM booking b
            LEFT JOIN consular_service cs ON b.fk_service = cs.pk_service
            """, countQuery = "SELECT COUNT(*) FROM booking", nativeQuery = true)
    Page<BookingTableProjection> findAllForTable(Pageable pageable);

    // Controle de vagas
    @Query("""
                SELECT COUNT(b)
                FROM Booking b
                WHERE b.fkService = :serviceId
                AND b.bookingDate = :date
                AND b.bookingTime = :time
            """)
    int countBookings(int serviceId, LocalDate date, String time);
}
