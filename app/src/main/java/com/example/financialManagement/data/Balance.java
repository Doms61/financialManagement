package com.example.financialManagement.data;

/**
 * Data class that captures the balance information for logged in users.
 */
public class Balance {

    private final String balanceName;
    private final int balance;

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
}
