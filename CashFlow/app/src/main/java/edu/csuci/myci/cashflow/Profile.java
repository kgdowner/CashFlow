package edu.csuci.myci.cashflow;

import android.content.Context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by viktoriya on 3/3/18.
 */

public class Profile {
    private static Profile sProfile;
    //s prefix is for Static Variables...

    private List<Transaction> mTransactions;

    public static Profile get(Context context){
        if(sProfile == null){
            sProfile = new Profile(context);
        }
        return  sProfile;

    }
    private Profile(Context context){
        mTransactions = new ArrayList<>();
        for(int i = 0; i<100; i++){
            //Transaction transaction = new Transaction(new BigDecimal(0.00), Set<Category> categories);
            //transaction.setAmount(0.00);
            //String[] tempArray = {"category1","category2"};
            //transaction.setCategory(tempArray);//every other one
            //mTransactions.add(transaction);
        }
    }

    public List<Transaction> getTransactions() {
        return mTransactions;
    }

    public Transaction getTransactions(UUID id) {
        for(Transaction transaction : mTransactions){
            if(transaction.getID().equals(id)){
                return transaction;
            }
        }
        return null;
    }
}
