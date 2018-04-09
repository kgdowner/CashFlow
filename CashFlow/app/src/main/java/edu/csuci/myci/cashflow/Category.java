    package edu.csuci.myci.cashflow;

/**
 * Created by john.miller415 on 3/28/18.
 */

public class Category {

    private String categoryName;
    private int categoryId;

    public Category(String categoryName, int categoryId){
        this.categoryName = categoryName;
        this.categoryId= categoryId;
    }

    public String getCategoryName(){
        return this.categoryName;
    }

    public int getCategoryId(){
        return this.categoryId;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public void setCategoryId(int categoryId){
        this.categoryId = categoryId;
    }

    public String toString(){
        return ("Category: " + categoryName + "\tID: " + categoryId);
    }

    public boolean equals(Category inputCategory) {
        if(this.categoryName.equalsIgnoreCase(inputCategory.getCategoryName())
                && this.categoryId == inputCategory.getCategoryId() ){
            return true;
        }else{
            return false;
        }
    }
}
