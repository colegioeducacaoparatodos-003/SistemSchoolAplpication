# SistemSchool

<ui:composition xmlns="http://www.w3.org/1999/xhtml" xmlns:h="http://xmlns.jcp.org/jsf/html"
    xmlns:ui="http://xmlns.jcp.org/jsf/facelets" xmlns:p="http://primefaces.org/ui"
    xmlns:f="http://xmlns.jcp.org/jsf/core">

    <style>
        /* Estilização e transições do rodapé da sidebar */
        .sidebar-user-card {
            transition: all 0.3s ease;
            
        }

        .sidebar-user-card .sidebar-user-trigger:hover {
            background-color: rgba(128, 0, 32, 0.05) !important;
        }

        .sidebar-user-card.open .sidebar-user-dropdown-item:hover {
            background-color: #800020 !important;
            color: #FFFFFF !important;
        }

        .sidebar-user-card.open .sidebar-user-dropdown-item:hover i,
        .sidebar-user-card.open .sidebar-user-dropdown-item:hover span {
            color: #FFFFFF !important;
        }

        /* Ícones em amarelo dourado */
        .sidebar .pi,
        .sidebar-user-card .pi {
            color: #FFD700 !important;
        }

        /* Ajuste específico para ícones de logout ou texto padrão para não perder contraste se necessário */
        .sidebar-user-dropdown-item.text-danger:hover i,
        .sidebar-user-dropdown-item.text-danger:hover span {
            color: #FFFFFF !important;
        }
    </style>

    <script type="text/javascript">
        function toggleSidebar() {
            document.querySelector('.dashbord-layout').classList.toggle('sidebar-visible');
        }
    </script>

    <div class="sidebar d-flex flex-column h-100 shadow-sm bg-white">
        <div class="flex-grow-1 overflow-y-auto">

            <!-- Cabeçalho da Sidebar -->
            <div class="sidebar-header p-3 d-flex align-items-center justify-content-between border-bottom">
                <h:link
                    outcome="#{sessionBean.isAdmin() ? '/dashboard.xhtml' : '/management/secretaria/dashboard.xhtml'}"
                    styleClass="d-flex align-items-center gap-2 text-decoration-none">
                    <div class="sidebar-brand-badge d-flex align-items-center justify-content-center text-white rounded p-2"
                        style="background-color: #800020; min-width: 38px; min-height: 38px;">
                        <i class="pi pi-building fs-5"></i>
                    </div>
                    <div class="d-flex flex-column">
                        <span class="fs-6 fw-bold text-dark lh-sm">SistemSchool</span>
                        <small class="text-muted" style="font-size: 0.75rem;">#{msg.schoolManagement}</small>
                    </div>
                </h:link>
                <!-- Botão de fechar em mobile -->
                <button type="button" class="btn btn-close d-lg-none shadow-none" aria-label="Close"
                    onclick="toggleSidebar()"></button>
            </div>

            <h:form styleClass="mt-2 px-2">
                <p:menu styleClass="admin-sidebar-menu border-0 w-100 bg-transparent">

                    <!-- Principal: visível para todos os perfis autenticados -->
                    <p:menuitem styleClass="sidebar-menu-header fw-bold text-uppercase text-muted px-2 py-1 mt-2"
                        value="#{msg.main}" icon="pi pi-home" disabled="true" />
                    <p:menuitem value="#{msg.dashboard}"
                        outcome="#{sessionBean.isAdmin() ? '/dashboard.xhtml' : '/management/secretaria/dashboard.xhtml'}"
                        icon="pi pi-th-large" />

                    <!-- Secretaria: SECRETARY e ADMIN -->
                    <p:menuitem styleClass="sidebar-menu-header fw-bold text-uppercase text-muted px-2 py-1 mt-3"
                        value="#{msg.secretariat}" icon="pi pi-briefcase" disabled="true"
                        rendered="#{sessionBean.isSecretary() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.schoolClass}" action="#{schoolClassController.load}" icon="pi pi-inbox"
                        rendered="#{sessionBean.isSecretary() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.student}" action="#{studentController.loadStudents}" icon="pi pi-users"
                        rendered="#{sessionBean.isSecretary() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.enrollments}" action="#{enrolmentController.load}" icon="pi pi-id-card"
                        rendered="#{sessionBean.isSecretary() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.documents}" action="#{documentController.load}" icon="pi pi-folder-open"
                        rendered="#{sessionBean.isSecretary() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.payment}" action="#{pagamentoController.load}" icon="pi pi-dollar"
                        rendered="#{sessionBean.isSecretary() or sessionBean.isAdmin()}" />

                    <!-- Pedagógico: PEDAGOGICAL e ADMIN -->
                    <p:menuitem styleClass="sidebar-menu-header fw-bold text-uppercase text-muted px-2 py-1 mt-3"
                        value="#{msg.pedagogical}" icon="pi pi-book" disabled="true"
                        rendered="#{sessionBean.isPedagogical() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.discipline}" action="#{disciplineController.load}" icon="pi pi-bookmark"
                        rendered="#{sessionBean.isPedagogical() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.schedule}" action="#{scheduleController.load}" icon="pi pi-book"
                        rendered="#{sessionBean.isPedagogical() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.grades}" action="#{gradeController.load}" icon="pi pi-graduation-cap"
                        rendered="#{sessionBean.isPedagogical() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.evaluation}" action="#{evaluationController.load}"
                        icon="pi pi-check-square" rendered="#{sessionBean.isPedagogical() or sessionBean.isAdmin()}" />

                    <!-- Financeiro: FINANCIAL e ADMIN -->
                    <p:menuitem styleClass="sidebar-menu-header fw-bold text-uppercase text-muted px-2 py-1 mt-3"
                        value="#{msg.financial}" icon="pi pi-wallet" disabled="true"
                        rendered="#{sessionBean.isFinancial() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.fee}" action="#{feeController.load}" icon="pi pi-tag"
                        rendered="#{sessionBean.isFinancial() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.cashBox}" action="#{cashBoxController.load}" icon="pi pi-inbox"
                        rendered="#{sessionBean.isFinancial() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.payment}" action="#{pagamentoController.load}" icon="pi pi-dollar"
                        rendered="#{sessionBean.isFinancial() or sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.financialMovement}" action="#{financialMovementController.load}"
                        icon="pi pi-chart-line" rendered="#{sessionBean.isFinancial() or sessionBean.isAdmin()}" />

                    <!-- Recursos Humanos: apenas ADMIN -->
                    <p:menuitem styleClass="sidebar-menu-header fw-bold text-uppercase text-muted px-2 py-1 mt-3"
                        value="#{msg.humanResources}" icon="pi pi-users" disabled="true"
                        rendered="#{sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.teachers}" action="#{teacherController.loadTeachers}" icon="pi pi-users"
                        rendered="#{sessionBean.isAdmin()}" />
                    <p:menuitem value="#{msg.users}" outcome="/settings/users.xhtml" icon="pi pi-user-plus"
                        rendered="#{sessionBean.isAdmin()}" />

                </p:menu>
            </h:form>
        </div>

        <!-- Rodapé do Usuário na Sidebar Estilizado e Responsivo -->
        <div class="sidebar-user-card p-3 border-top bg-light">
            <button type="button"
                class="sidebar-user-trigger btn w-100 d-flex align-items-center justify-content-between p-2 rounded border-0 bg-transparent shadow-none"
                title="#{msg.toggleUserMenu}" aria-label="#{msg.toggleUserMenu}" aria-haspopup="true"
                aria-expanded="false"
                onclick="this.closest('.sidebar-user-card').classList.toggle('open'); this.setAttribute('aria-expanded', this.closest('.sidebar-user-card').classList.contains('open'));">
                <div class="d-flex align-items-center gap-2 text-start" style="overflow: hidden;">
                    <div class="sidebar-user-avatar rounded-circle bg-secondary text-white d-flex align-items-center justify-content-center flex-shrink-0"
                        style="width: 35px; height: 35px;">
                        <i class="pi pi-user"></i>
                    </div>
                    <div class="d-flex flex-column" style="min-width: 0;">
                        <span class="fw-semibold text-truncate text-dark lh-sm" style="font-size: 0.85rem;">
                            <h:outputText value="#{sessionBean.loggedUser.email}" />
                        </span>
                        <small class="text-muted text-truncate" style="font-size: 0.75rem;">
                            <h:outputText value="#{msg[sessionBean.loggedUser.perfil.toString()]}" />
                        </small>
                    </div>
                </div>
                <i class="pi pi-chevron-down sidebar-user-trigger-icon text-muted flex-shrink-0 ms-2"
                    aria-hidden="true"></i>
            </button>

            <div class="sidebar-user-dropdown mt-2 pt-2 border-top d-none">
                <h:link outcome="/profile.xhtml"
                    styleClass="sidebar-user-dropdown-item d-flex align-items-center gap-2 py-2 px-2 rounded text-dark text-decoration-none transition-base">
                    <i class="pi pi-user" aria-hidden="true"></i>
                    <span style="font-size: 0.9rem;">#{msg.profile}</span>
                </h:link>
                <h:link outcome="/status.xhtml"
                    styleClass="sidebar-user-dropdown-item d-flex align-items-center gap-2 py-2 px-2 rounded text-dark text-decoration-none mt-1 transition-base">
                    <i class="pi pi-info-circle" aria-hidden="true"></i>
                    <span style="font-size: 0.9rem;">#{msg.status}</span>
                </h:link>
                <a href="#"
                    class="sidebar-user-dropdown-item d-flex align-items-center gap-2 py-2 px-2 rounded text-danger text-decoration-none mt-1 transition-base"
                    onclick="document.getElementById('logoutForm:logoutButton').click(); return false;">
                    <i class="pi pi-sign-out text-danger" aria-hidden="true"></i>
                    <span style="font-size: 0.9rem;">#{msg.logout}</span>
                </a>
            </div>
        </div>
    </div>

    <h:form id="logoutForm" style="display:none;">
        <h:commandButton id="logoutButton" value="Logout" action="#{userController.logout}" />
    </h:form>

</ui:composition>