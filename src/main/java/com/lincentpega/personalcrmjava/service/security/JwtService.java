package com.lincentpega.personalcrmjava.service.security;

import com.lincentpega.personalcrmjava.domain.account.Account;
import com.lincentpega.personalcrmjava.domain.account.AccountRole;
import com.lincentpega.personalcrmjava.domain.account.RefreshToken;
import com.lincentpega.personalcrmjava.service.security.result.RefreshTokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static final String TOKEN_ID_CLAIM = "tokenId";
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.access-token-expiration-time}")
    private long accessTokenExpiration;
    @Value("${security.jwt.refresh-token-expiration-time}")
    private long refreshTokenExpiration;
    @Value("${security.jwt.audience}")
    private String audience;

    public String extractUsername(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public RefreshTokenInfo extractRefreshTokenInfo(String token) {
        Claims claims = parseToken(token);
        var username = claims.getSubject();
        var refreshTokenId = claims.get(TOKEN_ID_CLAIM, Long.class);
        return new RefreshTokenInfo(username, refreshTokenId);
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(Account account) {
        return Jwts.builder()
                .setAudience(audience)
                .setSubject(account.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .claim("roles", account.getRoles().stream().map(AccountRole::getName).toList())
                .setExpiration(Date.from(Instant.now().plus(accessTokenExpiration, ChronoUnit.SECONDS)))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(RefreshToken refreshToken) {
        var account = refreshToken.getAccount();
        return Jwts.builder()
                .setAudience(audience)
                .setSubject(account.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(refreshTokenExpiration, ChronoUnit.SECONDS)))
                .claim(TOKEN_ID_CLAIM, refreshToken.getId())
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
