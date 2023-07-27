package com.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.security.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.security.MessageDigest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtUtils {

    @Autowired
    private HttpServletRequest servletRequest;

    public String extractusername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaim(token);
        return claimsTFunction.apply(claims);
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(),user);
    }

    public String generateToken(Map<String, Object> extrClaims, User user) {
        return Jwts.builder().setClaims(extrClaims)
                .setSubject("Token Testing")
                .setAudience("Token Audience")
                .setId(user.getUser_id().toString())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("number", user.getNumber())
                .claim("role", user.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken2(User user) {
        JwtBuilder builder = Jwts.builder().setId(String.valueOf(user.getUser_id()))
                .setIssuer("Test_USER")
                .setAudience("TEST_PROJECT")
                .setSubject(user.getUsername())
                .claim("id", user.getUser_id())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("phone", user.getNumber())
                .claim("role", user.getRole())
                .claim("token_initial", SHA256(getIpProxyIp(servletRequest) + getClientInfo(servletRequest)))
                .setIssuedAt(Calendar.getInstance().getTime()).setNotBefore(Calendar.getInstance().getTime());

        String token = builder.signWith(SignatureAlgorithm.HS512, getJwtKey()).compact();

        return token;
    }

    public Key getJwtKey() {
        return new SecretKeySpec(EnvConfig.getString("TOKEN_PRIVATE_KEY", ENV.getToken()).getBytes(),
                SignatureAlgorithm.HS512.getJcaName());
    }

    public static String getIpProxyIp(HttpServletRequest servletRequest) {
        String remoteAddr = servletRequest.getHeader("X-FORWARDED-FOR");
        if (remoteAddr == null || "".equals(remoteAddr))
            remoteAddr = servletRequest.getRemoteAddr();

        return remoteAddr;
    }

    public static String getClientInfo(HttpServletRequest servletRequest) {
        return servletRequest.getHeader("User-Agent") == null ? "" : servletRequest.getHeader("User-Agent");
    }

    public static String SHA256(String plainText) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plainText.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public String generateRefreshToken(UserDetails userDetails) {
        return builderToken(new HashMap<>(), userDetails, 604800000);
    }

    public String builderToken(Map<String, Object> params, UserDetails userDetails, long expiration) {
        return Jwts.builder().setClaims(params).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractusername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaim(String token) {
//        byte[] getSignInKey = new byte[0];
         Claims claim = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

        return claim;
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(ENV.getToken());
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean isTokenExpired2(DecodedJWT decodedJWT) {
        return null != decodedJWT && decodedJWT.getExpiresAt().before(new Date());
    }

    public DecodedJWT getDecodedToken(String token) {
        DecodedJWT decodedJWT = null;
        try {
            if (StringUtils.isEmpty(token)) return null;
            decodedJWT = JWT.decode(token);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return decodedJWT;
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(final String token, final Authentication authentication, final UserDetails userDetails) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(ENV.getToken());

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

}
