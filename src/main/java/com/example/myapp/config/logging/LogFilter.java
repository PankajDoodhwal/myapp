package com.example.myapp.config.logging;

import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.controller.UserController;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class LogFilter implements Filter {
    private static final PrettyLogger logger = PrettyLogger.getLogger(LogFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        GenericRequestContext ctx = GenericRequestContextHolder.get();

        logger.info("LogFiter:-------------------------");

        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId); // store in thread-local
        ctx.put("traceId", ctx.getTraceId());

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("traceId"); // always clean up!
        }
    }
}

