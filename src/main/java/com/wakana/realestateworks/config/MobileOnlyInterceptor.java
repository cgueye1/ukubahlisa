package com.wakana.realestateworks.config;
/* 
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MobileOnlyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        // Appliquer uniquement au pointage
        if (!request.getRequestURI().contains("/check")) {
            return true;
        }

        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null ||
                !(userAgent.contains("iPhone")
                        || userAgent.contains("Android")
                        || userAgent.contains("okhttp")
                        || userAgent.contains("Dart"))) {

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Accès réservé à l’application mobile");
            return false;
        }

        return true;
    }
}
*/