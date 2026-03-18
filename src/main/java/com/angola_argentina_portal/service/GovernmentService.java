package com.angola_argentina_portal.service;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.angola_argentina_portal.dto.DocumentTableDTO;
import com.angola_argentina_portal.dto.GovernmentDTO;
import com.angola_argentina_portal.dto.PersonDTO;
import com.angola_argentina_portal.interfaces.DocumentTableProjection;
import com.angola_argentina_portal.interfaces.GovernmentTableProjection;
import com.angola_argentina_portal.io.Assistant;
import com.angola_argentina_portal.io.FileImage;
import com.angola_argentina_portal.model.Government;
import com.angola_argentina_portal.model.Person;
import com.angola_argentina_portal.repository.GovernmentRepository;

@Service
public class GovernmentService {

    private final GovernmentRepository repository;

    public GovernmentService(GovernmentRepository repository){
        this.repository = repository;
    }

    //     // Buscar pessoa por ID
    // public PersonDTO.PersonResponseDTO getPGovernmentById(int id) {
    //     // logger.debug("Buscando pessoa por ID: {}", id);

    //     // Person person = repository.findById(id)
    //     //         .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com ID: " + id));

    //     // return personMapper.toResponseDTO(person);
    // }

    public Government save(Government government){
        return repository.save(government);
    }
public Page<GovernmentDTO> findLazy(int page, int size, Sort sort) {

    Pageable pageable = PageRequest.of(page, size, sort);

    Page<GovernmentTableProjection> projections = repository.findAllForTable(pageable);

    return projections.map(p -> new GovernmentDTO(
            p.getId(),
            p.getFullName(),
            p.getType(),
            p.getTitle(),
            p.getSubTitle(),
            p.getDescription()
    ));
}
}
