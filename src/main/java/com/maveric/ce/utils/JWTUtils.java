package com.maveric.ce.utils;

import com.maveric.ce.dto.UserDetailsImpl;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.response.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {
	@Value("${jwt.expirationDateInMs}")
	String sessionExpireTime;

	static String apiAccessKey;

	@Value("${apiAccessKey}")
	private void activeProfile(String key) {
		apiAccessKey = key;
	};


	public LoginResponse generateToken(UserDetailsImpl userDetails, HttpServletRequest request,
									   LoginResponse loginResponse) throws ServiceException {
		try {
			Map<String, Object> claims = new HashMap<>();
			String token = Jwts.builder().setClaims(claims).setSubject(userDetails.getEmailId())
					.setIssuedAt(new Date(System.currentTimeMillis())).setIssuer(request.getRequestURL().toString())
					.setExpiration(new Date(System.currentTimeMillis() + Integer.parseInt(sessionExpireTime)))
					.claim("roles", userDetails.getAuthorities()).claim("userName", userDetails.getUsername())
					.signWith(SignatureAlgorithm.HS256, apiAccessKey).compact();
			loginResponse.setAccessToken(token);
			loginResponse.setRefreshToken(generateRefershToken(userDetails, request));
		}catch (Exception e){
			e.getStackTrace();
			throw new ServiceException(ErrorCodes.Server_Expection);
		}
		return loginResponse;

	}

	public String generateRefershToken(UserDetails userDetails, HttpServletRequest request) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis())).setIssuer(request.getRequestURL().toString())
				.setExpiration(new Date(System.currentTimeMillis() + Integer.parseInt(sessionExpireTime)))
				.claim("roles", userDetails.getAuthorities()).signWith(SignatureAlgorithm.HS256, apiAccessKey).compact();
	}

	public static String extractUserMailId(String token) throws ServiceException {
		try {
			token = token.substring(7);
			Claims claims = Jwts.parserBuilder().setSigningKey(apiAccessKey).build().parseClaimsJws(token).getBody();
			return claims.getSubject();
		} catch (Exception e) {
			System.out.println("inside cht");
			throw new ServiceException(ErrorCodes.INVALID_TOKEN);
		}
	}

	public static String extractRole(String token) {
		try {
			System.out.println("serectKey-- "+apiAccessKey);
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(apiAccessKey).build().parseClaimsJws(token);
			return claims.getBody().get("roles").toString();
		} catch (Exception e) {
			throw new ServiceException(ErrorCodes.INVALID_TOKEN);

		}
	}

	public boolean isTokenValid(String token, UserDetailsImpl userDetails) {
		try {
			String userMailId = extractUserMailId(token);
			token = token.substring(7);
			Claims claims = Jwts.parserBuilder().setSigningKey(apiAccessKey).build().parseClaimsJws(token).getBody();
			Date expireTime = claims.getExpiration();
			boolean checkUserName = userMailId.equals(userDetails.getEmailId());
			boolean notExpireDate = expireTime.after(new Date());
			return checkUserName && notExpireDate;
		} catch (Exception e) {
			throw new ServiceException(ErrorCodes.INVALID_TOKEN);

		}
	}

}
