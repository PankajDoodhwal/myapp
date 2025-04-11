package com.example.myapp.config.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId); // store in thread-local

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("traceId"); // always clean up!
        }
    }
}

