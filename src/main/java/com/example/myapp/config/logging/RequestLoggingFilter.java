package com.example.myapp.config.logging;

import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.controller.UserController;
import com.example.myapp.model.RequestLog;
import com.example.myapp.service.RequestLogService;
import com.example.myapp.wrapper.CachedBodyHttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Autowired
    private RequestLogService requestLogService;

    private static final PrettyLogger logger = PrettyLogger.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        GenericRequestContext ctx = GenericRequestContextHolder.get();

        logger.info("requestLoggingFiler");

        String traceId = ctx.getTraceId();
        String method = request.getMethod();
        String endpoint = request.getRequestURI();

        RequestLog log = requestLogService.logStart(traceId, method, endpoint);

        // Capture response with wrapper
        CachedBodyHttpServletResponse wrappedResponse = new CachedBodyHttpServletResponse(response);

        try {
            filterChain.doFilter(request, wrappedResponse);
        } finally {
            int status = wrappedResponse.getStatus();
            String respBody = new String(wrappedResponse.getCaptureAsBytes(), response.getCharacterEncoding());
            requestLogService.logEnd(log, status, respBody, status >= 200 && status < 400);
            wrappedResponse.copyBodyToResponse();
        }
    }
}
