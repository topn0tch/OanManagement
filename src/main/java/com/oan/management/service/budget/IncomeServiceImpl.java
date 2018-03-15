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
 * @author Oan Stultjens
 * @since 12/02/2018.
 */

@Service
public class IncomeServiceImpl implements IncomeService {
    @Autowired
    IncomeRepository incomeRepository;


    @Override
    public Income findById(Long id) {
        return incomeRepository.findById(id);
    }

    @Override
    public List<Income> findAllByBudget(Budget budget) {
        return incomeRepository.findAllByBudget(budget);
    }

    /**
     * Calculates a total value from a List of income's.
     * @param incomeList List of Income's
     * @return Double of the total value
     */
    @Override
    public Double getTotalIncome(List<Income> incomeList) {
        Double total = 0.0;
        for (Income income : incomeList) {
            total += income.getAmount();
        }
        return total;
    }

    @Override
    public void deleteById(Long id) {
        incomeRepository.delete(incomeRepository.findById(id));
    }

    @Override
    public Income save(Income income) {
        return incomeRepository.save(income);
    }

    @Override
    public Income editById(Long id, String description, Double amount) {
        Income income = incomeRepository.findById(id);
        income.setDescription(description);
        income.setAmount(amount);
        return incomeRepository.save(income);
    }
}
