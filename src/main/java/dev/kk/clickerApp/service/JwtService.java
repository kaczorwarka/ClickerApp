package dev.kk.clickerApp.service;


import dev.kk.clickerApp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public String getUserName(String jwtToken){
        return getClaim(jwtToken, Claims::getSubject);
    }

    public String generateToken(User user){
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignIngKey())
                .compact(); //ompact generuje i zwraca token
    }

    public boolean isTokenValid(String jwtToken, User user) {
        final String username = getUserName(jwtToken);
        return (username.equals(user.getUsername())) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        return getClaim(jwtToken, Claims::getExpiration).before(new Date());
    }

    public <T> T getClaim(String jwtToken, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String jwtToke){
        return Jwts.parser()
                .verifyWith(getSignIngKey()) //Podis dla tokena zeby sprawdzić czy jest git i ten klucz działa se z algorytmem szyfrowania
                .build()
                .parseSignedClaims(jwtToke)
                .getPayload();
    }

    private SecretKey getSignIngKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
