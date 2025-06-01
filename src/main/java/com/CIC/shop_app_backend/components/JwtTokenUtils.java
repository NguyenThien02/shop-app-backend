package com.CIC.shop_app_backend.components;

import com.CIC.shop_app_backend.entity.User;
import com.CIC.shop_app_backend.exceptions.DataInvalidParamException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

//  private String securityKey = generateSecretKey();
    @Value("${jwt.securityKey}")
    private String securityKey;

    @Value("${jwt.expiration}")
    private int expiration;

    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }
    public String generateToken(User user) throws DataInvalidParamException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
//        generateSecretKey();
        try{
            String toke = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return toke;
        } catch (Exception e) {
            throw new DataInvalidParamException("Cannot create jwt token, error: " + e.getMessage());
        }
    }

    private Key getSignInKey(){
        byte[] bytes = Decoders.BASE64.decode(securityKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    // Hàm trích xuất tất cả các claims từ token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // Hàm lấy một claim cụ thể từ token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Hàm kiểm tra token đã hết hạn chưa
    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    // Trích xuất ra phoneNumber
    public String extractPhoneNumber(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()) &&
                !isTokenExpired(token));
    }
}
