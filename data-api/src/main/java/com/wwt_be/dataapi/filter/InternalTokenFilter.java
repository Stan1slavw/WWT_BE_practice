package com.wwt_be.dataapi.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

    @Component
    public class InternalTokenFilter extends OncePerRequestFilter {
        @Value("${app.internal-token}") String expected;

        @Override
        protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
                throws ServletException, IOException {
            if (req.getRequestURI().startsWith("/api/transform")) {
                var header = req.getHeader("X-Internal-Token");
                if (header == null || !header.equals(expected)) {
                    res.setStatus(HttpStatus.FORBIDDEN.value());
                    res.setContentType("application/json");
                    res.getWriter().write("{\"error\":\"forbidden\"}");
                    return;
                }
            }
            chain.doFilter(req, res);
        }
    }
