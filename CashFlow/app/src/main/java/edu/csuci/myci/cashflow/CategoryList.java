package edu.csuci.myci.cashflow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.csuci.myci.cashflow.database.TransactionBaseHelper;
import edu.csuci.myci.cashflow.database.TransactionCursorWrapper;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTransactionTable;

public class CategoryList {
    private Context context;
    private SQLiteDatabase database;


    public CategoryList(Context context) {
        this.context = context.getApplicationContext();
        this.database = new TransactionBaseHelper(this.context, GlobalScopeContainer.activeProfile.getName()).getWritableDatabase();

    }

    //Category Manipulation
    public List<String> getCategoryNames() {
        List<String> tempList = new ArrayList<String>();
        TransactionCursorWrapper cursor = queryCategories(null, null);

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
    public List<Category> getCategoris() {
        List<Category> tempList = new ArrayList<Category>();
        TransactionCursorWrapper cursor = queryCategories(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tempList.add(cursor.getCategory());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return tempList;


    }

    public Category getCategory(String name) {
        TransactionCursorWrapper cursor = queryCategories(
                CategoryTable.Cols.CATEGORY_NAME + " = ?",
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
        this.database.insert(CategoryTable.NAME, null, values);
    }

    private static ContentValues getContentValues(Category crime) {
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.ID_CATEGORY, crime.getCategoryId().toString());
        values.put(CategoryTable.Cols.CATEGORY_NAME, crime.getCategoryName());


        return values;

    }

    public void removeCategory(String categoryName) {
        String uuidString2 = String.valueOf((getCategory(categoryName)).getCategoryId());
        this.database.delete(CategoryTable.NAME, CategoryTable.Cols.CATEGORY_NAME + " = ?", new String[]{categoryName});
        this.database.delete(CategoryTransactionTable.NAME, CategoryTransactionTable.Cols.ID_CATEGORY + " = ? ", new String[]{uuidString2});
    }

    public void removeCategoryTransaction(UUID categoryId, UUID transactionId) {
        this.database.delete(CategoryTransactionTable.NAME,
                CategoryTransactionTable.Cols.ID_CATEGORY + " = ?  " +
                        "AND " + CategoryTransactionTable.Cols.ID_TRANSACTION + " = ? ",
                new String[]{categoryId.toString(), transactionId.toString()});
    }


    public void updateCategory(Category category) {
        String uuidString = category.getCategoryId().toString();
        ContentValues values = getContentValues(category);

        this.database.update(CategoryTable.NAME, values,
                CategoryTable.Cols.ID_CATEGORY + " = ?",
                new String[]{uuidString});

    }

    public void addCategoryTransaction(UUID categoryId, UUID transactionId) {
        ContentValues values = new ContentValues();
        values.put(CategoryTransactionTable.Cols.ID_CATEGORY, categoryId.toString());
        values.put(CategoryTransactionTable.Cols.ID_TRANSACTION, transactionId.toString());
        this.database.insert(CategoryTransactionTable.NAME, null, values);

    }

    public void populateCatList() {
        addCategory(new Category("groceries", UUID.randomUUID()));
        addCategory(new Category("transportation", UUID.randomUUID()));
        addCategory(new Category("utilities", UUID.randomUUID()));
        addCategory(new Category("entertainment", UUID.randomUUID()));
    }

    public List<Category> getAllCategoriesForTransaction(String transactionId) {
        List<Category> catNames = new ArrayList<>();


        TransactionCursorWrapper cursor = queryCategoriesForTransaction(transactionId);
        if (cursor.moveToFirst()) {
            do {

                catNames.add(
                        cursor.getCategory()

                );

            } while (cursor.moveToNext());
        }
        return catNames;
    }
    private TransactionCursorWrapper queryCategoriesForTransaction(String transactionId) {
        String query = "SELECT * FROM Categories, Cat_Transaction " +
                "WHERE Cat_Transaction.idTransaction =? " +
                "AND Categories.idCategory =  Cat_Transaction.idCategory";
        Cursor cursor = database.rawQuery(query, new String[]{transactionId});
        return new TransactionCursorWrapper(cursor);
    }


    //Limits Manipulation - exist in CategoryTable, so they are here.
    public void addLimit(Limit limit) {
        String name = limit.getName();
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.LIMIT_AMOUNT, limit.getAmount().toString());

        this.database.update(CategoryTable.NAME, values,
                CategoryTable.Cols.CATEGORY_NAME + " = ?",
                new String[]{name});

    }

    public List<Limit> getLimits() {
        List<Limit> limits = new ArrayList<>();

        TransactionCursorWrapper cursor = queryCategories(CategoryTable.Cols.LIMIT_AMOUNT, null);
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
        values.put(CategoryTable.Cols.LIMIT_AMOUNT, "");


        this.database.update(CategoryTable.NAME, values,
                CategoryTable.Cols.CATEGORY_NAME + " = ?",
                new String[]{name});

    }

    //QUERIES
    private TransactionCursorWrapper queryCategories(String whereClause, String[] whereArgs) {
        Cursor cursor = this.database.query(
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
