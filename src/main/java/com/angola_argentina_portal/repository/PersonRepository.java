package com.angola_argentina_portal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.angola_argentina_portal.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

       // Query nativa para buscar pessoa por email
        @Query(value = "SELECT * FROM person WHERE email = :email", nativeQuery = true)
        Optional<Person> findByEmailNative(@Param("email") String email);

        // Query nativa para buscar pessoa por número de documento
        @Query(value = "SELECT * FROM person WHERE document_number = :documentNumber", nativeQuery = true)
        Optional<Person> findByDocumentNumberNative(@Param("documentNumber") String documentNumber);

        // Query nativa para buscar pessoas ativas
        @Query(value = "SELECT * FROM person WHERE active = true ORDER BY created_at DESC", nativeQuery = true)
        List<Person> findActivePersonsNative();

        // Query nativa para buscar pessoas por cidade
        @Query(value = "SELECT * FROM person WHERE city = :city AND is_active = true", nativeQuery = true)
        List<Person> findByCityNative(@Param("city") String city);

        // Query nativa para buscar pessoas por usuário associado
        @Query(value = "SELECT * FROM person WHERE fk_user = :userId", nativeQuery = true)
        List<Person> findByUserIdNative(@Param("userId") int userId);

        // Query nativa para buscar pessoas com nome similar
        @Query(value = "SELECT * FROM person WHERE " +
                        "(first_name LIKE CONCAT('%', :name, '%') OR " +
                        "last_name LIKE CONCAT('%', :name, '%') OR " +
                        "middle_name LIKE CONCAT('%', :name, '%')) " +
                        "AND is_active = true", nativeQuery = true)
        List<Person> findByNameContainingNative(@Param("name") String name);

        // Query nativa para buscar pessoas criadas após uma data
        @Query(value = "SELECT * FROM person WHERE created_at > :date", nativeQuery = true)
        List<Person> findPersonsCreatedAfterDateNative(@Param("date") Date date);

        // Query nativa para atualizar status ativo
        @Modifying
        @Transactional
        @Query(value = "UPDATE person SET is_active = :active, updated_at = NOW() WHERE pk_person = :personId", nativeQuery = true)
        int updatePersonStatusNative(@Param("personId") int personId, @Param("active") boolean active);

        // Query nativa para atualizar imagem da pessoa
        @Modifying
        @Transactional
        @Query(value = "UPDATE person SET image_person = :imagePath, updated_at = NOW() WHERE pk_person = :personId", nativeQuery = true)
        int updateImagePathNative(@Param("personId") int personId, @Param("imagePath") String imagePath);

        // Query nativa para contar pessoas ativas por cidade
        @Query(value = "SELECT COUNT(*) FROM person WHERE city = :city AND is_active = true", nativeQuery = true)
        long countActivePersonsByCityNative(@Param("city") String city);

        // Query nativa para buscar pessoas com paginação - CORRIGIDO
        @Query(value = "SELECT * FROM person ORDER BY created_at DESC", countQuery = "SELECT COUNT(*) FROM person", nativeQuery = true)
        Page<Person> findAllWithPaginationNative(Pageable pageable);

        // Query nativa para verificar se email existe
        @Query(value = "SELECT EXISTS(SELECT 1 FROM person WHERE email = :email)", nativeQuery = true)
        int existsByEmailNative(@Param("email") String email);

        // Query nativa para verificar se número de documento existe
        @Query(value = "SELECT EXISTS(SELECT 1 FROM person WHERE document_number = :documentNumber)", nativeQuery = true)
        int existsByDocumentNumberNative(@Param("documentNumber") String documentNumber);

        // Query nativa complexa com JOIN com usuário
        @Query(value = "SELECT p.*, u.email as user_email " +
                        "FROM person p " +
                        "LEFT JOIN user u ON p.fk_user = u.pk_user " +
                        "WHERE p.is_active = true " +
                        "ORDER BY p.created_at DESC", nativeQuery = true)
        List<Object[]> findPersonsWithUserDetailsNative();

        // Query nativa para buscar pessoas por múltiplos critérios - CORRIGIDO
        @Query(value = "SELECT * FROM person " +
                        "WHERE (:firstName IS NULL OR first_name LIKE CONCAT('%', :firstName, '%')) " +
                        "AND (:lastName IS NULL OR last_name LIKE CONCAT('%', :lastName, '%')) " +
                        "AND (:phone IS NULL OR phone LIKE CONCAT('%', :phone, '%')) " +
                        "AND (:email IS NULL OR email LIKE CONCAT('%', :email, '%')) " +
                        "AND (:city IS NULL OR city LIKE CONCAT('%', :city, '%')) " +
                        "AND (:documentNumber IS NULL OR document_number LIKE CONCAT('%', :documentNumber, '%')) " +
                        "AND (:active IS NULL OR is_active = :active) " +
                        "AND (:fkUser IS NULL OR fk_user = :fkUser)", nativeQuery = true)
        List<Person> findByMultipleCriteriaNative(
                        @Param("firstName") String firstName,
                        @Param("lastName") String lastName,
                        @Param("phone") String phone,
                        @Param("email") String email,
                        @Param("city") String city,
                        @Param("documentNumber") String documentNumber,
                        @Param("active") Boolean active,
                        @Param("fkUser") Integer fkUser);

        // Query nativa para buscar pessoas próximas por coordenadas (raio de 10km)
        @Query(value = "SELECT *, " +
                        "(6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) * " +
                        "cos(radians(longitude) - radians(:longitude)) + " +
                        "sin(radians(:latitude)) * sin(radians(latitude)))) AS distance " +
                        "FROM person " +
                        "HAVING distance < 10 " +
                        "ORDER BY distance", nativeQuery = true)
        List<Person> findNearbyPersonsNative(@Param("latitude") Double latitude,
                        @Param("longitude") Double longitude);

        // Métodos padrão do Spring Data JPA
        Optional<Person> findByEmail(String email);

        Optional<Person> findByDocumentNumber(String documentNumber);

        List<Person> findByCity(String city);

        List<Person> findByFkUser(int fkUser);

        @Query("""
                            SELECT p
                            FROM Person p
                            WHERE p.fkUser = :userId
                              AND p.active = true
                        """)
        Optional<Person> findByFkUserForAvatar(@Param("userId") Integer userId);

        List<Person> findByActive(boolean active);

        boolean existsByEmail(String email);

        boolean existsByDocumentNumber(String documentNumber);

        // Método adicional usando JPQL (opcional)
        @Query("SELECT p FROM Person p WHERE " +
                        "(LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
                        "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
                        "LOWER(p.middleName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
                        "AND p.active = true")
        List<Person> findByNameContainingIgnoreCase(@Param("name") String name);

        /**
         * Fetches person details for selection DTOs.
         * We select the names separately to handle nulls in the Service/DTO layer.
         */
        @Query(value = """
                        SELECT pk_person, first_name, middle_name, last_name, document_number, image_person
                        FROM person
                        WHERE active = true
                        """, nativeQuery = true)
        List<Object[]> findPersonNamesDTO();

        @Query(value = "SELECT count(p.pk_person) FROM person p", nativeQuery = true)
        long headcountKpi();

        @Query("""
                            SELECT p
                            FROM Person p
                            WHERE p.pkPerson = :id
                        """)
        Optional<Person> findById(@Param("id") Integer id);

        @Query(value = """
                        SELECT p.pk_person, p.first_name, p.middle_name, p.last_name, p.phone
                        FROM person p
                        WHERE p.fk_user = :fkUser
                        """, nativeQuery = true)
        List<Object[]> findByFkUserNative(@Param("fkUser") int fkUser);

        @Query(value = "SELECT person.pk_person FROM person WHERE active = true", nativeQuery = true)
        List<Integer> findAllActiveIds();


}
