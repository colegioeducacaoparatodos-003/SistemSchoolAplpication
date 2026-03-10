package com.angola_argentina_portal.mapper;

import org.springframework.stereotype.Component;

import com.angola_argentina_portal.dto.PersonDTO;
import com.angola_argentina_portal.model.Person;

@Component
public class PersonMapper {

    // Converte Person para PersonResponseDTO
    public PersonDTO.PersonResponseDTO toResponseDTO(Person person) {
        if (person == null) {
            return null;
        }

        PersonDTO.PersonResponseDTO dto = new PersonDTO.PersonResponseDTO();
        dto.setPkPerson(person.getPkPerson());
        dto.setFirstName(person.getFirstName());
        dto.setMiddleName(person.getMiddleName());
        dto.setLastName(person.getLastName());
        dto.setPhone(person.getPhone());
        dto.setImagePerson(person.getImagePerson());

        dto.setEmail(person.getEmail());

        dto.setActive(person.isActive());


        return dto;
    }

    // Converte CreatePersonDTO para Person
    public Person toEntity(PersonDTO.CreatePersonDTO dto) {
        if (dto == null) {
            return null;
        }

        Person person = new Person();
        person.setFirstName(dto.getFirstName());
        person.setMiddleName(dto.getMiddleName());
        person.setLastName(dto.getLastName());
        person.setPhone(dto.getPhone());
        person.setImagePerson(dto.getImagePerson());


        person.setEmail(dto.getEmail());
        person.setActive(dto.isActive());

        return person;
    }

    // Atualiza Person a partir de UpdatePersonDTO
    public void updateFromDTO(Person person, PersonDTO.UpdatePersonDTO dto) {
        if (dto == null || person == null) {
            return;
        }

        if (dto.getFirstName() != null) {
            person.setFirstName(dto.getFirstName());
        }

        if (dto.getMiddleName() != null) {
            person.setMiddleName(dto.getMiddleName());
        }

        if (dto.getLastName() != null) {
            person.setLastName(dto.getLastName());
        }

        if (dto.getPhone() != null) {
            person.setPhone(dto.getPhone());
        }

        if (dto.getEmail() != null) {
            person.setEmail(dto.getEmail());
        }
        
        if (dto.getActive() != null) {
            person.setActive(dto.getActive());
        }

        if (dto.getImagePerson() != null) {
            person.setImagePerson(dto.getImagePerson());
        }

        // updatedAt será automaticamente atualizado pelo @PreUpdate
    }

}
