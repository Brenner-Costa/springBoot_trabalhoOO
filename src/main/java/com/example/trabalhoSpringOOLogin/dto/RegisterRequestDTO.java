package com.example.trabalhoSpringOOLogin.dto;

/**
 * Record que representa os dados necessários para registrar um novo usuário no sistema.
 * Contém as informações básicas para criação de uma conta de usuário.
 *
 * @param name Nome completo do usuário
 * @param email E-mail que será usado para login (deve ser único no sistema)
 * @param password Senha para autenticação (será criptografada antes do armazenamento)
 */
public record RegisterRequestDTO(String name, String email, String password) {
}