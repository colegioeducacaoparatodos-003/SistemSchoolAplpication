package com.angola_argentina_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.angola_argentina_portal.dto.PersonDTO;
import com.angola_argentina_portal.repository.PersonRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PersonService {
    
    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonRepository personRepository;

    public PersonDTO.CreatePersonDTO createPerson(PersonDTO.CreatePersonDTO createPersonDTO) {
        logger.info("Creating person with email: {}", createPersonDTO.getEmail());
        // Lógica para criar pessoa usando personRepository
        // Converter CreatePersonDTO para Person, salvar e retornar o DTO criado
        return null; // Substituir pelo DTO criado
    }

    public PersonDTO.PersonResponseDTO getPersonById(int id) {
        logger.info("Fetching person with ID: {}", id);
        // Lógica para buscar pessoa por ID usando personRepository
        // Converter Person para PersonResponseDTO e retornar
        return null; // Substituir pelo DTO encontrado
    }

    public List<PersonDTO.PersonResponseDTO> getAllActivePersons() {
        logger.info("Fetching all persons");
        // Lógica para buscar todas as pessoas usando personRepository
        // Converter List<Person> para List<PersonResponseDTO> e retornar
        return null; // Substituir pela lista de DTOs encontrados
    }
}
