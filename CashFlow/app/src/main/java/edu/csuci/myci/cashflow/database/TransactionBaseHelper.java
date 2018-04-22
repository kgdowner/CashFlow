package edu.csuci.myci.cashflow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.csuci.myci.cashflow.Category;
import edu.csuci.myci.cashflow.Transaction;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTransactionTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.TransactionTable;


/**
 * Created by viktoriya on 4/4/18.
 */

public class TransactionBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "transactionDatabase.db";
    private SQLiteDatabase mDatabase;

    public TransactionBaseHelper(Context context, String profileName) {
        super(context, profileName, null , VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TransactionTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                TransactionTable.Cols.IDTRANSACTION + ", " +
                TransactionTable.Cols.TITLE + ", " +
                TransactionTable.Cols.DATE + ", " +
//                TransactionTable.Cols.CATEGORIES + ", " +
                TransactionTable.Cols.AMOUNT + ")"


        );

        db.execSQL(
                "create table " + CategoryTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        CategoryTable.Cols.IDCATEGORY + ", " +
                        CategoryTable.Cols.CATEGORYNAME + ")"
        );

        db.execSQL(
                "create table " + CategoryTransactionTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        CategoryTransactionTable.Cols.IDCATEGORY + ", " +
                        CategoryTransactionTable.Cols.IDTRANSACTION + ")"
        );
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
