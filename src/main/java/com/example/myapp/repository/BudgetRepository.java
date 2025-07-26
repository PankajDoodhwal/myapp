package com.example.myapp.repository;

import com.example.myapp.model.Budget;
import com.example.myapp.model.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    String getAllBudgetByBudgetMonthAndBudgetYearQuery = "SELECT b.* from budget b WHERE b.budget_month = :budgetMonth AND b.budget_year = :budgetYear AND b.user_id = :userId";
    @Query(value = getAllBudgetByBudgetMonthAndBudgetYearQuery, nativeQuery = true)
    List<Budget> findByBudgetMonthAndBudgetYear(@Param("budgetMonth") long currentMonth, @Param("budgetYear") long currentYear, @Param("userId") Long userId);

    String getBudgetByMonthYearScopeAndUserQuery = "SELECT b.* from budget b WHERE b.budget_month = :budgetMonth AND b.budget_year = :budgetYear AND b.user_id = :userId AND b.scope_id = :scopeId";
    @Query(value = getBudgetByMonthYearScopeAndUserQuery, nativeQuery = true)
    Budget getBudgetByMonthYearScopeAndUser(@Param("budgetMonth") int budgetMonth, @Param("budgetYear") int budgetYear, @Param("scopeId") Long scopeId, @Param("userId") Long userId);
}
