package com.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.security.model.AppResponse;
import com.security.utils.JwtUtils;
import com.security.utils.KEY;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.SignatureException;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    private MyUserDetailsService userDetailsService;

    private final JwtUtils jwtUtils;
    private final MyUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationFilter(JwtUtils jwtUtils, MyUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    Map<String, Claim> claims = new HashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(KEY.AUTHORIZATION == "Authorization" ? "Authorization" : "authorization");
        String userName = null;

        if (token != null && !request.getMethod().equalsIgnoreCase("OPTIONS")) {

            DecodedJWT jwt = JWT.decode(token);

            if (jwt != null) {

                claims = jwt.getClaims();
                userName = claims.get("username").asString();

                if (jwtUtils.isTokenExpired2(jwt)) {
                    allowCrossOrigin(request, response, filterChain);
                    sendErrorResponse(response, HttpStatus.valueOf(response.SC_UNAUTHORIZED), "Token Expired");
                    return;
                }

                if (!hasValidClaims()) {
                    allowCrossOrigin(request, response, filterChain);
                    sendErrorResponse(response, HttpStatus.valueOf(response.SC_UNAUTHORIZED), "Invalid token.");
                    return;
                }

                System.out.println("getAuthentication :: " + SecurityContextHolder.getContext().getAuthentication());

                if (userName != null ) {
                    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(claims.get("role").asString()));
//                    final Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("role").toString().split(","))
//                            .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        allowCrossOrigin(request, response, filterChain);
        filterChain.doFilter(request, response);
    }

    private void allowCrossOrigin(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "authorization, Authorization, content-type, xsrf-token, uid");
        response.addHeader("Access-Control-Expose-Headers", "Authorization, xsrf-token, uid, cid, token");
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) response.setStatus(HttpServletResponse.SC_OK);
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        AppResponse errorResponse = new AppResponse(status, message);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(errorResponse.toJson2());
    }

    private boolean hasValidClaims() {

        if (claims != null && claims.containsKey("jti")) {
            return true;
        }

        return false;
    }


}
