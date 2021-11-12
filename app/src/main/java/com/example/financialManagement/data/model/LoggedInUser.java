package com.example.financialManagement.data.model;

import com.example.financialManagement.data.Balance;
import com.example.financialManagement.data.Budget;
import com.example.financialManagement.data.RecurringPayment;

import java.util.List;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private final String userId;
    private final String displayName;
    private final String email;
    private List<Balance> balanceList;
    private List<RecurringPayment> recurringPaymentsList;
    private List<Budget> budgetsList;

    public LoggedInUser(String userId, String displayName, String email) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public List<Balance> getBalanceList() {
        return balanceList;
    }

    public void setBalanceList(List<Balance> balanceList) {
        this.balanceList = balanceList;
    }

    public List<RecurringPayment> getRecurringPaymentsList() {
        return recurringPaymentsList;
    }

    public void setRecurringPaymentsList(List<RecurringPayment> recurringPaymentsList) {
        this.recurringPaymentsList = recurringPaymentsList;
    }

    public List<Budget> getBudgetsList() {
        return budgetsList;
    }

    public void setBudgetsList(List<Budget> budgetsList) {
        this.budgetsList = budgetsList;
    }
}