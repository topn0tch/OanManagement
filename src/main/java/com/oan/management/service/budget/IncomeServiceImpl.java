package com.oan.management.service.budget;

import com.oan.management.model.Budget;
import com.oan.management.model.Income;
import com.oan.management.repository.budget.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link IncomeService}
 * Basic CRUD operations
 *
 * @author Oan Stultjens
 * @since 12/02/2018.
 */

@Service
public class IncomeServiceImpl implements IncomeService {
    @Autowired
    IncomeRepository incomeRepository;

    @Autowired
    ExpenseService expenseService;

    /**
     * Returns an {@link Income} by the specified id
     * @param id Long
     * @return Income
     */
    @Override
    public Income findById(Long id) {
        return incomeRepository.findById(id);
    }

    /**
     * Returns a List of {@link Income}'s by the specified {@link Budget}
     * @param budget {@link Budget}
     * @return List of {@link Income}
     */
    @Override
    public List<Income> findAllByBudget(Budget budget) {
        return incomeRepository.findAllByBudget(budget);
    }

    /**
     * Calculates a total value from a List of income's.
     * @param budget
     * @return Double of the total value
     */
    @Override
    public Double getTotalIncome(Budget budget) {
        List<Income> incomeList = incomeRepository.findAllByBudget(budget);
        Double total = 0.0;
        for (Income income : incomeList) {
            total += income.getAmount();
        }
        return total;
    }

    /**
     * Deletes an {@link Income} from the database with the specified id
     * @param id Long
     */
    @Override
    public void deleteById(Long id) {
        incomeRepository.delete(incomeRepository.findById(id));
    }

    /**
     * Saves an {@link Income} to the database
     * @param income {@link Income}
     * @return Income
     */
    @Override
    public Income save(Income income) {
        return incomeRepository.save(income);
    }

    /**
     * Edits an {@link Income} by the specified id
     * @param id Long
     * @param description String
     * @param amount Double
     * @return Income
     */
    @Override
    public Income editById(Long id, String description, Double amount) {
        Income income = incomeRepository.findById(id);
        income.setDescription(description);
        income.setAmount(amount);
        return incomeRepository.save(income);
    }

    @Override
    public Double calculateIncomesPercent(Budget budget) {
        return 100 - expenseService.calculateExpensesPercent(budget);
    }
}
