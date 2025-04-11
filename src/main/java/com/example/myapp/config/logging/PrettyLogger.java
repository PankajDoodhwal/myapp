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
}


