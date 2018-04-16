package edu.csuci.myci.cashflow;

import android.database.sqlite.SQLiteDatabase;

public class Profile {
    private String name;
    private SQLiteDatabase database;


    public Profile(String name) {
        this.name = name;

        // TODO: this!
        // Look for a database file of this name in the file system
            // if not found, create a new database for this profile
            // if found, load the database and store it in this.database
    }

    public String getName() {
        return this.name;
    }
}