package com.example.financialManagement.data;

public class Balance {

    private final String balanceName;
    private final int balance;

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
