package com.example.trabalhoSpringOOLogin.dto;

/**
 * Record que representa os dados de requisição para autenticação (login).
 * - Recebido como corpo da requisição no endpoint de login
 * - Contém as credenciais básicas para autenticação
    Possui automaticamente:
    - Construtor com todos campos
    - Métodos getters (email(), password())
    - equals(), hashCode() e toString()
 */

public record LoginRequestDTO(String email, String password) {
}