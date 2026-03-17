package com.angola_argentina_portal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.angola_argentina_portal.dto.ConsularServiceTableDTO;
import com.angola_argentina_portal.interfaces.ConsularServiceTableProjection;
import com.angola_argentina_portal.model.ConsularService;
import com.angola_argentina_portal.repository.ConsularServiceRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConsularServiceService {

    private final ConsularServiceRepository repository;

    public ConsularServiceService(ConsularServiceRepository repository) {
        this.repository = repository;
    }

    public ConsularService save(ConsularService service) {
        return repository.save(service);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<ConsularServiceTableDTO> findLazy(int page, int size, Sort sort) {

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ConsularServiceTableProjection> projections = repository.findAllForTable(pageable);

        return projections.map(p -> new ConsularServiceTableDTO(
                p.getPkService(),
                p.getName(),
                p.getRequirements(),
                p.getFees(),
                p.getPrice(),
                p.getDetails(),
                p.getStatus(),
                p.getAvailableDays(),
                p.getOnlineBooking()));
    }
}