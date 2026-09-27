package sn.banque1.banque1_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class InternalApiKeyFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-Internal-Api-Key";
    private static final List<String> PROTECTED_PATTERNS = List.of(
            "/api/comptes/authenticate",
            "/api/transactions/paiement-externe",
            "/api/transactions/paiement-externe/status"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Value("${internal.api.key}")
    private String internalApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        boolean isProtected = PROTECTED_PATTERNS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));

        if (isProtected && !internalApiKey.equals(request.getHeader(HEADER))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Clé API interne invalide ou manquante");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
