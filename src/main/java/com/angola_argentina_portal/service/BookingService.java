package com.angola_argentina_portal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.angola_argentina_portal.dto.BookingTableDTO;
import com.angola_argentina_portal.interfaces.BookingTableProjection;
import com.angola_argentina_portal.model.Booking;
import com.angola_argentina_portal.repository.BookingRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingService {

    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public Booking save(Booking booking) {

        int count = repository.countBookings(
                booking.getFkService(),
                booking.getBookingDate(),
                booking.getBookingTime());

        if (count >= 5) {
            throw new RuntimeException("No slots available for this time");
        }

        return repository.save(booking);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<BookingTableDTO> findLazy(int page, int size, Sort sort) {

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BookingTableProjection> projections = repository.findAllForTable(pageable);

        return projections.map(p -> new BookingTableDTO(
                p.getPkBooking(),
                p.getServiceName(),
                p.getFullName(),
                p.getEmail(),
                p.getPhone(),
                p.getBookingDate(),
                p.getBookingTime(),
                p.getStatus()));
    }

    public Booking findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
}