package com.example.myapp.service;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.controller.UserController;
import com.example.myapp.model.RequestLog;
import com.example.myapp.repository.RequestLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RequestLogService {
    @Autowired
    private RequestLogRepository requestLogRepository;

    private static final PrettyLogger logger = PrettyLogger.getLogger(RequestLogService.class);


    public RequestLog logStart(String traceId, String method, String endpoint){
        logger.info("Create start log of the request");

        RequestLog log = new RequestLog();

        log.setTraceId(traceId);
        log.setMethod(method);
        log.setEndPoint(endpoint);
        log.setTimeStamp(LocalDateTime.now());

        return requestLogRepository.save(log);
    }

    public void logEnd(RequestLog log, int status, String responseBody, boolean success) {
        logger.info("Create end log of the request");

        log.setResponseStatus(status);
        log.setDurationMs(java.time.Duration.between(log.getTimeStamp(), LocalDateTime.now()).toMillis());
        log.setSuccess(success);
        requestLogRepository.save(log);
    }
}
