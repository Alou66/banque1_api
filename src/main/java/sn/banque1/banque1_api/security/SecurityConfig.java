package sn.banque1.banque1_api.security;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtService jwtService;
        private final CustomUserDetailsService customUserDetailsService;
        private final InternalApiKeyFilter internalApiKeyFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/comptes").permitAll()
                                                // Protégé par InternalApiKeyFilter, pas par JWT (appelé par
                                                // auth_api, service à service)
                                                .requestMatchers(HttpMethod.POST, "/api/comptes/authenticate")
                                                .permitAll()
                                                // Protégé par InternalApiKeyFilter, pas par JWT (appelé par
                                                // gestion_service_api, service à service)
                                                .requestMatchers(HttpMethod.POST, "/api/transactions/paiement-externe")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(internalApiKeyFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(
                                                new JwtAuthenticationFilter(jwtService, customUserDetailsService),
                                                UsernamePasswordAuthenticationFilter.class)
                                .build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // Origines de dev uniquement (Flutter Web / desktop) : les clients mobiles
        // (Android/iOS) ne sont pas soumis à CORS, seul le navigateur l'applique.
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}