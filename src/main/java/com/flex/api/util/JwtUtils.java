package com.flex.api.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.flex.api.exception.ServerSideException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtUtils {
	
	public static String createToken(Map<String, Object> claims, String secretKey, long ttl) {
		Date iat = new Date(System.currentTimeMillis());
		Date exp = new Date(iat.getTime() + ttl * 1000);
		
		return createToken(claims, secretKey, iat, exp);
	}
	
	public static String createToken(Map<String, Object> claims, String secretKey, Date iat, Date exp) {
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("alg", "HS256");
		header.put("typ", "JWT");
		
		try {
			return Jwts.builder()
				.setHeader(header)
				.addClaims(claims)
				.setSubject("user")
				.setIssuedAt(iat)
				.setExpiration(exp)
				.signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
				.compact();
		} catch (UnsupportedEncodingException e) {
			throw new ServerSideException("JWT", "Create Token Failed", null);
		}
	}
	
	public static Claims getTokenClaims(String authToken, String secretKey) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		return Jwts.parser()
				.setSigningKey(secretKey.getBytes("UTF-8"))
				.parseClaimsJws(authToken)
				.getBody();
	}

}
