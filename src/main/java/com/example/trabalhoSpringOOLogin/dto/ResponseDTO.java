package com.example.trabalhoSpringOOLogin.dto;

/**
 * Record que representa a resposta padrão para operações de autenticação bem-sucedidas.
 * Contém informações básicas do usuário e o token de acesso gerado.
 * 
 * @param name Nome do usuário autenticado (para exibição)
 * @param token Token JWT gerado para autorização nas requisições subsequentes
  Estrutura imutável que garante:
  - Segurança nos dados transmitidos
  - Consistência na resposta da API
  - Facilidade de serialização/deserialização JSON
 */
public record ResponseDTO(String name, String token) {
}