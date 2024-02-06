package com.task.vendingmachine.Configuration;

import com.task.vendingmachine.Models.User;
import com.task.vendingmachine.Repositories.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final String SECRET="a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7";
    private final UserRepo userRepo;
    public String generateToken(User user){
        final int validity =1000*60*60*24*3;

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity )) //valid for 3 days
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }
    public String getUsernameFromToken (String token){

        return extractClaim(token,Claims::getSubject);
    }
    public User getUserFromToken (String token){
        String username=extractClaim(token,Claims::getSubject);
        return userRepo.findByUsername(username).orElse(null);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String username= getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return getExpiryDate(token).before(new Date(System.currentTimeMillis()));
    }

    public Date getExpiryDate(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims,T> function){
        final Claims claims=getAllClaims(token);
        return function.apply(claims);
    }
    public Claims getAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);

        return Keys.hmacShaKeyFor(keyBytes);
    }

}

