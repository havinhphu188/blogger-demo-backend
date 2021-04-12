package com.example.bloggerdemo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Log4j2
public class JwtTokenUtil{
	private long jwtTokenValidity;
	@Value("${jwt.secret}")
	private String secret;

	@PostConstruct
	public void init(){
		jwtTokenValidity = 24 * 60 * 60L;
	}

	public String getIdFromToken(String token) {
		return getClaimFromToken(token, Claims::getId);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	public String generateToken(int userId) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userId);
	}

	private String doGenerateToken(Map<String, Object> claims, int userId) {
		return Jwts.builder().setClaims(claims).setId(String.valueOf(userId)).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			log.info("Invalid JWT token.");
			log.trace("Invalid JWT token trace.", e);
		}
		return false;
	}

}