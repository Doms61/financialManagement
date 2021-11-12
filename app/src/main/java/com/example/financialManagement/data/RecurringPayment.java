package com.example.financialManagement.data;

import java.util.Date;

/**
 * Data class that captures the recurring payment information for logged in users
 */
public class RecurringPayment {

    private final Date date;
    private final String name;
    private final int amount;

    /**
     * Constructor.
     *
     * @param date   Date when the payment should recur
     * @param name   Name of the payment
     * @param amount Amount of the payment
     */
    public RecurringPayment(Date date, String name, int amount) {
        this.date = date;
        this.name = name;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
