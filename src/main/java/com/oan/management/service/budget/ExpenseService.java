package com.oan.management.service.budget;

import com.oan.management.model.Budget;
import com.oan.management.model.Expense;

import java.util.List;

/**
 * Created by Oan on 12/02/2018.
 */
public interface ExpenseService {
    Expense findById(Long id);
    List<Expense> findAllByBudget(Budget budget);
    Double getTotalExpense(List<Expense> expenseList);
    void deleteById(Long id);
    Expense save(Expense expense);
    Expense editById(Long id, String description, Double amount);
}
