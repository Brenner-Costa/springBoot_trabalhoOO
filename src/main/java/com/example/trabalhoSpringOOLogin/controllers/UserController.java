package com.example.trabalhoSpringOOLogin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador responsável por operações relacionadas a usuários.
 * Todas as rotas começam com /user.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * Endpoint básico para teste de acesso à rota de usuário.
     * 
     * Exemplo de uso:
     * GET /user → retorna "sucesso!"
     */
    @GetMapping
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Usuário logado com sucesso!");
    }
}