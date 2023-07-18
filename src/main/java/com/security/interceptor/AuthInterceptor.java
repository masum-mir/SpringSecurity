package com.security.interceptor;

import com.security.utils.ENV;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        System.out.println("Pre handle is calling!!!");

        try {

            final Claims claims = Jwts.parser().setSigningKey(ENV.getToken()).parseClaimsJws(request.getHeader("TOKEN")).getBody();

            request.setAttribute("SESSION_NO", claims.getId());
            request.setAttribute("SESSION_USER_NO", claims.getIssuer());
            System.out.println("Tokken::: :: " + ENV.getToken());
            System.out.println("Tokken::: :: " + claims.getSubject());
            return true;

        } catch (Exception e) {
            response.setStatus(response.SC_PRECONDITION_FAILED);
            response.setHeader("Error: ", e.getMessage());
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {


    }


}
