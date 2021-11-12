package com.example.financialManagement.data;

public class Budget {

    private final String name;
    private final int amount;
    private int percentWarning;
    private int amountWarning;

    public Budget(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
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
