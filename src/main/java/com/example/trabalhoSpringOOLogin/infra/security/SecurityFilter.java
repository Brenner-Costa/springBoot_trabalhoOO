package com.example.trabalhoSpringOOLogin.infra.security;

import com.example.trabalhoSpringOOLogin.domain.*;
import com.example.trabalhoSpringOOLogin.repositories.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Filtro de segurança personalizado que processa tokens JWT.
 * Executado uma vez por requisição para autenticação baseada em token.
 */
@Component 
public class SecurityFilter extends OncePerRequestFilter { // Executado uma vez por requisição

    @Autowired
    TokenService tokenService; // Serviço para validação de tokens
    
    @Autowired
    UserRepository userRepository;

    /**
     * Método principal que implementa a lógica do filtro.
     * 
     * @param request Requisição HTTP
     * @param response Resposta HTTP
     * @param filterChain Cadeia de filtros
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) 
        throws ServletException, IOException {
        
        // Recupera o token do cabeçalho Authorization
        var token = this.recoverToken(request);
        
        // Valida o token e extrai o login (email)
        var login = tokenService.validateToken(token);

        if(login != null){

            User user = userRepository.findByEmail(login)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
            
            // Cria lista de authorities (roles) - padrão ROLE_USER
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            
            // Cria objeto de autenticação do Spring Security
            var authentication = new UsernamePasswordAuthenticationToken(
                user, // Principal
                null, // Credenciais (não necessárias após autenticação)
                authorities // Roles/permissões
            );
            
            // Define o contexto de segurança
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Recupera o token JWT do cabeçalho Authorization.
     * 
     * @param request Requisição HTTP
     * @return Token JWT (sem o prefixo "Bearer") ou null se não existir
     */
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", ""); // Remove o prefixo "Bearer"
    }
}