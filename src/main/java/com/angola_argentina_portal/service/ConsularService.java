package com.angola_argentina_portal.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.angola_argentina_portal.dto.ConsularServicesDTO;
import com.angola_argentina_portal.interfaces.ServiceTableProjection;
import com.angola_argentina_portal.model.ConsularServices;
import com.angola_argentina_portal.repository.ConsularServicesRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConsularService {

    private final ConsularServicesRepository repository;

    public ConsularService(ConsularServicesRepository repository) {
        this.repository = repository;
    }

    // Criar um novo serviço
    public ConsularServices save(ConsularServicesDTO createDTO) {
        ConsularServices services = new ConsularServices();

        services.setServiceName(createDTO.getServiceName());
        services.setRequirements(createDTO.getRequirements());
        services.setFees(createDTO.getFees());
        services.setPrice(createDTO.getPrice());
        services.setDetails(createDTO.getDetails());
        services.setStatus(createDTO.getStatus());
        services.setAvailableDays(createDTO.getAvailableDays());
        services.setOnlineBooking(createDTO.isOnlineBooking());
        return repository.save(services);


    }

    public Page<ConsularServicesDTO> findLazy(int page, int size, Sort sort, Map<String, Object> filters) {
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ServiceTableProjection> projections = repository.findAllForTable(pageable);

        return projections.map(p -> new ConsularServicesDTO(
                p.getServiceName(),
                p.getRequirements(),
                p.getFees(),
                p.getPrice(),
                p.getDetails(),
                p.getStatus(),
                p.getAvailableDays(),
                p.isOnlineBooking()
        ));
    }
}
