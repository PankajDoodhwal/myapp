package com.example.myapp.config.filter;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.controller.UserController;
import com.example.myapp.wrapper.CachedBodyHttpServletRequest;
import com.example.myapp.wrapper.CachedBodyHttpServletResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ContextFilter implements Filter {
    private static final PrettyLogger logger = PrettyLogger.getLogger(ContextFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(httpRequest);
        CachedBodyHttpServletResponse wrappedResponse = new CachedBodyHttpServletResponse(httpResponse);

        try {
            GenericRequestContext context = new GenericRequestContext();
            GenericRequestContextHolder.set(context);

            // Store request body
            String requestBody = new String(wrappedRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            context.put("REQUEST_BODY", requestBody);

            chain.doFilter(wrappedRequest, wrappedResponse); // Process request

            // Store response body
            String responseBody = wrappedResponse.getBodyAsString();
            context.put("RESPONSE_BODY", responseBody);

            // Log everything
            Logger flowLogger = LoggerFactory.getLogger("RequestTraceLogger");
            StringBuilder logOutput = new StringBuilder();
            logOutput.append("<api_name>\n");
            logOutput.append("    (" + context.getApiName() + ")\n");
            logOutput.append(context.getFormattedLogs());
            logOutput.append(context.getFormattedContext());
            logOutput.append(context.getFormattedSqlLogs());
            logOutput.append(context.getFormattedTrace());
            logOutput.append("</api_name>\n");
            flowLogger.info(logOutput.toString());

            // Finally, write cached response body back to actual response
            wrappedResponse.copyBodyToResponse();

        } finally {
            GenericRequestContextHolder.clear();
        }
    }


}

