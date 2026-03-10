package com.angola_argentina_portal.Mapper;

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
        dto.setAddress(person.getAddress());
        dto.setCity(person.getCity());
        dto.setLatitude(person.getLatitude());
        dto.setLongitude(person.getLongitude());
        dto.setFkUser(person.getFkUser());
        dto.setImagePerson(person.getImagePerson());
        dto.setCreatedAt(person.getCreatedAt());
        dto.setUpdatedAt(person.getUpdatedAt());
        dto.setEmail(person.getEmail());
        dto.setDocumentNumber(person.getDocumentNumber());
        dto.setDocumentType(person.getDocumentType());
        dto.setActive(person.isActive());
        dto.setFullName(person.getFullName());
        dto.setInitials(person.getInitials());

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
        person.setAddress(dto.getAddress());
        person.setCity(dto.getCity());
        person.setLatitude(dto.getLatitude());
        person.setLongitude(dto.getLongitude());
        person.setImagePerson(dto.getImagePerson());

        if (dto.getFkUser() != null) {
            person.setFkUser(dto.getFkUser());
        }

        person.setEmail(dto.getEmail());
        person.setDocumentNumber(dto.getDocumentNumber());
        person.setDocumentType(dto.getDocumentType());
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

        if (dto.getAddress() != null) {
            person.setAddress(dto.getAddress());
        }

        if (dto.getCity() != null) {
            person.setCity(dto.getCity());
        }

        if (dto.getLatitude() != null) {
            person.setLatitude(dto.getLatitude());
        }

        if (dto.getLongitude() != null) {
            person.setLongitude(dto.getLongitude());
        }

        if (dto.getFkUser() != null) {
            person.setFkUser(dto.getFkUser());
        }

        if (dto.getEmail() != null) {
            person.setEmail(dto.getEmail());
        }

        if (dto.getDocumentNumber() != null) {
            person.setDocumentNumber(dto.getDocumentNumber());
        }

        if (dto.getDocumentType() != null) {
            person.setDocumentType(dto.getDocumentType());
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
