package com.example.trabalhoSpringOOLogin.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um usuário no sistema.
 * Mapeada para a tabela "users" no banco de dados.
 */

@Entity // Indica que esta classe é uma entidade JPA
@Table(name = "users") // Especifica o nome da tabela no banco de dados
@Getter // Gera automaticamente os getters (Lombok)
@Setter // Gera automaticamente os setters (Lombok)
@AllArgsConstructor // Gera um construtor com todos os argumentos (Lombok)
@NoArgsConstructor // Gera um construtor vazio (Lombok)
public class User {
    
    /**
     * Identificador único do usuário.
     * Gerado automaticamente como UUID.
     */
    @Id // Indica que este é o campo chave primária
    @GeneratedValue(strategy = GenerationType.UUID) // Gera UUID automático
    private String id;
    private String name;
    private String email;
    private String password;
}