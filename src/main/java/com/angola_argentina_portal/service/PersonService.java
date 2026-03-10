package com.angola_argentina_portal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.angola_argentina_portal.dto.PersonDTO;
import com.angola_argentina_portal.mapper.PersonMapper;
import com.angola_argentina_portal.model.Person;
import com.angola_argentina_portal.repository.PersonRepository;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    // Criar nova pessoa
    public PersonDTO.PersonResponseDTO createPerson(PersonDTO.CreatePersonDTO createPersonDTO) {
        logger.info("Criando nova pessoa: {} {}", createPersonDTO.getFirstName(), createPersonDTO.getLastName());

        // Verificar se email já existe
        if (createPersonDTO.getEmail() != null &&
                personRepository.existsByEmailNative(createPersonDTO.getEmail()) > 0) {
            throw new RuntimeException("Email já está em uso");
        }

        // Criar entidade Person a partir do DTO
        Person person = personMapper.toEntity(createPersonDTO);

        // Salvar no banco de dados
        Person savedPerson = personRepository.save(person);
        logger.info("Pessoa criada com ID: {}", savedPerson.getPkPerson());
        // Converter para DTO de resposta        return personMapper.toResponseDTO(savedPerson);
        return personMapper.toResponseDTO(savedPerson);
    }

    // Buscar pessoa por ID
    public PersonDTO.PersonResponseDTO getPersonById(int pesonID) {
        logger.info("Buscando pessoa por ID: {}", pesonID);
        
        Person person = personRepository.findActivePersonById(pesonID)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
        return personMapper.toResponseDTO(person);
    }

    // Buscar todas pessoas ativas
    public List<PersonDTO.PersonResponseDTO> getAllActivePersons() {
        logger.info("Buscando todas as pessoas ativas");
        
        List<Person> persons = personRepository.findAllActivePersons();
        return persons.stream()
                .map(personMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar pessoa por email
    public PersonDTO.PersonResponseDTO getPersonByEmail(String email) {
        logger.info("Buscando pessoa por email: {}", email);
        
        Person person = personRepository.findPersonByEmail(email)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
        return personMapper.toResponseDTO(person);
    }

    // Upload de imagem
    public PersonDTO.PersonResponseDTO uploadPersonImage(int personID, String imageUrl) {
        logger.info("Fazendo upload de imagem para pessoa ID: {}", personID);
        
        Person person = personRepository.findActivePersonById(personID)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
        person.setImagePerson(imageUrl);
        Person updatedPerson = personRepository.save(person);
        logger.info("Imagem atualizada para pessoa ID: {}", personID);
        return personMapper.toResponseDTO(updatedPerson);
    }

}
