package com.oan.management.service.budget;

import com.oan.management.model.Budget;
import com.oan.management.model.Expense;
import com.oan.management.model.Income;
import com.oan.management.model.User;
import com.oan.management.repository.budget.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oan Stultjens
 * @since 9/02/2018.
 * The implementation of {@link BudgetService}
 * Basic CRUD operations are implemented here
 * @see org.springframework.data.jpa.repository.JpaRepository
 */

@Service
public class BudgetServiceImpl implements BudgetService{
    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    ExpenseService expenseService;

    @Autowired
    IncomeService incomeService;

    /**
     * Retrieve a list of all budgets
     * @return List of {@link Budget}
     */
    @Override
    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    /**
     * Retrieve a list of budgets from the specified {@link User}
     * @param user {@link User}
     * @return List of {@link Budget}
     */
    @Override
    public List<Budget> findAllByUser(User user) {
        return budgetRepository.findAllByUser(user);
    }

    /**
     * Save a {@link Budget} to the database
     * @param budget {@link Budget}
     * @return Budget
     */
    @Override
    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }

    /**
     * Find a {@link Budget} by the specified id
     * @param id Long
     * @return Budget
     */
    @Override
    public Budget findById(Long id) {
        return budgetRepository.findById(id);
    }

    /**
     * Delete a {@link Budget} by the specified id
     * @param id Long
     */
    @Override
    public void deleteById(Long id) {
        budgetRepository.delete(budgetRepository.findById(id));
    }

    @Override
    public Double calculateLeftover(Budget budget) {
        List<Income> incomeList = incomeService.findAllByBudget(budget);
        List<Expense> expenseList = expenseService.findAllByBudget(budget);
        return (budget.getBudgetAmount() + (incomeService.getTotalIncome(budget) - expenseService.getTotalExpense(budget)));
    }
}
