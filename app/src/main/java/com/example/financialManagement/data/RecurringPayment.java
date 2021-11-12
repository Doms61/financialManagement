package com.example.financialManagement.data;

import java.util.Date;

public class RecurringPayment {

    private final Date date;
    private final String name;
    private final int amount;

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
