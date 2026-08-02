package com.SistemSchool.config;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    @Inject
    private SessionBean sessionBean;

    // Prefixo de rota -> regra de acesso (avaliada sobre o SessionBean)
    private static final Map<String, Predicate<SessionBean>> MODULE_RULES = new LinkedHashMap<>();

    static {
        MODULE_RULES.put("/settings/", sb -> sb.isAdmin());
        MODULE_RULES.put("/management/secretaria/", sb -> sb.isAdmin() || sb.isSecretary());
        MODULE_RULES.put("/management/financeiro/", sb -> sb.isAdmin() || sb.isFinancial());
        MODULE_RULES.put("/management/pedagogico/", sb -> sb.isAdmin() || sb.isPedagogical());
    }

    private static final String[] PUBLIC_ROUTES = {
            "/login.xhtml",
            "/sign_in.xhtml",
            "/access-denied.xhtml"
    };

    private static final String[] PUBLIC_PREFIXES = {
            "/resources/",
            "/javax.faces.resource/",
            "/jakarta.faces.resource/"
    };

    @Override
    public void init(FilterConfig filterConfig) {
        // nada a inicializar
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contextPath = request.getContextPath();
        String path = request.getRequestURI().substring(contextPath.length());

        if (isPublicRoute(path)) {
            chain.doFilter(req, res);
            return;
        }

        if (sessionBean == null || !sessionBean.isLoggedIn()) {
            logger.warn("Acesso negado (não autenticado) a {}", path);
            response.sendRedirect(contextPath + "/login.xhtml");
            return;
        }

        // ADMIN tem acesso irrestrito a todos os módulos
        if (sessionBean.isAdmin()) {
            chain.doFilter(req, res);
            return;
        }

        for (Map.Entry<String, Predicate<SessionBean>> rule : MODULE_RULES.entrySet()) {
            if (path.startsWith(rule.getKey()) && !rule.getValue().test(sessionBean)) {
                logger.warn("Acesso negado ao módulo {} para usuário com perfil {}",
                        rule.getKey(),
                        sessionBean.getLoggedUser() != null ? sessionBean.getLoggedUser().getPerfil() : "N/A");
                response.sendRedirect(contextPath + "/access-denied.xhtml");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    private boolean isPublicRoute(String path) {
        for (String route : PUBLIC_ROUTES) {
            if (path.equals(route)) {
                return true;
            }
        }
        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        // nada a limpar
    }
}