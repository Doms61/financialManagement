package com.example.financialManagement.data;

import java.io.Serializable;

/**
 * Data class that captures the budget information for logged in users
 */
public class Budget implements Serializable {

    private String name;
    private double amount;
    private int percentWarning;
    private int amountWarning;

    public Budget() {

    }
    /**
     * Constructor.
     *
     * @param name   Name of the budget
     * @param amount Budget amount
     */
    public Budget(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getPercentWarning() {
        return percentWarning;
    }

    public void setPercentWarning(int percentWarning) {
        this.percentWarning = percentWarning;
    }

    public int getAmountWarning() {
        return amountWarning;
    }

    public void setAmountWarning(int amountWarning) {
        this.amountWarning = amountWarning;
    }
}
