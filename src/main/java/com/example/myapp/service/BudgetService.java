package com.example.myapp.service;

import com.example.myapp.config.logging.PrettyLogger;
import com.example.myapp.dto.BudgetResponse;
import com.example.myapp.dto.CreateBudgetRequest;
import com.example.myapp.dto.GetBudgetofMonthYearRequest;
import com.example.myapp.exception.InvalidDataException;
import com.example.myapp.model.Budget;
import com.example.myapp.model.Scope;
import com.example.myapp.model.User;
import com.example.myapp.repository.BudgetRepository;
import com.example.myapp.repository.ScopeRepository;
import com.example.myapp.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {
    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    ScopeRepository scopeRepository;

    @Autowired
    ProjectUtils projectUtils;

    private static final PrettyLogger logger = PrettyLogger.getLogger(BudgetService.class);

    public Budget createBudget(CreateBudgetRequest createBudgetRequest) {
        User user = projectUtils.getUserFromToken();

        Scope scope = scopeRepository.findByScopeIdAndUserId(createBudgetRequest.scopeId(), user.getId());

        if(scope == null){
            throw new InvalidDataException("The given scope is not exist");
        }

        Budget budget = new Budget();

        budget.setUser(user);
        budget.setBudgetMonth(createBudgetRequest.budgetMonth());
        budget.setBudgetYear(createBudgetRequest.budgetYear());
        budget.setAllocatedBudget(createBudgetRequest.allocatedBudget());
        budget.setRemainingBudget(createBudgetRequest.allocatedBudget());
        budget.setUsedBudget(0.0);
        budget.setScope(scope);

        logger.info("saving the budget in the database");
        Budget savedBudget = budgetRepository.save(budget);

        logger.info("lets update the scope");

        scope.setOverAllBudgetAllocated(scope.getOverAllBudgetAllocated() != null ? scope.getOverAllBudgetAllocated() + createBudgetRequest.allocatedBudget() : createBudgetRequest.allocatedBudget());
        scope.setOverAllRemainingBudget(scope.getOverAllRemainingBudget() != null ? scope.getOverAllRemainingBudget() + createBudgetRequest.allocatedBudget() : createBudgetRequest.allocatedBudget());
        scopeRepository.save(scope);
        return savedBudget;
    }

    public List<BudgetResponse> getBudgetOfCurrentMonths(GetBudgetofMonthYearRequest getBudgetofMonthYearRequest) {
        long currentMonth = getBudgetofMonthYearRequest.budgetMonth();
        long currentYear = getBudgetofMonthYearRequest.budgetYear();

        User user = projectUtils.getUserFromToken();

        // Assuming you have a BudgetRepository with a custom method
        List<Budget> currentMonthBudget = budgetRepository.findByBudgetMonthAndBudgetYear((long) currentMonth, (long) currentYear, user.getId());

        return currentMonthBudget.stream()
                .map(budget -> new BudgetResponse(
                        budget.getBudgetMonth(),
                        budget.getBudgetYear(),
                        budget.getAllocatedBudget(),
                        budget.getRemainingBudget(),
                        budget.getUsedBudget(),
                        budget.getScope().getId()
                )).toList();
    }
}
