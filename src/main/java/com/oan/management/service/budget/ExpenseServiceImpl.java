package com.oan.management.service.budget;

import com.oan.management.model.Budget;
import com.oan.management.model.Expense;
import com.oan.management.repository.budget.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oan Stultjens
 * @since 12/02/2018
 * The implementation of {@link ExpenseService}
 * Basic CRUD operations are implemented here
 * @see org.springframework.data.jpa.repository.JpaRepository
 */

@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired
    ExpenseRepository expenseRepository;

    @Override
    public Expense findById(Long id) {
        return expenseRepository.findById(id);
    }

    @Override
    public List<Expense> findAllByBudget(Budget budget) {
        return expenseRepository.findAllByBudget(budget);
    }

    /**
     * Calculates the total of expenses from a List
     * @param expenseList {@link List} of {@link Expense}
     * @return Double
     */
    @Override
    public Double getTotalExpense(List<Expense> expenseList) {
        Double total = 0.0;
        for (Expense expense : expenseList) {
            total += expense.getAmount();
        }
        return total;
    }

    @Override
    public void deleteById(Long id) {
        expenseRepository.delete(expenseRepository.findById(id));
    }

    @Override
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    /**
     * Edits an expense by id
     * @param id ID of the expense
     * @param description Expense description
     * @param amount Expense amount
     * @return Expense
     */
    @Override
    public Expense editById(Long id, String description, Double amount) {
        Expense expense = expenseRepository.findById(id);
        expense.setDescription(description);
        expense.setAmount(amount);
        return expenseRepository.save(expense);
    }
}
