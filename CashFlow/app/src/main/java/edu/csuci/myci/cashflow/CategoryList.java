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
import edu.csuci.myci.cashflow.database.TransactionDbSchema;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTransactionTable;

/**
 * Created by viktoriya on 4/9/18.
 */

public class CategoryList {
    private static CategoryList sCategoryList;
    private Context mContext;

    private SQLiteDatabase mDatabase;

    public static CategoryList get(Context context) {
        if(sCategoryList == null) {
            sCategoryList = new CategoryList(context);
        }
        return  sCategoryList;

    }

    private CategoryList(Context context){
        mContext = context.getApplicationContext();
        mDatabase =new TransactionBaseHelper(mContext, GlobalScopeContainer.activeProfile.getName()).getWritableDatabase();

    }

    public List<String> getCategories() {
        List<String> tempList = new ArrayList<String>();
        TransactionCursorWrapper cursor = querryCategories(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
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
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCategory();
        } finally {
            cursor.close();
        }
    }

    public Category getCategorybyID(String id) {
        TransactionCursorWrapper cursor = querryCategories(
                CategoryTable.Cols.IDCATEGORY + " = ?",
                new String[]{id}
        );

        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCategory();
        } finally {
            cursor.close();
        }
    }


    public void addCategory(Category t){

        ContentValues values = getContentValues(t);
        mDatabase.insert(CategoryTable.NAME, null, values);
    }
    private static ContentValues getContentValues(Category crime){
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.IDCATEGORY, crime.getCategoryId().toString());
        values.put(CategoryTable.Cols.CATEGORYNAME, crime.getCategoryName());


        return values;

    }

    public void removeCategory(String categoryName){
        String uuidString = categoryName;
        String uuidString2 = String.valueOf( (sCategoryList.getCategory(categoryName)).getCategoryId());
        mDatabase.delete(CategoryTable.NAME, CategoryTable.Cols.CATEGORYNAME + " = ?", new String[] {uuidString});
        mDatabase.delete(CategoryTransactionTable.NAME, CategoryTransactionTable.Cols.IDCATEGORY + " = ? ", new String[]{uuidString2});
    }


    //            Category tempCat = new Category("category"+i, i);
//            Set<Category> tempCats = new HashSet();
//            tempCats.add(tempCat);

    private TransactionCursorWrapper querryCategories(String whereClause, String[] whereArgs){
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
    public void addCategoryTransaction(UUID categoryId, String transactionId){
        ContentValues values = new ContentValues();
        values.put(CategoryTransactionTable.Cols.IDCATEGORY, categoryId.toString());
        values.put(CategoryTransactionTable.Cols.IDTRANSACTION, transactionId);
        mDatabase.insert(CategoryTransactionTable.NAME, null, values);

    }

    public void populateCatList(){
        addCategory(new Category("groceries", UUID.randomUUID()));
        addCategory(new Category("transportation", UUID.randomUUID()));
        addCategory(new Category("utilities", UUID.randomUUID()));
        addCategory(new Category("entertainment", UUID.randomUUID()));
//        addCategory(new Category("clothes", UUID.randomUUID()));
//        addCategory(new Category("drinking", UUID.randomUUID()));
//        addCategory(new Category("makeup", UUID.randomUUID()));
//        addCategory(new Category("computer", UUID.randomUUID()));
    }

    public void addLimit(Limit limit){
        String name = limit.getName();
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.LIMITAMOUNT, limit.getAmount().toString());


        mDatabase.update(CategoryTable.NAME, values,
                CategoryTable.Cols.CATEGORYNAME + " = ?",
                new String[] {name});

    }

    public void removeLimit(Limit limit){
        String name = limit.getName();
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.LIMITAMOUNT, "");


        mDatabase.update(CategoryTable.NAME, values,
                CategoryTable.Cols.CATEGORYNAME + " = ?",
                new String[] {name});

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


}
