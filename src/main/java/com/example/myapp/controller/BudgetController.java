package com.example.myapp.controller;

import com.example.myapp.common.Entity;
import com.example.myapp.common.MofConstants;
import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.context.GenericRequestContext;
import com.example.myapp.context.GenericRequestContextHolder;
import com.example.myapp.dto.*;
import com.example.myapp.model.Budget;
import com.example.myapp.model.Friends;
import com.example.myapp.response.ApiResponse;
import com.example.myapp.service.BudgetService;
import com.example.myapp.service.FriendService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @Autowired
    BudgetService budgetService;

    private static final PrettyLogger logger = PrettyLogger.getLogger(BudgetController.class);

    @SuppressWarnings("unused")
    @PostMapping("/create")
    public ApiResponse<Budget> createFriend(@Valid @RequestBody CreateBudgetRequest createBudgetRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Creating a new Budget");
        ctx.put(MofConstants.CREATED_ENTITY_TYPE, Entity.BUDGET);
        Budget newBudget = budgetService.createBudget(createBudgetRequest);
        logger.info("Created new Budget:- " + newBudget.toString());
        ctx.put(MofConstants.CREATED_ENTITY_ID, newBudget.getId());
        return ApiResponse.success(newBudget, "Budget Created Successfully", ctx.getTraceId());
    }

    @SuppressWarnings("unused")
    @PostMapping("/get-budgetOfMonth")
    public ApiResponse<List<BudgetResponse>> getBudgetOfMonth(@Valid @RequestBody GetBudgetofMonthYearRequest getBudgetofMonthYearRequest) {
        GenericRequestContext ctx = GenericRequestContextHolder.get();
        logger.info("Getting All Budgets for the user of particular month");
        List<BudgetResponse> bugetList = budgetService.getBudgetOfCurrentMonths(getBudgetofMonthYearRequest);
        logger.info("Fetched budgets");
        return ApiResponse.success(bugetList, "Budget list fetched successfully", ctx.getTraceId());
    }
}
