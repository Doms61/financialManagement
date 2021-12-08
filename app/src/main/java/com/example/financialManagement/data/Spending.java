package com.example.financialManagement.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Data class for spending
 */
public class Spending implements Serializable {

    private String name;
    private double amount;
    private Date date;
    private String description;
    private String dateShort;

    public Spending() {}

    public Spending(String name, double amount, Date date, String description, String dateShort) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.dateShort = dateShort;
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

    public String getDateShort() {
        return dateShort;
    }

    public void setDateShort(String dateShort) {
        this.dateShort = dateShort;//dateShort.lastIndexOf('a')
    }
}
