package edu.csuci.myci.cashflow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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

    private SQLiteDatabase mDatabase;
    private List<String> mCategories;

    public static CategoryList get(Context context) {
        if(sCategoryList == null) {
            sCategoryList = new CategoryList(context);
        }
        return  sCategoryList;

    }

    private CategoryList(Context context){
        mDatabase =new TransactionBaseHelper(context).getWritableDatabase();


//        this.mCategories = new ArrayList<>();
        addCategory(new Category("groceries", 0));
        addCategory(new Category("gas", 1));
        addCategory(new Category("bullshit", 2));
        addCategory(new Category("housing", 3));
        addCategory(new Category("clothes", 4));
        addCategory(new Category("drinking", 5));
        addCategory(new Category("makeup", 6));
        addCategory(new Category("computer", 7));
    }

    public List<String> getCategories() {
        mCategories = new ArrayList<String>();
        TransactionCursorWrapper cursor = querryCategories(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                mCategories.add(cursor.getCategory().getCategoryName());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return mCategories;



    }


    public void addCategory(Category t){

        ContentValues values = getContentValues(t);
        mDatabase.insert(CategoryTable.NAME, null, values);
    }
    private static ContentValues getContentValues(Category crime){
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.IDCATEGORY, crime.getCategoryId());
        values.put(CategoryTable.Cols.CATEGORYNAME, crime.getCategoryName());


        return values;

    }

    public void removeCategory(Category t){
      //  mCategories.remove(t);
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
    public void addCategoryTransaction(int categoryId, String transactionId){
        ContentValues values = new ContentValues();
        values.put(CategoryTransactionTable.Cols.IDCATEGORY, categoryId);
        values.put(CategoryTransactionTable.Cols.IDTRANSACTION, transactionId);
        mDatabase.insert(CategoryTransactionTable.NAME, null, values);

    }

    //add category /transaction combination
    //remove category/transaction
}
