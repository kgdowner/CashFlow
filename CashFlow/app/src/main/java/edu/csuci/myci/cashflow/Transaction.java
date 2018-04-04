package edu.csuci.myci.cashflow;

import java.math.BigDecimal;
import java.util.Set;


// A transaction of some negative or positive amount.

public class Transaction {
    private BigDecimal amount;
    private Set<Category> categories;
    private TransactionID id;

    // basic initializer with a set amount & categories (categories can be null)
    public Transaction(BigDecimal amount, Set<Category> categories) {
        // set the amount
        setAmount(amount);

        // set the categories (if they were passed)
        if(categories != null) {setCategories(categories);}

        // create the transaction ID
        this.id = new TransactionID();
    }

    // accessors
    public BigDecimal getAmount() {
        return this.amount;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public TransactionID getID() {
        return this.id;
    }

    // mutators
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void setID(TransactionID id) {
        this.id = id;
    }

    // additional methods
    public void addCategory(Category c) {
        this.categories.add(c);
    }

    public void removeCateogry(Category c) {
        this.categories.remove(c);
    }
}
