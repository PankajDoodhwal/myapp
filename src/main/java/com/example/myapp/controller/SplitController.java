package com.example.myapp.controller;

import com.example.myapp.common.Entity;
import com.example.myapp.common.MofConstants;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.dto.CreateSplitRequest;
import com.example.myapp.dto.SplitResponse;
import com.example.myapp.dto.UpdateSplitStatusRequest;
import com.example.myapp.model.SplitTransaction;
import com.example.myapp.response.ApiResponse;
import com.example.myapp.service.SplitTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/split")
public class SplitController {
    @Autowired
    SplitTransactionService splitTransactionService;

    private static final PrettyLogger logger = PrettyLogger.getLogger(SplitController.class);

    @SuppressWarnings("unused")
    @PostMapping("/splite-transaction")
    public ApiResponse<List<SplitTransaction>> splitTransaction(@Valid @RequestBody CreateSplitRequest createSplitRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Spliting a new transaction");
        ctx.put(MofConstants.CREATED_ENTITY_TYPE, Entity.SCOPE);
        List<SplitTransaction> savedSplitTransactions = splitTransactionService.splitTransactions(createSplitRequest);
        logger.info("Splited a new transaction:- " + savedSplitTransactions.toString());
        logger.info("Splitted the transaction between multiple friends so we are not putting id in logs table");
        return ApiResponse.success(savedSplitTransactions, "Splitted Transction Successfully", ctx.getTraceId());
    }

    @SuppressWarnings("unused")
    @GetMapping("/get-all-splits")
    public ApiResponse<List<SplitResponse>> getAllSplitsByUser() {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Getting All Splits for the user");
        List<SplitResponse> splitResponseList = splitTransactionService.getAllSplits();
        logger.info("Fetched all Splites");
        return ApiResponse.success(splitResponseList, "Split list fetched successfully", ctx.getTraceId());
    }

    @GetMapping("/get-split-by_txnid")
    public ApiResponse<List<SplitResponse>> getSplitByTxnId(@RequestParam("txnId") Long txnId) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Getting All Splits for the transaction id");
        List<SplitResponse> splitResponseList = splitTransactionService.getAllSplitsWithTxnId(txnId);
        logger.info("Fetched all Splites for the transaction");
        return ApiResponse.success(splitResponseList, "Split list for transaction fetched successfully", ctx.getTraceId());
    }

    @PostMapping("/update-splits")
    public ApiResponse<List<SplitResponse>> updateSplitStatus(@Valid @RequestBody UpdateSplitStatusRequest updateSplitStatusRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Updating all the status of transaction");
        List<SplitResponse> splitResponseList = splitTransactionService.updateSplitStatus(updateSplitStatusRequest);
        logger.info("Fetched all Splites for the transaction");
        return ApiResponse.success(splitResponseList, "Split list for transaction fetched successfully", ctx.getTraceId());
    }
}
