package edu.csuci.myci.cashflow;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.csuci.myci.cashflow.database.TransactionBaseHelper;
import edu.csuci.myci.cashflow.database.TransactionCursorWrapper;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTransactionTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.TransactionTable;

public class Profile {
    private String name;
    private static Profile sProfile;
    private Context mContext;
    private List<Transaction> mTransactions;
    private SQLiteDatabase mDatabase;



    public static Profile get(Context context, String profileName) {
        if(sProfile == null || !(sProfile.getName().equals(profileName))) {
            sProfile = new Profile(context, profileName);
        }
        return  sProfile;

    }
    private Profile(Context context, String profileName) {
        mContext = context.getApplicationContext();
        mDatabase = new TransactionBaseHelper(mContext, profileName).getWritableDatabase();
        this.name = profileName;


    }

    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        TransactionCursorWrapper cursor = queryTransactions(null, null);

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

    public LineGraphSeries<DataPoint> getSeries(){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        TransactionCursorWrapper cursor;

        cursor = queryTransactions(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                series.appendData(cursor.getDataPoint(),true,100);


                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return series;

    }

    public BarGraphSeries<DataPoint> getBarSeries(){
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        TransactionCursorWrapper cursor;

        cursor = queryTransactionsSumByCategory();
        int i = 0;
        StringBuilder sb = new StringBuilder();

        CategoryList categoryList = CategoryList.get(mContext);
        List<String> categoryNames = new ArrayList<String>();
        categoryNames.addAll(categoryList.getCategories());


        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){

                //FIXME: need to redo query.
                String name  = cursor.getString(cursor.getColumnIndex(CategoryTable.Cols.CATEGORYNAME));
                Category category = categoryList.getCategory(name);


                Double y = cursor.getDouble(cursor.getColumnIndex("temp"));
                Double x = (double) categoryNames.indexOf(category.getCategoryName()); //needs to be ascending... wtf

                DataPoint temp = new DataPoint(i,y);

                String tempString = i+", "+ y.toString();
                sb.append(tempString+"...");

                series.appendData(temp, true,100);
                i++;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Toast.makeText(mContext,sb.toString(), Toast.LENGTH_LONG).show();

        return series;


    }

    public List<Transaction> getTransactionsInOrder(String order) {
        List<Transaction> transactions = new ArrayList<>();
        TransactionCursorWrapper cursor;

        if(order.equals("amount")){
            cursor = queryTransactionsInOrderByDate(null, null, order);

        }else if(order.equals("date")){
            cursor = queryTransactionsInOrder(null, null, order);
        } else{
            cursor = queryTransactionsInOrderByCategory();
        }
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
        TransactionCursorWrapper cursor = queryTransactions(
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

    public String getName() {
        return this.name;
    }

    public void updateTransaction(Transaction transaction){
        String uuidString = transaction.getID().toString();
        ContentValues values = getContentValues(transaction);

        mDatabase.update(TransactionTable.NAME, values,
                TransactionTable.Cols.IDTRANSACTION + " = ?",
                new String[] {uuidString});

    }

    public void removeProfile(String name){

        mContext.deleteDatabase(name);
        GlobalScopeContainer.profileList.remove(name);


    }



    public void removeTransaction(Transaction t){
        String uuidString = t.getID().toString();
        mDatabase.delete(TransactionTable.NAME, TransactionTable.Cols.IDTRANSACTION + " = ?", new String[] {uuidString});
        mDatabase.delete(CategoryTransactionTable.NAME, CategoryTransactionTable.Cols.IDTRANSACTION + " = ? ", new String[]{uuidString});

    }
    public void addTransaction(Transaction t){
        ContentValues values = getContentValues(t);
        mDatabase.insert(TransactionTable.NAME, null, values);
    }
    private static ContentValues getContentValues(Transaction crime){
        ContentValues values = new ContentValues();
        values.put(TransactionTable.Cols.IDTRANSACTION, crime.getID().toString());
        values.put(TransactionTable.Cols.TITLE, crime.getName());
        values.put(TransactionTable.Cols.DATE, crime.getDate().getTime());
        values.put(TransactionTable.Cols.AMOUNT, crime.getAmount().toString());

        return values;

    }
    private TransactionCursorWrapper queryTransactions(String whereClause, String[] whereArgs){
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

    private TransactionCursorWrapper queryTransactionsInOrder(String whereClause, String[] whereArgs, String orderBy){
        Cursor cursor = mDatabase.query(
                TransactionTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                orderBy + " DESC"
        );
        return new TransactionCursorWrapper(cursor);
    }
    private TransactionCursorWrapper queryTransactionsInOrderByDate(String whereClause, String[] whereArgs, String orderBy){
        Cursor cursor = mDatabase.query(
                TransactionTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                "CAST ( "+orderBy+" AS NUMERICAL )" + " DESC"
        );
        return new TransactionCursorWrapper(cursor);
    }

    private TransactionCursorWrapper queryTransactionsInOrderByCategory(){
        String query = "SELECT * FROM Transactions " +
                "INNER JOIN Cat_Transaction ON Transactions.idTransaction = Cat_Transaction.idTransaction " +
                "INNER JOIN Categories ON Cat_Transaction.idCategory = Categories.idCategory " +
                "GROUP BY Transactions.idTransaction " +
                "ORDER BY Categories.categoryName DESC";
        Cursor cursor = mDatabase.rawQuery(query,new String[]{});
        return new TransactionCursorWrapper(cursor);
    }
    private TransactionCursorWrapper queryTransactionsSumByCategory(){
        String query = "SELECT Categories.categoryName, SUM(Transactions.amount) AS temp " +
                "FROM Transactions " +
                "INNER JOIN Cat_Transaction ON Cat_Transaction.idTransaction = Transactions.idTransaction " +
                "INNER JOIN Categories ON Cat_Transaction.idCategory = Categories.idCategory " +
                "GROUP BY Cat_Transaction.idCategory " +
                "ORDER BY Categories.idCategory ASC";
        Cursor cursor = mDatabase.rawQuery(query,new String[]{});
        return new TransactionCursorWrapper(cursor);
    }



}

