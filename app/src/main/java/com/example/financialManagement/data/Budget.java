package com.example.financialManagement.data;

import java.io.Serializable;

/**
 * Data class that captures the budget information for logged in users
 */
public class Budget implements Serializable {

    private String name;
    private double totalAmount;
    private double remainingAmount;
    private int percentWarning;
    private int amountWarning;

    public Budget() {

    }
    /**
     * Constructor.
     *
     * @param name   Name of the budget
     * @param remainingAmount Budget amount
     */
    public Budget(String name, double remainingAmount, double totalAmount) {
        this.name = name;
        this.remainingAmount = remainingAmount;
        this.totalAmount = totalAmount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getName() {
        return name;
    }

    public double getRemainingAmount() {
        return remainingAmount;
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
