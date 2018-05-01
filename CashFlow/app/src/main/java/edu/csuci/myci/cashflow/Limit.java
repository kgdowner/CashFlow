package edu.csuci.myci.cashflow;

import java.math.BigDecimal;

public class Limit {
    BigDecimal amount;
    String name;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Limit(BigDecimal amount, String name) {
        this.amount = amount;
        this.name = name;

    }
}
