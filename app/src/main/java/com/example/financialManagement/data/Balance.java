package com.example.financialManagement.data;

import java.io.Serializable;

/**
 * Data class that captures the balance information for logged in users.
 */
public class Balance implements Serializable {

    private String balanceName;
    private int balance;


    public Balance() { }

    /**
     * Constructor.
     *
     * @param balanceName Name of the balance or wallet
     * @param balance     Balance amount
     */
    public Balance(String balanceName, int balance) {
        this.balanceName = balanceName;
        this.balance = balance;
    }

    public String getBalanceName() {
        return balanceName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalanceName(String balanceName) {
        this.balanceName = balanceName;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
