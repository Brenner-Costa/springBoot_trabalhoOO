package com.example.trabalhoSpringOOLogin.infra.security;

import com.example.trabalhoSpringOOLogin.domain.User;
import com.example.trabalhoSpringOOLogin.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Serviço customizado para carregar detalhes do usuário durante a autenticação.
 * Implementa a interface UserDetailsService do Spring Security.
 */
@Component // Componente gerenciado pelo Spring
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository repository; // Repositório para acesso aos dados do usuário

    /**
     * Carrega os detalhes do usuário baseado no username (email no caso).
     * 
     * @param username O email do usuário a ser autenticado
     * @return UserDetails contendo informações necessárias para autenticação
     * @throws UsernameNotFoundException Se o usuário não for encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.repository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // Cria e retorna um UserDetails do Spring Security com:
        // - email como username
        // - senha codificada
        // - lista vazia de authorities (roles/permissões) -> Náo possui roles para fins de simplificacao
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(), 
            user.getPassword(), 
            new ArrayList<>()
        );
    }
}