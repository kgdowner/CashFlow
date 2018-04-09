package edu.csuci.myci.cashflow;

import android.content.Context;

import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;
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

    public static Profile get(Context context) {
        if(sProfile == null) {
            sProfile = new Profile(context);
        }
        return  sProfile;

    }
    private Profile(Context context) {
        this.mTransactions = new ArrayList<>();
//        for(int i = 0; i<100; i++){
//            // TODO: remove this & implement non-hardcoded list
//            Category tempCat = new Category("category"+i, i);
//            Set<Category> tempCats = new HashSet();
//            tempCats.add(tempCat);
//
//            Transaction transaction = new Transaction(new BigDecimal(0.00), tempCats);
//            transaction.setAmount(new BigDecimal(0.25 * i));
//
//            this.mTransactions.add(transaction);
//        }
    }

    public List<Transaction> getTransactions() {
        return mTransactions;
    }

    public Transaction getTransactions(UUID id) {
        for(Transaction transaction : mTransactions){
            if(transaction.getID().equals(id)) {
                return transaction;
            }
        }
        return null;
    }
    public void removeTransaction(Transaction t){
        mTransactions.remove(t);
    }
    public void addTransaction(Transaction t){mTransactions.add(t);}
}
