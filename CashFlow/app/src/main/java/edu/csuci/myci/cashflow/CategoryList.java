package edu.csuci.myci.cashflow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.csuci.myci.cashflow.database.TransactionBaseHelper;
import edu.csuci.myci.cashflow.database.TransactionCursorWrapper;
import edu.csuci.myci.cashflow.database.TransactionDbSchema;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTransactionTable;

/**
 * Created by viktoriya on 4/9/18.
 */

public class CategoryList {
    private Context mContext;

    private SQLiteDatabase mDatabase;


    public CategoryList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TransactionBaseHelper(mContext, GlobalScopeContainer.activeProfile.getName()).getWritableDatabase();

    }

    //Category Manipulation
    public List<String> getCategories() {
        List<String> tempList = new ArrayList<String>();
        TransactionCursorWrapper cursor = querryCategories(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tempList.add(cursor.getCategory().getCategoryName());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return tempList;


    }

    public Category getCategory(String name) {
        TransactionCursorWrapper cursor = querryCategories(
                CategoryTable.Cols.CATEGORYNAME + " = ?",
                new String[]{name}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCategory();
        } finally {
            cursor.close();
        }
    }


    public void addCategory(Category t) {

        ContentValues values = getContentValues(t);
        mDatabase.insert(CategoryTable.NAME, null, values);
    }

    private static ContentValues getContentValues(Category crime) {
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.IDCATEGORY, crime.getCategoryId().toString());
        values.put(CategoryTable.Cols.CATEGORYNAME, crime.getCategoryName());


        return values;

    }

    public void removeCategory(String categoryName) {
        String uuidString2 = String.valueOf((getCategory(categoryName)).getCategoryId());
        mDatabase.delete(CategoryTable.NAME, CategoryTable.Cols.CATEGORYNAME + " = ?", new String[]{categoryName});
        mDatabase.delete(CategoryTransactionTable.NAME, CategoryTransactionTable.Cols.IDCATEGORY + " = ? ", new String[]{uuidString2});
    }

    public void updateCategory(Category category) {
        String uuidString = category.getCategoryId().toString();
        ContentValues values = getContentValues(category);

        mDatabase.update(CategoryTable.NAME, values,
                CategoryTable.Cols.IDCATEGORY + " = ?",
                new String[]{uuidString});

    }

    public void addCategoryTransaction(UUID categoryId, String transactionId) {
        ContentValues values = new ContentValues();
        values.put(CategoryTransactionTable.Cols.IDCATEGORY, categoryId.toString());
        values.put(CategoryTransactionTable.Cols.IDTRANSACTION, transactionId);
        mDatabase.insert(CategoryTransactionTable.NAME, null, values);

    }

    public void populateCatList() {
        addCategory(new Category("groceries", UUID.randomUUID()));
        addCategory(new Category("transportation", UUID.randomUUID()));
        addCategory(new Category("utilities", UUID.randomUUID()));
        addCategory(new Category("entertainment", UUID.randomUUID()));
    }



    //Limits Manipulation - exist in CategoryTable, so they are here.
    public void addLimit(Limit limit) {
        String name = limit.getName();
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.LIMITAMOUNT, limit.getAmount().toString());

        mDatabase.update(CategoryTable.NAME, values,
                CategoryTable.Cols.CATEGORYNAME + " = ?",
                new String[]{name});

    }

    public List<Limit> getLimits() {
        List<Limit> limits = new ArrayList<>();

        TransactionCursorWrapper cursor = querryCategories(CategoryTable.Cols.LIMITAMOUNT, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                limits.add(cursor.getLimit());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return limits;
    }

    public void removeLimit(Limit limit) {
        String name = limit.getName();
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.LIMITAMOUNT, "");


        mDatabase.update(CategoryTable.NAME, values,
                CategoryTable.Cols.CATEGORYNAME + " = ?",
                new String[]{name});

    }

    //QUERIES
    private TransactionCursorWrapper querryCategories(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CategoryTable.NAME,
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
