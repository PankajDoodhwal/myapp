package com.example.myapp.service;

import com.example.myapp.common.MofConstants;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
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


    public RequestLog logStart(String traceId, String method, String endpoint, GenericRequestContext ctx){
        logger.info("Create start log of the request");

        RequestLog log = new RequestLog();

        log.setTraceId(traceId);
        log.setMethod(method);
        log.setEndPoint(endpoint);
        log.setTimeStamp(LocalDateTime.now());

        return requestLogRepository.save(log);
    }

    public void logEnd(RequestLog log, int status, String responseBody, boolean success, GenericRequestContext ctx) {
        logger.info("Create end log of the request");

        log.setResponseStatus(status);
        log.setDurationMs(java.time.Duration.between(log.getTimeStamp(), LocalDateTime.now()).toMillis());
        log.setSuccess(success);

        Object createdEntityType = ctx.get(MofConstants.CREATED_ENTITY_TYPE);
        Object createdEntityId = ctx.get(MofConstants.CREATED_ENTITY_ID);

        if(createdEntityType != null && createdEntityId != null){
            log.setEntityType(createdEntityType.toString());
            log.setEntityId(createdEntityId.toString());
        }

        requestLogRepository.save(log);
    }
}
