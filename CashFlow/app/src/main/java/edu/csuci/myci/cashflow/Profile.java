package edu.csuci.myci.cashflow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.csuci.myci.cashflow.database.TransactionBaseHelper;
import edu.csuci.myci.cashflow.database.TransactionCursorWrapper;
import edu.csuci.myci.cashflow.database.TransactionDbSchema;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTransactionTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.TransactionTable;

/**
 * Created by viktoriya on 3/3/18.
 */

public class Profile {
    private static Profile sProfile;
    private Context mContext;
    private List<Transaction> mTransactions;
    private SQLiteDatabase mDatabase;


    public static Profile get(Context context) {
        if(sProfile == null) {
            sProfile = new Profile(context);
        }
        return  sProfile;

    }
    private Profile(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TransactionBaseHelper(mContext).getWritableDatabase();

    }

   public List<Transaction> getTransactions() {
       List<Transaction> transactions = new ArrayList<>();

       TransactionCursorWrapper cursor = querryCrimes(null, null);

       try {
           cursor.moveToFirst();
           while (!cursor.isAfterLast()){
               transactions.add(cursor.getTransaction());
               cursor.moveToNext();
           }
       } finally {
           cursor.close();
       }
       return transactions;
   }

    public Transaction getTransactions(UUID id) {
        TransactionCursorWrapper cursor = querryCrimes(
                TransactionTable.Cols.IDTRANSACTION + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getTransaction();
        } finally {
            cursor.close();
        }
    }

    public ArrayList<String> getAllCategoriesForTransaction(String transactionId){
        //for displaying in listViewFragment
        ArrayList<String> catNames = new ArrayList<>();

        String query = "SELECT * FROM Categories, Cat_Transaction " +
                "WHERE Cat_Transaction.idTransaction =? " +
                "AND Categories.idCategory =  Cat_Transaction.idCategory";
        Cursor c = mDatabase.rawQuery(query, new String[]{transactionId});
        if (c.moveToFirst()) {
            do {

                catNames.add(c.getString(c.getColumnIndex(CategoryTable.Cols.CATEGORYNAME)));

            } while (c.moveToNext());
        }
        return  catNames;


    }

    public void updateTransaction(Transaction crime){
        String uuidString = crime.getID().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(TransactionTable.NAME, values,
                TransactionTable.Cols.IDTRANSACTION + " = ?",
                new String[] {uuidString});

    }


    public void removeTransaction(Transaction t){
        String uuidString = t.getID().toString();
        mDatabase.delete(TransactionTable.NAME, TransactionTable.Cols.IDTRANSACTION + " = ?", new String[] {uuidString});

    }
    public void addTransaction(Transaction t){
        ContentValues values = getContentValues(t);
        mDatabase.insert(TransactionTable.NAME, null, values);
    }
    private static ContentValues getContentValues(Transaction crime){
        ContentValues values = new ContentValues();
        values.put(TransactionTable.Cols.IDTRANSACTION, crime.getID().toString());
        values.put(TransactionTable.Cols.TITLE, crime.getName());
        values.put(TransactionTable.Cols.DATE, crime.getDate().toString());
        values.put(TransactionTable.Cols.AMOUNT, crime.getAmount().toString());

        return values;

    }
    private TransactionCursorWrapper querryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                TransactionTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new TransactionCursorWrapper(cursor);
    }


}
