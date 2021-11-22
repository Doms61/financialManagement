package com.example.financialManagement.data;

import java.util.Date;

/**
 * Data class for spending
 */
public class Spending {

    private String name;
    private double amount;
    private Date date;
    private String description;

    public Spending() {}

    public Spending(String name, double amount, Date date, String description) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
