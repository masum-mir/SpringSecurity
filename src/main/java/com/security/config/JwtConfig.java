package com.security.config;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.security.model.User;
import com.security.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtConfig extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MyUserDetailsService userDetailsService;

    Map<String, Claim> claims = new HashMap<>();

    User user;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String userName = null;
        String token = null;

        if (authorizationHeader != null) {
            token = authorizationHeader.trim();

            try {
                userName = jwtUtils.extractusername(token);
            } catch (IllegalArgumentException e) {
                logger.error("An error occurred while fetching username from token: ", e);
            } catch (ExpiredJwtException e) {
                logger.warn("The token has expired", e);
            } catch (SignatureException e) {
                logger.error("Authentication Failed. Username or Password not valid.");
            }

//            DecodedJWT jwt = jwtUtils.getDecodedToken(token.toString());
//
//            if (jwt != null) {
//
//                claims = jwt.getClaims();
//                System.out.println("claims::: " + claims);
//
//                if (isTokenExpired(jwt)) {
//
//                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                    response.getWriter().write("{\"status\":401, \"body\":null, \"message\": \"Token expired\"}");
//                    allowCrossOrigin(request, response, filterChain);
//                    return;
//                }
//
//                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//
//                    if (jwtUtils.isTokenValid(token, userDetails)) {
//                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                    }
//                }
//            }
        }

        System.out.println("test::: ");
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

            if (jwtUtils.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

//        allowCrossOrigin(request, response, filterChain);
        filterChain.doFilter(request, response);
    }

    private void allowCrossOrigin(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Request-Headers");
    }

//    private boolean isTokenExpired(DecodedJWT jwt) {
//        return jwt != null && jwt.getExpiresAt().before(new Date());
//    }
//
//    private User retriveUser(DecodedJWT jwt) {
//
////        if(hasKey("user_id")) user.setUser_id(claims.get("user_id").as(Long.class));
//        if (hasKey("username")) user.setUsername(claims.get("sub").asString());
//        if (hasKey("email")) user.setEmail(claims.get("aud").asString());
//
//        return user;
//    }
//
//    private boolean hasKey(String key) {
//        return !noData(claims) && claims.containsKey(key);
//    }
//
//    private boolean noData(Object obj) {
//        return obj == null;
//    }

}
