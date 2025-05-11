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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scope")
public class ScopeController {
    @Autowired
    ScopeService scopeService;

    private static final PrettyLogger logger = PrettyLogger.getLogger(ScopeController.class);

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    @GetMapping("/get-all-scopes")
    public ApiResponse<List<Scope>> getScopeByUser() {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Getting All scopes for the user");
        List<Scope> scopeList = scopeService.getAllScopeByUser();
        logger.info("Fetched scopeList");
        return ApiResponse.success(scopeList, "Scope list fetched successfully", ctx.getTraceId());
    }
}
