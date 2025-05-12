package com.example.myapp.controller;

import com.example.myapp.common.Entity;
import com.example.myapp.common.MofConstants;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.dto.CreateAccountRequest;
import com.example.myapp.model.Account;
import com.example.myapp.response.ApiResponse;
import com.example.myapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    private static final PrettyLogger logger = PrettyLogger.getLogger(AccountController.class);

    @SuppressWarnings("unused")
    @PostMapping("/create-account")
    public ApiResponse<Account> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Creating a new Account");
        ctx.put(MofConstants.CREATED_ENTITY_TYPE, Entity.ACCOUNT);
        Account newAccount = accountService.createAccount(createAccountRequest);
        logger.info("Created new Account:- " + newAccount.toString());
        ctx.put(MofConstants.CREATED_ENTITY_ID, newAccount.getId());
        return ApiResponse.success(newAccount, "Account Created Successfully", ctx.getTraceId());
    }

    @SuppressWarnings("unused")
    @GetMapping("/get-all-accounts")
    public ApiResponse<List<Account>> getAllAccountsByUser(){
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Fetching all the accounts for user");
        List<Account> accountList = accountService.getAllAccountByUser();
        logger.info("Fetched All the accounts:- ");
        return ApiResponse.success(accountList, "Fetched All the account successfully", ctx.getTraceId());
    }
}
