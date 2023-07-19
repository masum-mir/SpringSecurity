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

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtConfig implements Filter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init method call::;"+filterConfig);
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        /*Enumeration<String> headerNames = httpRequest.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerKey = headerNames.nextElement();
                System.out.println("Header ==> " + headerKey + ":" + httpRequest.getHeader(headerKey));
            }
        }*/


        String username = null;

        if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"status\": 200, \"body\": \"OK\", \"message\": \"SUCCESS\"}");
        }

        String token = httpRequest.getHeader("authorization");
        if (token == null || token.trim().isEmpty()) {
            token = httpRequest.getHeader("Authorization");
        }

        if (token != null && !token.trim().isEmpty()) {
            try {
                username = jwtUtils.extractusername(token);
            } catch (ExpiredJwtException e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"status\": 401, \"body\": null, \"message\": \"Token Expired\"}");
                return;
            }
        }

        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtils.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

//            }

//            System.out.println(token);
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            response.getWriter().write("{\"status\": 200, \"body\": \"OK\", \"message\": \"SUCCESS\"}");

        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("destroy method call");
        Filter.super.destroy();
    }
}
