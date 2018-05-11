package edu.csuci.myci.cashflow;

import android.widget.Toast;

import java.io.Serializable;
import java.util.UUID;

public class Category implements Serializable {

    private String categoryName;
    private UUID categoryId;

    public Category(String categoryName, UUID categoryId) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public UUID getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String toString() {
        return ("Category: " + categoryName + "\tID: " + categoryId);
    }


    public boolean equals(Object o){
        if(o instanceof Category){
            Category toCompare = (Category) o;
            return this.categoryId.equals(toCompare.categoryId);
        }
        return false;
    }



}
