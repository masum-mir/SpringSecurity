package com.security.config;

import com.security.model.AppResponse;
import com.security.utils.JwtUtils;
import com.security.utils.KEY;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(KEY.AUTHORIZATION);
        String userName = null;
        String token = null;

        if (authorizationHeader != null) {
            token = authorizationHeader.trim();

            try {
                userName = jwtUtils.extractusername(token);
            } catch (IllegalArgumentException e) {
                logger.error("An error occurred while fetching username from token: ", e);
            } catch (ExpiredJwtException e) {
                logger.warn("The token has expired!!!");
                sendErrorResponse(response, HttpStatus.valueOf(response.SC_UNAUTHORIZED), "Token Expired!");
                return;
            } catch (SignatureException e) {
                logger.error("Authentication Failed. Username or Password not valid.");
            }
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if (jwtUtils.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
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

}
