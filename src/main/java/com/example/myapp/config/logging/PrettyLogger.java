package com.example.myapp.config.logging;

import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrettyLogger {
    private final Logger logger;
    private final String className;

    private PrettyLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
        this.className = clazz.getSimpleName();
    }

    public static PrettyLogger getLogger(Class<?> clazz) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        if (ctx != null) {
            ctx.markClass(clazz.getSimpleName());
        }
        return new PrettyLogger(clazz);
    }

    public void info(String message) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        if (ctx != null) {
            ctx.addLog(className, message); // Only storing now
        }
        logger.info(message); // Optional if you want real-time too
    }

    public void error(String message) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        if (ctx != null) {
            ctx.addLog(className, "[ERROR] " + message);
        }
        logger.error(message);
    }

    public void error(String message, Throwable t) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        if (ctx != null) {
            ctx.addLog(className, "[ERROR] " + message + " - Exception: " + t.getMessage());
        }
        logger.error(message, t);
    }

    public void error(String format, Object... args) {
        String formattedMessage = formatMessage(format, args);

        GenericRequestContext ctx = GenericRequestContextHolder.get();
        if (ctx != null) {
            ctx.addLog(className, "[ERROR] " + formattedMessage);
        }
        logger.error(format, args);
    }

    private String formatMessage(String format, Object... args) {
        try {
            return java.text.MessageFormat.format(format.replace("{}", "'{'0'}'"), args);
        } catch (Exception e) {
            return format; // fallback in case formatting fails
        }
    }
}


