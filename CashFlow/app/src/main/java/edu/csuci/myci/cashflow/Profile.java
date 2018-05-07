package edu.csuci.myci.cashflow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import edu.csuci.myci.cashflow.database.TransactionBaseHelper;
import edu.csuci.myci.cashflow.database.TransactionCursorWrapper;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.CategoryTransactionTable;
import edu.csuci.myci.cashflow.database.TransactionDbSchema.TransactionTable;

public class Profile {
    private String name;
    private static Profile activeProfile;
    private Context context;
    private SQLiteDatabase database;
    private static SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);



    public static Profile get(Context context, String profileName) {
        if (activeProfile == null || !(activeProfile.getName().equals(profileName))) {
            activeProfile = new Profile(context, profileName);
        }
        return activeProfile;

    }

    private Profile(Context context, String profileName) {
        this.context = context.getApplicationContext();
        database = new TransactionBaseHelper(context, profileName).getWritableDatabase();
        this.name = profileName;


    }

    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        TransactionCursorWrapper cursor = queryTransactions(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                transactions.add(cursor.getTransaction());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return transactions;
    }

    public List<Transaction> getTransactionsInOrder(String order) {
        List<Transaction> transactions = new ArrayList<>();
        TransactionCursorWrapper cursor;

        if (order.equals("amount")) {
            cursor = queryTransactionsInOrder(null, null, "CAST ( " + order + " AS NUMERICAL ) DESC");

        } else if (order.equals("date")) {
            cursor = queryTransactionsInOrder(null, null, order + " DESC");
        } else {
            cursor = queryTransactionsInOrderByCategory();
        }
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
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
                TransactionTable.Cols.ID_TRANSACTION + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getTransaction();
        } finally {
            cursor.close();
        }
    }

    public ArrayList<String> getAllCategoriesForTransaction(String transactionId) {
        //for displaying in listViewFragment
        ArrayList<String> catNames = new ArrayList<>();

        String query = "SELECT * FROM Categories, Cat_Transaction " +
                "WHERE Cat_Transaction.idTransaction =? " +
                "AND Categories.idCategory =  Cat_Transaction.idCategory";
        Cursor c = database.rawQuery(query, new String[]{transactionId});
        if (c.moveToFirst()) {
            do {

                catNames.add(c.getString(c.getColumnIndex(CategoryTable.Cols.CATEGORY_NAME)));

            } while (c.moveToNext());
        }
        return catNames;


    }


    public String getName() {
        return this.name;
    }

    public void updateTransaction(Transaction transaction) {
        String uuidString = transaction.getID().toString();
        ContentValues values = getContentValues(transaction);

        database.update(TransactionTable.NAME, values,
                TransactionTable.Cols.ID_TRANSACTION + " = ?",
                new String[]{uuidString});

    }

    public void removeProfile(String name) {
        database.close();
        context.deleteDatabase(name);
        GlobalScopeContainer.profileList.remove(name);

        if (GlobalScopeContainer.profileList.isEmpty()) {
            GlobalScopeContainer.profileList.add("defaultProfile" + (new Date().toString()) + ".db");
            GlobalScopeContainer.activeProfile = Profile.get(context, GlobalScopeContainer.profileList.get(0));
        }


    }
    public void closeProfile(){
        database.close();
    }


    public void removeTransaction(Transaction t) {
        String uuidString = t.getID().toString();
        database.delete(TransactionTable.NAME, TransactionTable.Cols.ID_TRANSACTION + " = ?", new String[]{uuidString});
        database.delete(CategoryTransactionTable.NAME, CategoryTransactionTable.Cols.ID_TRANSACTION + " = ? ", new String[]{uuidString});

    }

    public void addTransaction(Transaction t) {
        ContentValues values = getContentValues(t);
        database.insert(TransactionTable.NAME, null, values);
    }

    private static ContentValues getContentValues(Transaction transaction) {


        ContentValues values = new ContentValues();
        values.put(TransactionTable.Cols.ID_TRANSACTION, transaction.getID().toString());
        values.put(TransactionTable.Cols.TITLE, transaction.getName());
        values.put(TransactionTable.Cols.DATE, df1.format(transaction.getDate()).toString());
        values.put(TransactionTable.Cols.AMOUNT, transaction.getAmount().toString());

        return values;

    }

    private TransactionCursorWrapper queryTransactions(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
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

    private TransactionCursorWrapper queryTransactionsInOrder(String whereClause, String[] whereArgs, String orderBy) {
        Cursor cursor = database.query(
                TransactionTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                orderBy
        );
        return new TransactionCursorWrapper(cursor);
    }


    private TransactionCursorWrapper queryTransactionsInOrderByCategory() {
        String query = "SELECT * FROM Transactions " +
                "INNER JOIN Cat_Transaction ON Transactions.idTransaction = Cat_Transaction.idTransaction " +
                "INNER JOIN Categories ON Cat_Transaction.idCategory = Categories.idCategory GROUP BY Transactions.idTransaction " +
                "ORDER BY Categories.categoryName DESC";
        Cursor cursor = database.rawQuery(query, new String[]{});
        return new TransactionCursorWrapper(cursor);
    }

    //    String query = "SELECT * FROM Categories, Cat_Transaction " +
//            "WHERE Cat_Transaction.idTransaction =? " +
//            "AND Categories.idCategory =  Cat_Transaction.idCategory";
    public void limitChecker() {
        TransactionCursorWrapper cursor = queryTransactionLimitsCheck();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String title = cursor.getString(cursor.getColumnIndex(CategoryTable.Cols.CATEGORY_NAME));
                Double y = cursor.getDouble(cursor.getColumnIndex("temp"));
                Double z = cursor.getDouble(cursor.getColumnIndex(CategoryTable.Cols.LIMIT_AMOUNT));
                if (y > z) {

                    Toast.makeText(context, "you are over limit in category " + title + " by $" + (y - z), Toast.LENGTH_LONG).show();
                }


                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    private TransactionCursorWrapper queryTransactionLimitsCheck() {
        String query = "SELECT Categories.categoryName, SUM(Transactions.amount) AS temp, Categories.limits " +
                "FROM Transactions " +
                "INNER JOIN Cat_Transaction ON Cat_Transaction.idTransaction = Transactions.idTransaction " +
                "INNER JOIN Categories ON Cat_Transaction.idCategory = Categories.idCategory " +
                "WHERE Categories.limits ";
        Cursor cursor = database.rawQuery(query, new String[]{});
        return new TransactionCursorWrapper(cursor);
    }


    public DataPoint[] getSeries() {

        TransactionCursorWrapper cursor;

        cursor = queryTransactionsInOrder(null, null, "date ASC");
        DataPoint[] series = new DataPoint[cursor.getCount()];
        int i = 0;

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                series[i] = cursor.getDataPoint();
                i++;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return series;

    }



    public DataPoint[] getBarSeries() {
        TransactionCursorWrapper cursor;

        cursor = queryTransactionsSumByCategory();

        DataPoint[] series = new DataPoint[cursor.getCount()];

        int i = 0;
        StringBuilder sb = new StringBuilder();

        CategoryList categoryList = new CategoryList(context);
        List<String> categoryNames = new ArrayList<String>();
        categoryNames.addAll(categoryList.getCategories());


        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                //FIXME: need to redo query.
                //TODO: Needs a different wrapper for BarGraph Data Point
                String name = cursor.getString(cursor.getColumnIndex(CategoryTable.Cols.CATEGORY_NAME));
                Category category = categoryList.getCategory(name);


                Double y = cursor.getDouble(cursor.getColumnIndex("temp"));
                Double x = (double) categoryNames.indexOf(category.getCategoryName()); //needs to be ascending... wtf

                DataPoint temp = new DataPoint(i, y);

                String tempString = i + ", " + y.toString();
                sb.append(tempString + "...");

                series[i] = temp;
                i++;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();

        return series;


    }

    private TransactionCursorWrapper queryTransactionsSumByCategory() {
        String query = "SELECT Categories.categoryName, SUM(Transactions.amount) AS temp " +
                "FROM Transactions " +
                "INNER JOIN Cat_Transaction ON Cat_Transaction.idTransaction = Transactions.idTransaction " +
                "INNER JOIN Categories ON Cat_Transaction.idCategory = Categories.idCategory " +
                "GROUP BY Cat_Transaction.idCategory " +
                "ORDER BY Categories.idCategory ASC";
        Cursor cursor = database.rawQuery(query, new String[]{});
        return new TransactionCursorWrapper(cursor);
    }
    private TransactionCursorWrapper queryTransactionsSumByDate() {
        String query = "SELECT date(Transactions.date) AS date, SUM(Transactions.amount) AS temp " +
                "FROM Transactions " +
                "GROUP BY date " +
                "ORDER BY date ASC";




        Cursor cursor = database.rawQuery(query, new String[]{});
        return new TransactionCursorWrapper(cursor);
    }


    public DataPoint[] getSumSeries() {

        TransactionCursorWrapper cursor;

        //cursor = queryTransactionsInOrder(null, null, "date ASC");
        cursor = queryTransactionsSumByDate();
        BigDecimal amount = BigDecimal.ZERO;



        DataPoint[] series = new DataPoint[cursor.getCount()];
        int i = 0;

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                Date date1;

                try {
                    date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    date1 = new Date();
                }

                Toast.makeText(context, date, Toast.LENGTH_SHORT).show();

                amount =amount.add( new BigDecimal(cursor.getString(cursor.getColumnIndex("temp"))));

                series[i] = new DataPoint(date1, amount.doubleValue() );
                i++;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return series;

    }

}

