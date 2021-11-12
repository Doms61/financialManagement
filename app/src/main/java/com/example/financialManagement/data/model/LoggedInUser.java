package com.example.financialManagement.data.model;

import com.example.financialManagement.data.Balance;
import com.example.financialManagement.data.Budget;
import com.example.financialManagement.data.RecurringPayment;

import java.util.List;

/**
 * Data class that captures user information for logged in users.
 */
public class LoggedInUser {

    private String userId;
    private String userName;
    private String email;
    private List<Balance> balanceList;
    private List<RecurringPayment> recurringPaymentsList;
    private List<Budget> budgetsList;

    /**
     * Constructor.
     *
     * @param userId   User ID
     * @param userName User name
     * @param email    user email
     */
    public LoggedInUser(String userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }

    /**
     * Empty constructor for object deserialization.
     */
    public LoggedInUser() {
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}