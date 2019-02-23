package com.bosto.atmapi.security;

/**
 * Created by bosto on 2018/12/21
 **/

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bosto.atmapi.exception.InitAlgorithmTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Date;
import java.util.Map;
import static com.bosto.atmapi.common.Maps.*;


public class JwtToken {
	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String RESPONSE_TOKEN = "token";

	private static final String SECRET = System.getProperty("jwt.privateKey","jwtsecret");
	private static final String ISS = "hskj";
	private static final String ROLE_CLAIMS = "rol";

	// 过期时间是3600秒，既是1个小时
	private static final long EXPIRATION = 3600L;

	// 选择了记住我之后的过期时间为7天
	private static final long EXPIRATION_REMEMBER = 604800L;

	private static final JWTVerifier verifier = JWT.require(initAlgorithm(SECRET)).build();

	private static final Logger logger = LoggerFactory.getLogger(JwtToken.class);



	public static String createToken(String subject, String roles, boolean isRememberMe){
		long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
		return createToken(subject, roles, expiration);
	}

	public static String createToken(String subject, String roles, long expireDuration){
		return createToken(subject, map(ROLE_CLAIMS, roles), expireDuration);
	}


	public static String createToken(String subject, Map<String, String> claims, long expireDuration){
		JWTCreator.Builder builder = JWT.create();
		if (claims != null) {
			for (Map.Entry<String, String> entry : claims.entrySet()) {
				builder.withClaim(entry.getKey(), entry.getValue());
			}
		}

		return builder
				.withIssuer(ISS)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + expireDuration * 1000))
				.withSubject(subject)
				.sign(initAlgorithm(SECRET));
	}

	public static String createToken(String subject, JWTCreator.Builder builder, long expireDuration){
//		JWTCreator.Builder builder = JWT.create();
//		if (claims != null) {
//			for (Map.Entry<String, String> entry : claims.entrySet()) {
//				builder.withClaim(entry.getKey(), entry.getValue());
//			}
//		}

		return builder
				.withIssuer(ISS)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + expireDuration * 1000))
				.withSubject(subject)
				.sign(initAlgorithm(SECRET));
	}
//	public static void verifyToken(String key, String token)  {
//		JWTVerifier verifier = JWT.require(initAlgorithm(key))
//				.build();
//		DecodedJWT jwt = verifier.verify(token);
//		Map<String, Claim> claims = jwt.getClaims();
//		if (System.currentTimeMillis() > Long.valueOf(claims.get("expired").asString()) ){
//			throw new TokenExpireException("Token expired");
//		}
//		logger.info(claims.get("name").asString() + " verifyToken success");
//	}

	private static Algorithm initAlgorithm(String key) {

		Algorithm algorithm = null;
		try {
			algorithm =  Algorithm.HMAC512(key);
		} catch (Exception e) {
			throw new InitAlgorithmTokenException("Init algorithm fail");
		}
		return algorithm;
	}

	public static String getUsername(String token){
		return getTokenBody(token).getSubject();
	}

	public static String getSubject(String token){
		return getTokenBody(token).getSubject();
	}

	public static String getRoleClaims(String token){
		return getTokenBody(token).getClaim(ROLE_CLAIMS).asString();
	}

	// 是否已过期
	public static boolean isExpiration(String token){
		return getTokenBody(token).getNotBefore().before(new Date());
	}

	public static DecodedJWT getTokenBody(String token){
		DecodedJWT result;
		try {
			result = verifier.verify(token);
		} catch (Exception jwtException) {
			throw new BadCredentialsException(jwtException.getMessage());
		}
		return result;
	}

	public static boolean verifyToken(String token) {
		return getTokenBody(token) != null;
	}

}
