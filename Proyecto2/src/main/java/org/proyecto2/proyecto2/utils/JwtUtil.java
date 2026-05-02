package org.proyecto2.proyecto2.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.proyecto2.proyecto2.models.usuario.Usuario;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {
    // Clave secreta usada para firmar y verificar el token
    private static final String SECRET = "clave-super-secreta-para-jwt-que-debe-ser-larga-256bits!!";

    // Tiempo de vida del token: 2 horas en milisegundos.
    private static final long EXPIRATION_MS = 1000L * 60 * 60 * 2;

    // Construimos la clave criptográfica a partir del string secreto.
    // HMAC-SHA256 necesita al menos 256 bits (32 bytes).
    private static final SecretKey KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * Genera un JWT firmado para el usuario dado.
     *
     * @param username  nombre de usuario que se incluirá como "subject" del token
     * @return          string con el JWT listo para enviar al cliente
     */
    public static String generarToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getUserName())                          // quien es el dueño del token
                .claim("usuarioId", usuario.getUsuarioId())
                .claim("rol", usuario.getRol().name())
                .issuedAt(new Date())                       // fecha de creación
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS)) // fecha de expiración
                .signWith(KEY)                              // firma con HS256
                .compact();                                 // construye el string final
    }

    /**
     * Valida el token y extrae el username (subject).
     *
     * Si el token está mal formado, expirado o la firma no coincide,
     * lanza una JwtException que el filtro debe capturar.
     *
     * @param token  el JWT sin el prefijo "Bearer "
     * @return       el username almacenado en el subject del token
     * @throws JwtException  si el token es inválido o expirado
     */
    public static Claims validarYObtenerUsername(String token) {
        // parseSignedClaims verifica la firma y la expiración automáticamente.
        // Si algo falla, lanza una excepción de la familia JwtException.
        return Jwts.parser()
                .verifyWith(KEY)          // indica con qué clave verificar la firma
                .build()
                .parseSignedClaims(token) // parsea y valida en un solo paso
                .getPayload();         // extrae el "subject" (username)
    }
}
