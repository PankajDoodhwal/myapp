package com.example.myapp.controller;

import com.example.myapp.common.Entity;
import com.example.myapp.common.MofConstants;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.dto.CreateTransactionRequest;
import com.example.myapp.dto.TransactionResponse;
import com.example.myapp.model.Transaction;
import com.example.myapp.response.ApiResponse;
import com.example.myapp.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    private static final PrettyLogger logger = PrettyLogger.getLogger(TransactionController.class);

    @SuppressWarnings("unused")
    @PostMapping("/create")
    public ApiResponse<Transaction> createTransaction(@Valid @RequestBody CreateTransactionRequest createTransactionRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Creating a new transaction");
        ctx.put(MofConstants.CREATED_ENTITY_TYPE, Entity.TRANSACTION);
        Transaction newTransaction = transactionService.createNewTransaction(createTransactionRequest);
        logger.info("Created New Transaction:- " + newTransaction);
        ctx.put(MofConstants.CREATED_ENTITY_ID, newTransaction.getId());
        return ApiResponse.success(newTransaction, "Successfully created new Transaction", ctx.getTraceId());
    }

    @SuppressWarnings("unused")
    @PostMapping("/get-all-transactions")
    public ApiResponse<List<TransactionResponse>> getAllTransaction() {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Fetching all the transactions");
//        ctx.put(MofConstants.CREATED_ENTITY_TYPE, Entity.TRANSACTION);
        List<TransactionResponse> allTransactions = transactionService.fetchAllTheTransactions();
        logger.info("Fetched all the Transactiona:- " );
//        ctx.put(MofConstants.CREATED_ENTITY_ID, newTransaction.getId());
        return ApiResponse.success(allTransactions, "Successfully fetched all Transactions", ctx.getTraceId());
    }
}
