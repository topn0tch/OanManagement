package com.oan.management.service.budget;

import com.oan.management.model.Budget;
import com.oan.management.model.User;

import java.util.List;

/**
 * Created by Oan on 9/02/2018.
 */
public interface BudgetService {
    List<Budget> findAll();
    List<Budget> findAllByUser(User user);
    Budget save(Budget budget);
    Budget findById(Long id);
    void deleteById(Long id);
    Double calculateLeftover(Budget budget);
}
