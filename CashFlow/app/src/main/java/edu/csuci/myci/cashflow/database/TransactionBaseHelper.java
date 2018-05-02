package edu.csuci.myci.cashflow.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTransactionTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.TransactionTable;

public class TransactionBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "transactionDatabase.db";
    private SQLiteDatabase mDatabase;

    public TransactionBaseHelper(Context context, String profileName) {
        super(context, profileName, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TransactionTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        TransactionTable.Cols.ID_TRANSACTION + ", " +
                        TransactionTable.Cols.TITLE + ", " +
                        TransactionTable.Cols.DATE + ", " +
//                TransactionTable.Cols.CATEGORIES + ", " +
                        TransactionTable.Cols.AMOUNT + ")"


        );

        db.execSQL(
                "create table " + CategoryTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        CategoryTable.Cols.ID_CATEGORY + ", " +
                        CategoryTable.Cols.CATEGORY_NAME + ", " +
                        CategoryTable.Cols.LIMIT_AMOUNT +
                        ")"
        );

        db.execSQL(
                "create table " + CategoryTransactionTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        CategoryTransactionTable.Cols.ID_CATEGORY + ", " +
                        CategoryTransactionTable.Cols.ID_TRANSACTION + ")"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("ALTER TABLE " + CategoryTable.NAME + " ADD COLUMN limits ");
        }
    }

}
