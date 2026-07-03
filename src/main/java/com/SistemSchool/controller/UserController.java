package com.SistemSchool.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.SistemSchool.dto.UserDTO;
import com.SistemSchool.dto.PersonDTO.PersonResponseDTO;
import com.SistemSchool.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named // Marks this class as a managed bean that is automatically instantiated and
       // available for use in the JSF context.
@ViewScoped // Ensures that the bean is available during the view lifecycle, meaning it
            // remains active as long as the user is on the current page.
public class UserController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Inject
    private transient UserService userService;

    // @Inject
    // //private transient PersonService personService;

    // @Inject
    // private PermissionService permissionService;

    // private Permission selectedPermission;
    // private List<Permission> availablePermissions;
    // private List<Permission> userPermissions;
    private List<Integer> selectedPermissionIds;
    private Integer selectedUserId;

    // Dados para login
    private String loginEmail;
    private String loginPassword;
    private boolean rememberMe;

    // Dados para novo usuário
    private String newFirstName;
    private String newMiddleName;
    private String newLastName;
    private String newUserEmail;
    private String newUserPassword;
    private String newUserConfirmPassword;
    private Integer newUserFkPerson = 0;
    private Integer newUserFkUserType = 0;
    private boolean newUserActive = true;
    private String newUserDeviceToken;

    // Dados para edição
    private UserDTO.UserResponseDTO selectedUser;
    private String editUserEmail;
    private String editUserDeviceToken;
    private boolean editUserActive;
    private Integer editUserFkUserType = 0;

    // Listas e estado
    private List<UserDTO.UserResponseDTO> users = new ArrayList<>();
    private List<UserDTO.UserResponseDTO> filteredUsers;
    private UserDTO.UserResponseDTO loggedUser;

    // Filtros
    private String filterEmail;
    private Integer filterUserType;
    private Boolean filterActive;

    // Estado da UI
    private boolean editMode;
    private boolean loginDialogVisible;
    private boolean registerDialogVisible;

    private String firstName;
    private String lastName;
    private String image_person;
    //private List<PersonDTO.PersonResponseDTO> personCache;

    //private PersonDTO.PersonResponseDTO selectedPersonDTO;

    @PostConstruct
    public void init() {
        logger.info("Inicializando UserController");
        // loadUsers();
        resetLoginFields();
        resetNewUserFields();
        resetEditFields();

        // Verificar se há usuário logado na sessão
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        loggedUser = (UserDTO.UserResponseDTO) sessionMap.get("loggedUser");
    }

    // ========== MÉTODO DE LOGIN ==========
    public String login() {
        try {
            logger.info("Tentativa de login com email: {}", loginEmail);

            validateLoginData();

            // Criar DTO de login
            UserDTO.LoginDTO loginDTO = new UserDTO.LoginDTO();
            loginDTO.setEmail(loginEmail.trim());
            loginDTO.setPassword(loginPassword);

            // Autenticar
            UserDTO.UserResponseDTO authenticatedUser = userService.authenticate(loginDTO);

            // Armazenar usuário na sessão
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            sessionMap.put("loggedUser", authenticatedUser);
            loggedUser = authenticatedUser;

            // Configurar cookie "lembrar-me"
            if (rememberMe) {
                logger.info("Lembrar-me ativado para usuário: {}", authenticatedUser.getEmail());
            }

            // Limpar campos
            resetLoginFields();

            // Fechar dialog se estiver aberto
            if (loginDialogVisible) {
                PrimeFaces.current().executeScript("PF('loginDialog').hide()");
                loginDialogVisible = false;
            }

            // Adicionar mensagem de sucesso
            addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Login realizado com sucesso!");

            // → Apenas retornar string de navegação com redirect
            return "/dashboard.xhtml?faces-redirect=true";

        } catch (RuntimeException e) {
            logger.error("Falha no login para email: {}", loginEmail, e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro de Autenticação",
                    "Email ou senha inválidos. Por favor, tente novamente.");
            loginPassword = null;
        } catch (Exception e) {
            logger.error("Erro inesperado durante login", e);
            addMessage(FacesMessage.SEVERITY_FATAL, "Erro do Sistema",
                    "Ocorreu um erro inesperado. Por favor, contate o administrador.");
        }

        return null;
    }
    // public String login() {
    // try {
    // logger.info("Tentativa de login com email: {}", loginEmail);

    // validateLoginData();

    // // Criar DTO de login
    // UserDTO.LoginDTO loginDTO = new UserDTO.LoginDTO();
    // loginDTO.setEmail(loginEmail.trim());
    // loginDTO.setPassword(loginPassword);

    // // Autenticar
    // UserDTO.UserResponseDTO authenticatedUser =
    // userService.authenticate(loginDTO);

    // // Armazenar usuário na sessão
    // ExternalContext externalContext =
    // FacesContext.getCurrentInstance().getExternalContext();
    // Map<String, Object> sessionMap = externalContext.getSessionMap();
    // sessionMap.put("loggedUser", authenticatedUser);
    // loggedUser = authenticatedUser;

    // // Configurar cookie de "lembrar-me" se necessário
    // if (rememberMe) {
    // // Aqui você implementaria a lógica para cookie
    // logger.info("Lembrar-me ativado para usuário: {}",
    // authenticatedUser.getEmail());
    // }

    // // Registrar login no log
    // logger.info("Usuário autenticado com sucesso: {} (ID: {})",
    // authenticatedUser.getEmail(), authenticatedUser.getPkUser());

    // // Limpar campos
    // resetLoginFields();

    // // Fechar dialog se estiver aberto
    // if (loginDialogVisible) {
    // PrimeFaces.current().executeScript("PF('loginDialog').hide()");
    // loginDialogVisible = false;
    // }

    // // Adicionar mensagem de sucesso
    // addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Login realizado com
    // sucesso!");

    // // Redirecionar para página principal
    // try {
    // externalContext.redirect(externalContext.getRequestContextPath() +
    // "/dashboard.xhtml");
    // } catch (IOException e) {
    // logger.error("Erro ao redirecionar após login", e);
    // }

    // // loadUserPermissions();

    // return "/dashboard.xhtml?faces-redirect=true";

    // } catch (RuntimeException e) {
    // logger.error("Falha no login para email: {}", loginEmail, e);
    // addMessage(FacesMessage.SEVERITY_ERROR, "Erro de Autenticação",
    // "Email ou senha invalidos. Por favor, tente novamente.");

    // // Limpar senha por segurança
    // loginPassword = null;
    // } catch (Exception e) {
    // logger.error("Erro inesperado durante login", e);
    // addMessage(FacesMessage.SEVERITY_FATAL, "Erro do Sistema",
    // "Ocorreu um erro inesperado. Por favor, contate o administrador.");
    // }

    // return null;
    // }

    // public void loadUserPermissions() {
    // try {
    // if (selectedUser != null) {
    // userPermissions =
    // permissionService.findPermissionsByUser(selectedUser.getPkUser());
    // } else {
    // userPermissions = new ArrayList<>();
    // }
    // } catch (Exception e) {
    // FacesMessageUtil.errorMessage("Erro ao carregar permissões do usuário");
    // e.printStackTrace();
    // }
    // }

    public String getUserInitials() {
        StringBuilder sb = new StringBuilder();

        if (firstName != null && !firstName.isBlank()) {
            sb.append(firstName.substring(0, 1).toUpperCase());
        }

        if (lastName != null && !lastName.isBlank()) {
            sb.append(lastName.substring(0, 1).toUpperCase());
        }

        return sb.toString();
    }

    // THIS METHODS IS A MIDDLEWARE THAT CHECKS IF THE USER IS AUTHENTICATED
    public void ensureLoggedIn() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();

        if (sessionMap.get("loggedUser") == null) {
            try {
                externalContext.redirect(externalContext.getRequestContextPath() + "/login.xhtml");
            } catch (IOException e) {
                logger.error("Erro ao redirecionar usuário não autenticado", e);
            }
        }
    }

    private void validateLoginData() {
        // Validar campos
        if (loginEmail == null || loginEmail.trim().isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Email é obrigatório");
            return;
        } else if (loginPassword == null || loginPassword.trim().isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Senha é obrigatória");
            return;
        }
    }

    public void logout() {
        try {
            if (loggedUser != null) {
                logger.info("Usuário fazendo logout: {} (ID: {})",
                        loggedUser.getEmail(), loggedUser.getPkUser());
            }

            // Invalidar sessão
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.invalidateSession();

            // Limpar usuário logado
            loggedUser = null;

            // Redirecionar para página de login
            externalContext.redirect(externalContext.getRequestContextPath() + "/login.xhtml");

        } catch (IOException e) {
            logger.error("Erro ao fazer logout", e);
        }
    }

    // ========== MÉTODOS AUXILIARES DE LOGIN ==========

    public void showLoginDialog() {
        resetLoginFields();
        loginDialogVisible = true;
        PrimeFaces.current().executeScript("PF('loginDialog').show()");
    }

    public void checkAutoLogin() {
        // Aqui você pode implementar lógica para verificar cookie de "lembrar-me"
        // e fazer login automático se necessário
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();

        if (sessionMap.containsKey("autoLoginToken")) {
            // Lógica para login automático
            logger.info("Verificando login automático");
        }
    }

    public boolean isUserLoggedIn() {
        return loggedUser != null;
    }

    // public boolean hasPermission(String permission) {
    // if (loggedUser == null) return false;
    // return permissionService.userHasPermission(loggedUser.getPkUser(),
    // permission);
    // }

    // public void loadAvailablePermissions(int targetUserId) {
    // this.availablePermissions =
    // permissionService.findAvailablePermissionsByUser(targetUserId);
    // }

    // public void saveUserPermissions() {
    // try {
    // permissionService.saveUserPermissions(
    // selectedUser.getPkUser(),
    // selectedPermissionIds
    // );
    // FacesMessageUtil.infoMessage("Permissions saved.");
    // selectedPermissionIds = null;
    // } catch (Exception e) {
    // e.printStackTrace();
    // FacesMessageUtil.errorMessage("Error saving permissions: " + e.getMessage());
    // }
    // }

    // ========== MÉTODOS CRUD ==========

    public String createUser() {
        try {
            logger.info("Criando novo usuário com email: {}", newUserEmail);
            validateNewUserData();

            UserDTO.CreateUserDTO createUserDTO = new UserDTO.CreateUserDTO();
            createUserDTO.setEmail(newUserEmail.trim());
            createUserDTO.setPassword(newUserPassword);
            createUserDTO.setFkPerson(newUserFkPerson);
            createUserDTO.setFkUserType(newUserFkUserType);
            createUserDTO.setActive(newUserActive);
            createUserDTO.setDeviceToken(newUserDeviceToken);

            // PersonDTO.CreatePersonDTO createPersonDTO = new PersonDTO.CreatePersonDTO();
            // createPersonDTO.setFirstName(newFirstName);
            // createPersonDTO.setMiddleName(newMiddleName);
            // createPersonDTO.setLastName(newLastName);
            // createPersonDTO.setEmail(newUserEmail.trim());
            // createPersonDTO.setImagePerson(this.image_person);

            UserDTO.UserResponseDTO createdUser = userService.createUser(createUserDTO);
            // createPersonDTO.setFkUser(createdUser.getPkUser());
            // PersonDTO.PersonResponseDTO createdPerson =
            // personService.createPerson(createPersonDTO);
            // userService.updateFkPerson(createdUser.getPkUser(),
            // createdPerson.getPkPerson());

            // if (selectedPermissionIds != null && !selectedPermissionIds.isEmpty()) {
            // permissionService.saveUserPermissions(createdUser.getPkUser(),
            // selectedPermissionIds);
            // }

            users.add(createdUser);

            // Limpar campos e fechar dialog
            resetNewUserFields();
            PrimeFaces.current().executeScript("PF('createUserDialog').hide()");

            addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário criado com sucesso!");

            return "/dashboard.xhtml?faces-redirect=true";
        } catch (RuntimeException e) {
            logger.error("Erro ao criar usuário", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage());
        }
        return null;
    }

    private void validateNewUserData() {
        if (!newUserPassword.equals(newUserConfirmPassword)) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "As senhas não coincidem");
            return;
        }
        if (newUserEmail == null || newUserEmail.trim().isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Email é obrigatório");
            return;
        }
    }

    public void updateUser() {
        try {
            if (selectedUser == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Nenhum usuário selecionado");
                return;
            }

            logger.info("Atualizando usuário ID: {}", selectedUser.getPkUser());

            // Criar DTO de atualização
            UserDTO.UpdateUserDTO updateUserDTO = new UserDTO.UpdateUserDTO();
            updateUserDTO.setPkUser(selectedUser.getPkUser());
            updateUserDTO.setEmail(editUserEmail);
            updateUserDTO.setFkUserType(editUserFkUserType);
            updateUserDTO.setActive(editUserActive);
            updateUserDTO.setDeviceToken(editUserDeviceToken);

            // Chamar serviço
            UserDTO.UserResponseDTO updatedUser = userService.updateUser(updateUserDTO);

            // if (selectedPermissionIds != null) {
            // permissionService.saveUserPermissions(updatedUser.getPkUser(),
            // selectedPermissionIds);
            // }

            // Atualizar na lista
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getPkUser() == updatedUser.getPkUser()) {
                    users.set(i, updatedUser);
                    break;
                }
            }

            // Limpar seleção e fechar dialog
            selectedUser = null;
            resetEditFields();
            PrimeFaces.current().executeScript("PF('editUserDialog').hide()");

            addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário atualizado com sucesso!");

        } catch (RuntimeException e) {
            logger.error("Erro ao atualizar usuário", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage());
        }
    }

    public void deleteUser() {
        if (selectedUser == null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Nenhum usuário selecionado");
            return;
        }

        try {
            logger.info("Desativando usuário ID: {}", selectedUser.getPkUser());

            userService.updateUserStatus(selectedUser.getPkUser(), false);

            // Remover da lista
            users.removeIf(user -> user.getPkUser() == selectedUser.getPkUser());
            selectedUser = null;

            addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário desativado com sucesso!");

        } catch (RuntimeException e) {
            logger.error("Erro ao desativar usuário", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage());
        }
    }

    // public void prepareEditUser(UserDTO.UserResponseDTO user) {
    // this.selectedUser = user;
    // this.editUserEmail = user.getEmail();
    // this.editUserFkUserType = user.getFkUserType();
    // this.editUserActive = user.isActive();
    // this.editUserDeviceToken = user.getDeviceToken();
    // this.editMode = true;

    // List<Permission> userPermissions =
    // permissionService.findPermissionsByUser(user.getPkUser());
    // this.selectedPermissionIds = userPermissions.stream()
    // .map(Permission::getPkPermission)
    // .collect(Collectors.toList());
    // }

    public void showRegisterDialog() {
        resetNewUserFields();
        registerDialogVisible = true;
        PrimeFaces.current().executeScript("PF('registerDialog').show()");
    }

    // ========== MÉTODOS DE RESET ==========

    private void resetLoginFields() {
        loginEmail = null;
        loginPassword = null;
        rememberMe = false;
    }

    private void resetNewUserFields() {
        newFirstName = null;
        newMiddleName = null;
        newLastName = null;
        newUserEmail = null;
        newUserPassword = null;
        newUserConfirmPassword = null;
        newUserFkPerson = 0;
        newUserFkUserType = 0;
        newUserActive = true;
        newUserDeviceToken = null;
        // selectedPermission = null;
        image_person = "";
        firstName = "";
        lastName = "";
        this.selectedPermissionIds = new ArrayList<>();
    }

    private void resetEditFields() {
        editUserEmail = null;
        editUserDeviceToken = null;
        editUserActive = true;
        editUserFkUserType = 0;
        editMode = false;
        this.selectedPermissionIds = new ArrayList<>();
    }

    // ========== MÉTODOS DE FILTRO ==========

    public void filterUsers() {
        if (users == null) {
            return;
        }

        filteredUsers = new ArrayList<>();

        for (UserDTO.UserResponseDTO user : users) {
            boolean matches = true;

            if (filterEmail != null && !filterEmail.trim().isEmpty()) {
                matches = user.getEmail().toLowerCase().contains(filterEmail.toLowerCase());
            }

            if (matches && filterUserType != null) {
                matches = user.getFkUserType() == filterUserType;
            }

            if (matches && filterActive != null) {
                matches = user.isActive() == filterActive;
            }

            if (matches) {
                filteredUsers.add(user);
            }
        }
    }

    // public List<Permission> getAllAvailablePermissions() {
    // return permissionService.findAvailablePermissionsByUser(selectedUserId);
    // }

    public String getInitials(String fullName) {
        if (fullName == null || fullName.isEmpty())
            return "?";
        String[] parts = fullName.split(" ");
        String initials = "";
        for (int i = 0; i < Math.min(parts.length, 2); i++) {
            initials += parts[i].substring(0, 1).toUpperCase();
        }
        return initials;
    }

    private final String[] avatarColors = new String[] {
            "#F44336", "#E91E63", "#9C27B0", "#673AB7",
            "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4",
            "#009688", "#4CAF50", "#8BC34A", "#CDDC39",
            "#FFC107", "#FF9800", "#FF5722"
    };

    /*
     * public String avatarClass(PersonResponseDTO emp) {
     * // garante que não seja null ou vazio
     * String name = emp.getFullSearchName();
     * if (name == null || name.isEmpty()) {
     * name = "unknown"; // valor padrão
     * }
     * 
     * // calcula o índice baseado no hash
     * int index = Math.abs(name.hashCode() % avatarColors.length);
     * 
     * return "avatar-color-" + index;
     * }
     */

    // public String avatarClass(ContractDTO.ExpiringThisMonthDTO item) {
    // if (item == null || item.getFullName() == null ||
    // item.getFullName().isEmpty()) {
    // return "avatar-color-0";
    // }

    // int index = Math.abs(item.getFullName().hashCode() % avatarColors.length);
    // return "avatar-color-" + index;
    // }

    public String avatarClass(PersonResponseDTO emp) {
        // garante que não seja null ou vazio
        String name = emp.getFullSearchName();
        if (name == null || name.isEmpty()) {
            name = "unknown"; // valor padrão
        }

        // calcula o índice baseado no hash
        int index = Math.abs(name.hashCode() % avatarColors.length);

        return "avatar-color-" + index;
    }

    public void clearFilters() {
        filterEmail = null;
        filterUserType = null;
        filterActive = null;
        filteredUsers = null;
    }

    // ========== MÉTODOS DE MENSAGEM ==========

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, summary, detail));

        // Para atualizar componentes específicos
        PrimeFaces.current().ajax().update(":messages");
    }

    // public void createAvatar() {
    // Integer userId = loggedUser.getPkUser(); //

    // PersonDTO.PersonResponseDTO person = personService.findByUserId(userId);

    // if (person != null) {
    // this.firstName = person.getFirstName();
    // this.lastName = person.getLastName();
    // this.image_person = person.getImagePerson();
    // } else {
    // this.image_person = "default.png";
    // }
    // }

    /*
     * Obsolete method for permission autocomplete
     */
    // public List<Permission> completePermission(String query) {
    // return new ArrayList<Permission>();
    // }

    // ========== GETTERS E SETTERS ==========

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getNewUserEmail() {
        return newUserEmail;
    }

    public void setNewUserEmail(String newUserEmail) {
        this.newUserEmail = newUserEmail;
    }

    public String getNewUserPassword() {
        return newUserPassword;
    }

    public void setNewUserPassword(String newUserPassword) {
        this.newUserPassword = newUserPassword;
    }

    public String getNewUserConfirmPassword() {
        return newUserConfirmPassword;
    }

    public void setNewUserConfirmPassword(String newUserConfirmPassword) {
        this.newUserConfirmPassword = newUserConfirmPassword;
    }

    public Integer getNewUserFkPerson() {
        return newUserFkPerson;
    }

    public void setNewUserFkPerson(Integer newUserFkPerson) {
        this.newUserFkPerson = newUserFkPerson;
    }

    public Integer getNewUserFkUserType() {
        return newUserFkUserType;
    }

    public void setNewUserFkUserType(Integer newUserFkUserType) {
        this.newUserFkUserType = newUserFkUserType;
    }

    public boolean isNewUserActive() {
        return newUserActive;
    }

    public void setNewUserActive(boolean newUserActive) {
        this.newUserActive = newUserActive;
    }

    public String getNewUserDeviceToken() {
        return newUserDeviceToken;
    }

    public void setNewUserDeviceToken(String newUserDeviceToken) {
        this.newUserDeviceToken = newUserDeviceToken;
    }

    public UserDTO.UserResponseDTO getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UserDTO.UserResponseDTO selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getEditUserEmail() {
        return editUserEmail;
    }

    public void setEditUserEmail(String editUserEmail) {
        this.editUserEmail = editUserEmail;
    }

    public String getEditUserDeviceToken() {
        return editUserDeviceToken;
    }

    public void setEditUserDeviceToken(String editUserDeviceToken) {
        this.editUserDeviceToken = editUserDeviceToken;
    }

    public boolean isEditUserActive() {
        return editUserActive;
    }

    public void setEditUserActive(boolean editUserActive) {
        this.editUserActive = editUserActive;
    }

    public Integer getEditUserFkUserType() {
        return editUserFkUserType;
    }

    public void setEditUserFkUserType(Integer editUserFkUserType) {
        this.editUserFkUserType = editUserFkUserType;
    }

    public List<UserDTO.UserResponseDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO.UserResponseDTO> users) {
        this.users = users;
    }

    public List<UserDTO.UserResponseDTO> getFilteredUsers() {
        if (filteredUsers != null) {
            return filteredUsers;
        }
        return users;
    }

    public void setFilteredUsers(List<UserDTO.UserResponseDTO> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public UserDTO.UserResponseDTO getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(UserDTO.UserResponseDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String getFilterEmail() {
        return filterEmail;
    }

    public void setFilterEmail(String filterEmail) {
        this.filterEmail = filterEmail;
    }

    public Integer getFilterUserType() {
        return filterUserType;
    }

    public void setFilterUserType(Integer filterUserType) {
        this.filterUserType = filterUserType;
    }

    public Boolean getFilterActive() {
        return filterActive;
    }

    public void setFilterActive(Boolean filterActive) {
        this.filterActive = filterActive;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isLoginDialogVisible() {
        return loginDialogVisible;
    }

    public void setLoginDialogVisible(boolean loginDialogVisible) {
        this.loginDialogVisible = loginDialogVisible;
    }

    public boolean isRegisterDialogVisible() {
        return registerDialogVisible;
    }

    public void setRegisterDialogVisible(boolean registerDialogVisible) {
        this.registerDialogVisible = registerDialogVisible;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public String getNewMiddleName() {
        return newMiddleName;
    }

    public void setNewMiddleName(String newMiddleName) {
        this.newMiddleName = newMiddleName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage_person() {
        return image_person;
    }

    public void setImage_person(String image_person) {
        this.image_person = image_person;
    }

    /*public List<PersonResponseDTO> getPersonCache() {
        return personCache;
    }

    public void setPersonCache(List<PersonResponseDTO> personCache) {
        this.personCache = personCache;
    }

    public PersonResponseDTO getSelectedPersonDTO() {
        return selectedPersonDTO;
    }

    public void setSelectedPersonDTO(PersonResponseDTO selectedPersonDTO) {
        this.selectedPersonDTO = selectedPersonDTO;
    }*/

    // public Permission getSelectedPermission() {
    // return selectedPermission;
    // }

    // public void setSelectedPermission(Permission selectedPermission) {
    // this.selectedPermission = selectedPermission;
    // }

    public List<Integer> getSelectedPermissionIds() {
        return selectedPermissionIds;
    }

    public void setSelectedPermissionIds(List<Integer> selectedPermissionIds) {
        this.selectedPermissionIds = selectedPermissionIds;
    }

    // public List<Permission> getAvailablePermissions() {
    // return availablePermissions;
    // }

    // public void setAvailablePermissions(List<Permission> availablePermissions) {
    // this.availablePermissions = availablePermissions;
    // }

    public Integer getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(Integer selectedUserId) {
        this.selectedUserId = selectedUserId;
    }
}