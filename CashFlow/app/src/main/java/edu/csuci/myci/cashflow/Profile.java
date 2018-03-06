package edu.csuci.myci.cashflow;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by viktoriya on 3/6/18.
 */

public class Profile {
    public static Profile sProfile;

    private List<Transaction> mTransactions;

    public static Profile get(Context context){
        if(sProfile==null){
            sProfile = new Profile(context);

        }
        return sProfile;
    }
    private Profile(Context context){
        mTransactions = new ArrayList<>();
        for(int i = 0; i<100; i++){
            Transaction transaction = new Transaction();
            String[] tempArray ={"category","category"};
            transaction.setCategory(tempArray);//every other one
            transaction.setAmount(0.00);
            mTransactions.add(transaction);
        }
    }

    public List<Transaction> getTransactions() {
        return mTransactions;
    }

    public Transaction getTransaction(UUID id) {
        for(Transaction transaction : mTransactions){
            if(transaction.getId().equals(id)){
                return transaction;
            }
        }
        return null;
    }

}
