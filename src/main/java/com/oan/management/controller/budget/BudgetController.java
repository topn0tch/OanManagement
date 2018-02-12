package com.oan.management.controller.budget;

import com.oan.management.model.Budget;
import com.oan.management.model.Expense;
import com.oan.management.model.Income;
import com.oan.management.model.User;
import com.oan.management.service.UserService;
import com.oan.management.service.budget.BudgetService;
import com.oan.management.service.budget.ExpenseService;
import com.oan.management.service.budget.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Oan on 9/02/2018.
 */

@Controller
public class BudgetController {
    @Autowired
    UserService userService;

    @Autowired
    BudgetService budgetService;

    @Autowired
    IncomeService incomeService;

    @Autowired
    ExpenseService expenseService;

    @GetMapping("/budget-list")
    public String getBudgetManager(Model model, Authentication authentication) {
        User userLogged = userService.findByUser(authentication.getName());
        List<Budget> budgetList = budgetService.findAllByUser(userLogged);
        budgetList.sort(Comparator.comparing(Budget::getId).reversed());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("budgetList", budgetList);
        }
        return "budget-list";
    }

    @GetMapping("/budget-new")
    public String newBudget(Model model, Authentication authentication) {
        User userLogged = userService.findByUser(authentication.getName());
        model.addAttribute("budget", new Budget());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
        }
        return "budget-new";
    }

    @PostMapping("/budget-new")
    public String saveNewBudget(Model model, Budget budget, Authentication authentication) {
        User userLogged = userService.findByUser(authentication.getName());
        if (budget.getTitle().length() >= 3 ) {
            if (budget.getBudgetAmount() >= 0) {
                Budget userBudget = new Budget(budget.getTitle(), budget.getBudgetAmount(), userLogged);
                budgetService.save(userBudget);
                return "redirect:/budget-list?success";
            } else {
                return "redirect:/budget-new?error";
            }
        } else {
            return "redirect:/budget-new?error";
        }
    }

    @GetMapping("/budget")
    public String showBudget(Model model, Authentication authentication, @RequestParam(required = false) Long id) {
        User userLogged = userService.findByUser(authentication.getName());

        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("expense", new Expense());
            model.addAttribute("income", new Income());
            if (id != null) {
                // Save the id in a attribute to save later values
                Budget paramBudget = budgetService.findById(id);
                model.addAttribute("paramBudget", paramBudget);
                // Get incomes and expenses
                List<Income> incomeList = incomeService.findAllByBudget(paramBudget);
                List<Expense> expenseList = expenseService.findAllByBudget(paramBudget);
                // Get the total of incomes and expenses
                Double totalIncome = incomeService.getTotalIncome(incomeList);
                Double totalExpense = expenseService.getTotalIncome(expenseList);

                // Calculations
                Double leftOver = (paramBudget.getBudgetAmount() + (totalIncome - totalExpense));
                Double expensesPercent = totalExpense / (totalIncome+paramBudget.getBudgetAmount()) * 100;
                Double incomesPercent = 100 - expensesPercent;

                // Settings attributes
                model.addAttribute("paramBudget", paramBudget);
                model.addAttribute("totalIncome", totalIncome);
                model.addAttribute("totalExpense", totalExpense);
                model.addAttribute("leftOver", leftOver);
                // Percentages for progress bar
                model.addAttribute("expensesPercent", expensesPercent);
                model.addAttribute("incomesPercent", incomesPercent);
                // Lists
                model.addAttribute("incomeList", incomeList);
                model.addAttribute("expenseList", expenseList);
            } else {
                return "redirect:budget-list?notfound";
            }
        }
        return "budget";
    }

    @GetMapping("budget-delete")
    public String deleteBudget(Authentication authentication, @RequestParam Long id) {
        User userLogged = userService.findByUser(authentication.getName());

        Budget paramBudget = budgetService.findById(id);
        if (paramBudget.getUser() == userLogged) {
            budgetService.deleteById(id);
            return "redirect:budget-list?deleted";
        } else {
            return "redirect:budget-list?notfound";
        }
    }

    @GetMapping("income-delete")
    public String deleteIncome(Authentication authentication, @RequestParam Long id) {
        User userLogged = userService.findByUser(authentication.getName());
        List<Budget> budgetList = budgetService.findAllByUser(userLogged);
        Income paramIncome = incomeService.findById(id);

        // Check of income is from the user
        if (budgetList.contains(paramIncome.getBudget())) {
            Long redirectBack = paramIncome.getBudget().getId();
            incomeService.deleteById(id);
            return "redirect:budget?id="+redirectBack;
        } else {
            return "redirect:budget-list";
        }
    }

    @GetMapping("expense-delete")
    public String deleteExpense(Authentication authentication, @RequestParam Long id) {
        User userLogged = userService.findByUser(authentication.getName());
        List<Budget> budgetList = budgetService.findAllByUser(userLogged);
        Expense paramExpense = expenseService.findById(id);

        // Check of income is from the user
        if (budgetList.contains(paramExpense.getBudget())) {
            Long redirectBack = paramExpense.getBudget().getId();
            expenseService.deleteById(id);
            return "redirect:budget?id="+redirectBack;
        } else {
            return "redirect:budget-list";
        }
    }

    @PostMapping("/budget")
    public String addIncome(@ModelAttribute("paramBudget") Budget paramBudget, @RequestParam("action") String action, Model model, Authentication authentication, Income income, Expense expense) {
        User userLogged = userService.findByUser(authentication.getName());
        System.out.println("mother:"+action);

        if (action.contains("income")) {
            if (income.getAmount() > 0) {
                incomeService.save(new Income(paramBudget, income.getDescription(), income.getAmount()));
                return "redirect:/budget?id=" + paramBudget.getId();
            } else {
                return "redirect:/budget-list?amount";
            }
        } else if (action.contains("expense")) {
            if (expense.getAmount() > 0) {
                expenseService.save(new Expense(paramBudget, expense.getDescription(), expense.getAmount()));
                return "redirect:/budget?id=" + paramBudget.getId();
            } else {
                return "redirect:budget-list?amount";
            }
        } else {
            return "redirect:budget-list?actionerror";
        }
    }
    /*if (income.getAmount() > 0 && income.getAmount() != null) {
            incomeService.save(new Income(paramBudget, income.getDescription(), income.getAmount()));
            System.out.println("test1: " + income.getAmount() + " : " + expense.getAmount());
            if (expense.getAmount() > 0 && expense.getDescription() != income.getDescription()) {
                expenseService.save(new Expense(paramBudget, expense.getDescription(), expense.getAmount()));
                System.out.println("test3: " + income + " : " + expense);
                return "redirect:/budget?id=" + paramBudget.getId();
            } else {
                return "redirect:/budget?id=" + paramBudget.getId();
            }
        } else {
            return "redirect:/budget?id="+paramBudget.getId()+"?error";
        }*/
}