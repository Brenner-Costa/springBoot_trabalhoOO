package com.example.trabalhoSpringOOLogin.controllers;

import com.example.trabalhoSpringOOLogin.domain.*;
import com.example.trabalhoSpringOOLogin.dto.*;
import com.example.trabalhoSpringOOLogin.infra.security.*;
import com.example.trabalhoSpringOOLogin.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controlador responsável pelas operações de autenticação (login e registro).
 * Todas as rotas começam com /auth.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor 
public class AuthController {
    // Repositório para operações com usuários no banco de dados
    private final UserRepository repository;
    
    // Codificador de senhas para segurança
    private final PasswordEncoder passwordEncoder;
    
    // Serviço para geração e validação de tokens JWT
    private final TokenService tokenService;

    /**
     * Endpoint para autenticação de usuários existentes.
     * 
     * @param body DTO contendo email e senha do usuário
     * @return ResponseEntity com nome do usuário e token JWT em caso de sucesso,
     *         ou erro 400 em caso de falha
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        
        User user = this.repository.findByEmail(body.email())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verifica se a senha corresponde
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            // Gera token JWT para o usuário
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * Endpoint para registro de novos usuários.
     * 
     * @param body DTO contendo nome, email e senha do novo usuário
     * @return ResponseEntity com nome do usuário e token JWT em caso de sucesso,
     *         ou erro 400 se o email já estiver cadastrado
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        // Verifica se o email já está cadastrado
        Optional<User> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()) {
            // Cria novo usuário
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password())); // Codifica a senha
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            
            // Salva no banco de dados
            this.repository.save(newUser);

            // Gera token JWT para o novo usuário
            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        }

        return ResponseEntity.badRequest().build();
    }
}

