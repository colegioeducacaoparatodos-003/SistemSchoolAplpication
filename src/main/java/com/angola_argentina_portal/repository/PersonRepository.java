package com.angola_argentina_portal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.angola_argentina_portal.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    /*
     * ===============================
     * BASIC SELECT QUERIES
     * ===============================
     */

    @Query(value = """
            SELECT
                pk_person,
                first_name,
                middle_name,
                last_name,
                phone,
                image_person,
                email,
                active
            FROM person
            WHERE active = true
            """, nativeQuery = true)
    List<Person> findAllActivePersons();

    @Query(value = """
            SELECT *
            FROM person
            WHERE pk_person = :id
            AND active = true
            """, nativeQuery = true)
    Optional<Person> findActivePersonById(@Param("id") Integer id);

    @Query(value = """
            SELECT *
            FROM person
            WHERE email = :email
            """, nativeQuery = true)
    Optional<Person> findPersonByEmail(@Param("email") String email);

    /*
     * ===============================
     * SEARCH QUERIES
     * ===============================
     */

    @Query(value = """
            SELECT *
            FROM person
            WHERE
                LOWER(first_name) LIKE LOWER(CONCAT('%', :name, '%'))
                OR LOWER(last_name) LIKE LOWER(CONCAT('%', :name, '%'))
            AND active = true
            ORDER BY first_name ASC
            """, nativeQuery = true)
    List<Person> searchPersonByName(@Param("name") String name);

    /*
     * ===============================
     * PAGINATION QUERY
     * ===============================
     */

    @Query(value = """
            SELECT *
            FROM person
            WHERE active = true
            ORDER BY pk_person DESC
            """, countQuery = """
            SELECT COUNT(*)
            FROM person
            WHERE active = true
            """, nativeQuery = true)
    Page<Person> findAllActivePersons(Pageable pageable);

    /*
     * ===============================
     * KPI QUERY
     * ===============================
     */

    @Query(value = """
            SELECT COUNT(*)
            FROM person
            WHERE active = true
            """, nativeQuery = true)
    Long countActivePersons();

    /*
     * ===============================
     * UPDATE QUERY
     * ===============================
     */

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE person
            SET active = false
            WHERE pk_person = :id
            """, nativeQuery = true)
    void deactivatePerson(@Param("id") Integer id);

    // Query nativa para verificar se email existe
    @Query(value = "SELECT EXISTS(SELECT 1 FROM person WHERE email = :email)", nativeQuery = true)
    int existsByEmailNative(@Param("email") String email);
}
