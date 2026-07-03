package com.SistemSchool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.SistemSchool.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Query nativa para buscar usuário por email
    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmailNative(@Param("email") String email);

    // Query nativa para buscar usuários ativos
    @Query(value = "SELECT * FROM user WHERE active = true ORDER BY user_creation_date DESC", nativeQuery = true)
    List<User> findActiveUsersNative();

    // Query nativa para buscar usuários por tipo
    @Query(value = "SELECT * FROM user WHERE fk_user_type = :userTypeId", nativeQuery = true)
    List<User> findByUserTypeNative(@Param("userTypeId") int userTypeId);

    // Query nativa para buscar usuários por pessoa
    @Query(value = "SELECT * FROM user WHERE fk_person = :personId", nativeQuery = true)
    Optional<User> findByPersonIdNative(@Param("personId") int personId);

    // Query nativa para buscar usuários criados após uma data
    @Query(value = "SELECT * FROM user WHERE user_creation_date > :date", nativeQuery = true)
    List<User> findUsersCreatedAfterDateNative(@Param("date") Date date);

    // Query nativa para atualizar status ativo
    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET active = :active, user_modification_date = NOW() WHERE pk_user = :userId", nativeQuery = true)
    int updateUserStatusNative(@Param("userId") int userId, @Param("active") boolean active);

    // Query nativa para atualizar device token
    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET device_token = :deviceToken, user_modification_date = NOW() WHERE pk_user = :userId", nativeQuery = true)
    int updateDeviceTokenNative(@Param("userId") int userId, @Param("deviceToken") String deviceToken);

    // Query nativa para contar usuários ativos por tipo
    @Query(value = "SELECT COUNT(*) FROM user WHERE fk_user_type = :userTypeId AND active = true", nativeQuery = true)
    long countActiveUsersByTypeNative(@Param("userTypeId") int userTypeId);

    // Query nativa para buscar usuários com paginação
   // @Query(value = "SELECT * FROM user ORDER BY user_creation_date DESC LIMIT :limit OFFSET :offset   nativeQuery = true)
    //List<User> findUsersWithPaginationNative(@Param("limit") int limit, @Param("offset") int offset

    // Query nativa para buscar email e senha (para login)
    @Query(value = "SELECT pk_user, password, salt FROM user WHERE email = :email AND active = true", nativeQuery = true)
    List<Object[]> findUserCredentialsNative(@Param("email") String email);

    // Query nativa para verificar se email existe
    @Query(value = "SELECT EXISTS(SELECT 1 FROM user WHERE email = :email)", nativeQuery = true)
    int existsByEmailNative(@Param("email") String email);

    // Query nativa complexa com JOIN (exemplo)
    @Query(value = """
            SELECT u.*, p.name as person_name, ut.description as user_type_desc
            FROM user u
            LEFT JOIN person p ON u.fk_person = p.pk_person
            LEFT JOIN user_type ut ON u.fk_user_type = ut.pk_user_type
            WHERE u.active = true
            ORDER BY u.user_creation_date DESC
            """, nativeQuery = true)
    List<Object[]> findUsersWithDetailsNative();

    // Métodos padrão do Spring Data JPA
    Optional<User> findByEmail(String email);

    List<User> findByActive(boolean active);

    List<User> findByFkUserType(int fkUserType);

    boolean existsByEmail(String email);

    @Modifying
    @Query(value = """
            UPDATE user u SET u.fk_person = :userId where u.pk_user = :id
            """, nativeQuery = true)
    void updateFkPerson(@Param("id") int id, @Param("userId") int userId);
}