package com.example.mobileappws3.shared;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.mobileappws3.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class Utils {
	
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
    // generate User id
    public String generateUserId(int length) {
        return generateRandomString(length);
    }
    
    //  generate Address id
    public String generateAddressId(int length) {
        return generateRandomString(length);
    }
    
    // generate RANDOM String
    private String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnValue);
        
    }
    

   //  check if token has expired
	public static boolean hasTokenExpired(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConstants.getTokenSecret())
				.parseClaimsJws(token).getBody();
		
		Date tokenExpirationDate = claims.getExpiration();
		Date todayDate = new Date();

		return tokenExpirationDate.before(todayDate);
	}


	// generate Email Verification Token
	public String generateEmailVerificationToken(String userId) {
		String token = Jwts.builder()
				.setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
        return token;
		
		
	}

	public String generatePasswordResetToken(String userId) {
		
		String token = Jwts.builder()
				.setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
        return token;
		
	}
    
}
