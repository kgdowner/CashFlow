package edu.csuci.myci.cashflow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


// A transaction of some negative or positive amount.

public class Transaction implements Serializable {
    private BigDecimal amount;
    //private Set<Category> categories;
    private Date date;
    private UUID id;

    private String name;

    // basic initializer with a set amount & categories (categories can be null)
    public Transaction(BigDecimal amount, Set<Category> categories, String name) {
        // set the amount
        setAmount(amount);

        // set the categories (if they were passed)
        //if(categories != null) {setCategories(categories);}

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

//    public Set<Category> getCategories(Context context) {
//        Set<Category> categories = Profile.get(context).getAllCategoriesForTransaction(getID().toString());
//        return categories;
//    }

    public UUID getID() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // mutators
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

//    public void setCategories(Set<Category> categories) {
//        this.categories = categories;
//    }

    public void setID(UUID id) {
        this.id = id;
    }

    // additional methods
//    public void addCategory(Category c) {
//        this.categories.add(c);
//    }
//
//    public void removeCateogry(Category c) {
//        this.categories.remove(c);
//    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
