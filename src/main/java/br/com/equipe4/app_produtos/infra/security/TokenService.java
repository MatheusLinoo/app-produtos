package br.com.equipe4.app_produtos.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.equipe4.app_produtos.model.User;

import org.springframework.beans.factory.annotation.Value;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("AppProdutos")
                    .withSubject(user.getId().toString())
                    .withClaim("nome", user.getName())
                    .withClaim("login", user.getEmail())
                    .withExpiresAt(genTokenExpirationDate())
                    .sign(algorithm);

            return token;

        }catch (JWTCreationException e ){

            throw new RuntimeException("Error generating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("AppProdutos")
                .build()
                .verify(token)
                .getSubject();
        } catch (Exception e){
            return null;
        }
    }

    private Instant genTokenExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}