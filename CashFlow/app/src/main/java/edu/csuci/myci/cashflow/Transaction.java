package edu.csuci.myci.cashflow;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


// A transaction of some negative or positive amount.

public class Transaction implements Serializable {
    private BigDecimal amount;
    //private Set<Category> categories;
    private Date date;
    private UUID id;

    private String name;

    // basic initializer with a set amount & categories (categories can be null)
    public Transaction(BigDecimal amount, String name) {
        // set the amount
        setAmount(amount);


        // create the transaction ID
        this.id = (UUID.randomUUID());

        //set name
        setName(name);

        this.date = new Date();
    }


    // accessors
    public BigDecimal getAmount() {
        return this.amount;
    }

    public UUID getID() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // mutator methods
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setID(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
