package edu.csuci.myci.cashflow;

import java.util.Date;
import java.util.UUID;

/**
 * Created by viktoriya on 3/12/18.
 */

public class Transaction {
    private UUID mID;
    private  Date mDate;
    private double mAmount;
    private String[] mCategory;

    public Transaction(){
        mID = UUID.randomUUID();
        mDate = new Date();

    }

    public UUID getID() {
        return mID;
    }

    public Date getDate() {
        return mDate;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }

    public String[] getCategory() {
        return mCategory;
    }

    public void setCategory(String[] category) {
        mCategory = category;
    }
}
