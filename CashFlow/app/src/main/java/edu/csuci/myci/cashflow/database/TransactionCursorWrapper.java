package edu.csuci.myci.cashflow.database;

import android.database.Cursor;
import android.database.CursorWrapper;


import com.jjoe64.graphview.series.DataPoint;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import edu.csuci.myci.cashflow.Category;
import edu.csuci.myci.cashflow.Limit;
import edu.csuci.myci.cashflow.Transaction;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.TransactionTable;

public class TransactionCursorWrapper extends CursorWrapper {
    public TransactionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Transaction getTransaction(){
        String uuidString = getString(getColumnIndex(TransactionTable.Cols.ID_TRANSACTION));
        String title = getString(getColumnIndex(TransactionTable.Cols.TITLE));
        Long date = getLong(getColumnIndex(TransactionTable.Cols.DATE));
        BigDecimal amount = new BigDecimal(getString(getColumnIndex(TransactionTable.Cols.AMOUNT)));
        Transaction transaction = new Transaction(amount,null,title);
        transaction.setDate(new Date(date));
        transaction.setID(UUID.fromString(uuidString));


        return transaction;
    }

    public Category getCategory(){
        String uuidString = getString(getColumnIndex(CategoryTable.Cols.ID_CATEGORY));
        String title = getString(getColumnIndex(CategoryTable.Cols.CATEGORY_NAME));

        return new Category(title, UUID.fromString(uuidString));

    }
    public Limit getLimit(){
        BigDecimal amount = new BigDecimal(getString(getColumnIndex(CategoryTable.Cols.LIMIT_AMOUNT)));
        String title = getString(getColumnIndex(CategoryTable.Cols.CATEGORY_NAME));

        return new Limit(amount, title);

    }
    public DataPoint getDataPoint(){
        Long date = getLong(getColumnIndex(TransactionTable.Cols.DATE));
        BigDecimal amount = new BigDecimal(getString(getColumnIndex(TransactionTable.Cols.AMOUNT)));

        return new DataPoint(date, amount.doubleValue());

    }

}
