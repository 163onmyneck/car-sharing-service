package car.sharing.app.carsharingservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${jwt.expiration}")
    private Long expirationTime;
    private Key secret;
    @Value("${jwt.secret}")
    private String secretString;

    @PostConstruct
    private void setSecret() {
        secret = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secret)
                .compact();
    }

    public boolean isValidToken(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token);
        return claimsJws.getBody().getExpiration().after(new Date(System.currentTimeMillis()));
    }

    public String getUsernameByToken(String token) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return body.getSubject();
    }
}
