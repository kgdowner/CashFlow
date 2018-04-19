package edu.csuci.myci.cashflow.database;

import android.database.Cursor;
import android.database.CursorWrapper;



import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import edu.csuci.myci.cashflow.Category;
import edu.csuci.myci.cashflow.Transaction;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.TransactionTable;

/**
 * Created by viktoriya on 4/4/18.
 */

public class TransactionCursorWrapper extends CursorWrapper {
    public TransactionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Transaction getTransaction(){
        String uuidString = getString(getColumnIndex(TransactionTable.Cols.IDTRANSACTION));
        String title = getString(getColumnIndex(TransactionTable.Cols.TITLE));
        Long date = getLong(getColumnIndex(TransactionTable.Cols.DATE));
        BigDecimal amount = new BigDecimal(getString(getColumnIndex(TransactionTable.Cols.AMOUNT)));
        Transaction transaction = new Transaction(amount,null,title);
        transaction.setDate(new Date(date));
        transaction.setID(UUID.fromString(uuidString));


        return transaction;
    }

    public Category getCategory(){
        String uuidString = getString(getColumnIndex(CategoryTable.Cols.IDCATEGORY));
        String title = getString(getColumnIndex(CategoryTable.Cols.CATEGORYNAME));

        Category category = new Category(title, UUID.fromString(uuidString));

        return category;



    }

}
