package edu.csuci.myci.cashflow;

import java.util.Date;
import java.util.UUID;

/**
 * Created by viktoriya on 3/5/18.
 */

public class Transaction {
    private UUID mId;
    private Date mDate;
    private boolean mPositive;
    private String[] Category;
    private Double mAmount;

    public Double getAmount() {
        return mAmount;
    }

    public void setAmount(Double amount) {
        mAmount = amount;
    }

    public Transaction(){
        mId = UUID.randomUUID();
        mDate = new Date();

    }

    public UUID getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isPositive() {
        return mPositive;
    }

    public void setPositive(boolean positive) {
        mPositive = positive;
    }


    public String[] getCategory() {
        return Category;
    }

    public void setCategory(String[] category) {
        Category = category;
    }
}
