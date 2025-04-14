package com.example.trabalhoSpringOOLogin.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe principal de configuração de segurança do Spring Security.
 * Define políticas de autenticação, autorização e configuração de filtros.
 */

@Configuration // Configurações do Spring
@EnableWebSecurity // Habilita a segurança web personalizada
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService; 

    @Autowired
    SecurityFilter securityFilter; // Filtro de segurança customizado

    /**
     * Configura a cadeia principal de filtros de segurança.
     * 
     * @param http Configuração de segurança HTTP
     * @return SecurityFilterChain configurado
     * @throws Exception
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF (comum em APIs stateless)
                .csrf(csrf -> csrf.disable())
                
                // Configura sessão como stateless (não mantém estado) -. Padrao em API Restful
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // Configura regras de autorização
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público aos endpoints de login e registro
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        
                        // Todos os outros endpoints requerem autenticação -> Para fortalecer a seguranca dos endpoints
                        .anyRequest().authenticated()
                )
                
                // Adiciona filtro de segurança antes do filtro padrão de autenticação
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    /**
     * Configura o codificador de senhas (usado para hash de senhas).
     * 
     * @return Instância de BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Expõe o AuthenticationManager como um bean gerenciado pelo Spring.
     * 
     * @param authenticationConfiguration Configuração de autenticação
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
