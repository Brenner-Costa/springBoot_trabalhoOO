package com.example.trabalhoSpringOOLogin.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.trabalhoSpringOOLogin.domain.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Serviço responsável pela geração e validação de tokens JWT.
 * Utiliza a biblioteca java-jwt (auth0) para manipulação dos tokens.
 */

@Service 
public class TokenService {
    
    @Value("${api.security.token.secret}") // Injeta a chave secreta do application.properties
    private String secret; // Chave secreta para assinatura dos tokens

    /**
     * Gera um token JWT para o usuário.
     * 
     * @param user Usuário para quem o token será gerado
     * @return String contendo o token JWT
     * @throws RuntimeException Se houver erro na geração do token
     */

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); // Algoritmo de criptografia

            String token = JWT.create()
                    .withIssuer("login-auth-api") // Emissor do token
                    .withSubject(user.getEmail()) // Subject (normalmente username/email)
                    .withExpiresAt(this.generateExpirationDate()) // Data de expiração
                    .sign(algorithm); // Assina o token
            
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while authenticating");
        }
    }

    /**
     * Valida um token JWT e retorna o subject (email) se válido.
     * 
     * @param token Token JWT a ser validado
     * @return String com o email do usuário (subject) ou null se token inválido
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api") // Verifica o emissor
                    .build()
                    .verify(token) // Valida o token
                    .getSubject(); // Retorna o email (subject)
        } catch (JWTVerificationException exception) {
            return null; // Token inválido ou expirado
        }
    }

    /**
     * Gera a data de expiração do token (2 horas a partir do momento atual).
     * 
     * @return Instant representando a data/hora de expiração
     */
    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(2) // Token expira em 2 horas
                .toInstant(ZoneOffset.of("-03:00"));
    }
}