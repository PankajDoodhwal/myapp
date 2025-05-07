package com.example.myapp.controller;

import com.example.myapp.common.Entity;
import com.example.myapp.common.MofConstants;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.dto.ScopeRequest;
import com.example.myapp.model.Scope;
import com.example.myapp.response.ApiResponse;
import com.example.myapp.service.ScopeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scope")
public class ScopeController {
    @Autowired
    private ScopeService scopeService;

    private static final PrettyLogger logger = PrettyLogger.getLogger(ScopeController.class);


    @PostMapping("/create")
    public ApiResponse<Scope> createScope(@Valid @RequestBody ScopeRequest scopeRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Creating a new Scope");
        ctx.put(MofConstants.CREATED_ENTITY_TYPE, Entity.SCOPE);
        Scope newScope = scopeService.createScope(scopeRequest);
        logger.info("Created new scope:- " + newScope.toString());
        ctx.put(MofConstants.CREATED_ENTITY_ID, newScope.getId());
        return ApiResponse.success(newScope, "Scope Created Successfully", ctx.getTraceId());
    }
}
