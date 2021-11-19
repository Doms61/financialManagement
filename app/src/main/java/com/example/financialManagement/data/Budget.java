package com.example.financialManagement.data;

/**
 * Data class that captures the budget information for logged in users
 */
public class Budget {

    private final String name;
    private final double amount;
    private int percentWarning;
    private int amountWarning;

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
