package com.SistemSchool.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SistemSchool.dto.UserDTO;
import com.SistemSchool.mapper.UserMapper;
import com.SistemSchool.model.User;
import com.SistemSchool.repository.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // Método para gerar salt aleatório
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Método para gerar hash da senha
    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Erro ao gerar hash da senha", e);
            throw new RuntimeException("Erro no processamento da senha");
        }
    }

    // Criar novo usuário
    public UserDTO.UserResponseDTO createUser(UserDTO.CreateUserDTO createUserDTO) {
        logger.info("Criando novo usuário com email: {}", createUserDTO.getEmail());

        // Verificar se email já existe
        if (userRepository.existsByEmailNative(createUserDTO.getEmail()) > 0) {
            throw new RuntimeException("Email já está em uso");
        }

        // Gerar salt e hash da senha
        String salt = generateSalt();
        String hashedPassword = hashPassword(createUserDTO.getPassword(), salt);

        // Criar entidade
        User user = userMapper.toEntity(createUserDTO);
        user.setPassword(hashedPassword);
        user.setSalt(salt);

        // Salvar
        User savedUser = userRepository.save(user);
        logger.info("Usuário criado com ID: {}", savedUser.getPkUser());

        return userMapper.toResponseDTO(savedUser);
    }

    // Buscar usuário por ID
    public UserDTO.UserResponseDTO getUserById(int userId) {
        logger.debug("Buscando usuário por ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + userId));

        return userMapper.toResponseDTO(user);
    }

    // Buscar todos usuários ativos (usando query nativa)
    public List<UserDTO.UserResponseDTO> getAllActiveUsers() {
        logger.debug("Buscando todos usuários ativos");

        List<User> users = userRepository.findActiveUsersNative();

        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar usuário por email (usando query nativa)
    public Optional<UserDTO.UserResponseDTO> getUserByEmail(String email) {
        logger.debug("Buscando usuário por email: {}", email);

        return userRepository.findByEmailNative(email)
                .map(userMapper::toResponseDTO);
    }

    // Atualizar usuário
    public UserDTO.UserResponseDTO updateUser(UserDTO.UpdateUserDTO updateUserDTO) {
        logger.info("Atualizando usuário com ID: {}", updateUserDTO.getPkUser());

        User user = userRepository.findById(updateUserDTO.getPkUser())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + updateUserDTO.getPkUser()));

        // Verificar se novo email já existe (se foi alterado)
        if (updateUserDTO.getEmail() != null &&
                !updateUserDTO.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmailNative(updateUserDTO.getEmail()) > 0) {
            throw new RuntimeException("Novo email já está em uso");
        }

        // Atualizar dados
        userMapper.updateFromDTO(user, updateUserDTO);

        // Salvar
        User updatedUser = userRepository.save(user);
        logger.info("Usuário atualizado com ID: {}", updatedUser.getPkUser());

        return userMapper.toResponseDTO(updatedUser);
    }

    public void updatePassword(User user){
        userRepository.save(user);
    }

    // Atualizar status do usuário (usando query nativa para performance)
    public void updateUserStatus(int userId, boolean active) {
        logger.info("Atualizando status do usuário ID: {} para active: {}", userId, active);

        int updated = userRepository.updateUserStatusNative(userId, active);

        if (updated == 0) {
            throw new RuntimeException("Usuário não encontrado com ID: " + userId);
        }

        logger.info("Status do usuário ID: {} atualizado com sucesso", userId);
    }

    // Atualizar device token (usando query nativa)
    public void updateDeviceToken(int userId, String deviceToken) {
        logger.info("Atualizando device token do usuário ID: {}", userId);

        int updated = userRepository.updateDeviceTokenNative(userId, deviceToken);

        if (updated == 0) {
            throw new RuntimeException("Usuário não encontrado com ID: " + userId);
        }

        logger.info("Device token do usuário ID: {} atualizado com sucesso", userId);
    }

    // Autenticar usuário
    public UserDTO.UserResponseDTO authenticate(UserDTO.LoginDTO loginDTO) {

        List<Object[]> results = userRepository.findUserCredentialsNative(loginDTO.getEmail());

        if (results == null || results.isEmpty()) {
            throw new RuntimeException("Credenciais inválidas" + loginDTO.getEmail());
        }

        // 2. Pegue a primeira linha do resultado
        Object[] credentials = results.get(0);

        int userId = ((Number) credentials[0]).intValue();
        String storedPasswordHash = (String) credentials[1];
        String salt = (String) credentials[2];
        // Gerar hash da senha fornecida
        //String providedPasswordHash = PasswordUtil.hashPassword(loginDTO.getPassword(), salt);

        // Verificar senha
        // if (!storedPasswordHash.equals(providedPasswordHash)) {
        //     throw new RuntimeException("Credenciais inválidas");
        // }

        // Buscar usuário
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verificar se usuário está ativo
        if (!user.isActive()) {
            throw new RuntimeException("Usuário inativo");
        }

            logger.info("Usuário autenticado com sucesso: {}", userId);
        return userMapper.toResponseDTO(user);
    }

    // Buscar usuários com paginação (usando query nativa)
    public List<UserDTO.UserResponseDTO> getUsersWithPagination(int page, int size) {
        logger.debug("Buscando usuários - página: {}, tamanho: {}", page, size);

        int offset = page * size;
        List<User> users = null;// userRepository.findUsersWithPaginationNative(size, offset);

        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar usuários por tipo (usando query nativa)
    public List<UserDTO.UserResponseDTO> getUsersByType(int userTypeId) {
        logger.debug("Buscando usuários por tipo: {}", userTypeId);

        List<User> users = userRepository.findByUserTypeNative(userTypeId);

        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Contar usuários ativos por tipo
    public long countActiveUsersByType(int userTypeId) {
        return userRepository.countActiveUsersByTypeNative(userTypeId);
    }

    // Verificar se email existe
    public boolean emailExists(String email) {
        return userRepository.existsByEmailNative(email) > 0;
    }

    // Buscar usuários criados após uma data
    public List<UserDTO.UserResponseDTO> getUsersCreatedAfter(Date date) {
        List<User> users = userRepository.findUsersCreatedAfterDateNative(date);

        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar usuários com detalhes (usando query nativa complexa)
    public List<Object[]> getUsersWithDetails() {
        return userRepository.findUsersWithDetailsNative();
    }

    public void updateFkPerson(int pkUser, Integer pkPerson) {
        if(pkUser <= 0 || pkPerson == null || pkPerson <= 0) {
            throw new IllegalArgumentException("IDs inválidos para atualização de fk_person");
        }
        userRepository.updateFkPerson(pkUser, pkPerson);
    }
}