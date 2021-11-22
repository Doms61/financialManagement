package com.example.financialManagement.enumerations;

public enum Enums {

  BALANCE_LIST("balanceList"),
  SPENDING_LIST ("spendingList");

  private final String description;

    Enums(String description) {
      this.description = description;
    }

    public String getDescription() {
      return this.description;
    }
}
