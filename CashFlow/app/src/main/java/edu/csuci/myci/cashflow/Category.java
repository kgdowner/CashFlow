    package edu.csuci.myci.cashflow;

    import java.io.Serializable;
    import java.util.UUID;

    /**
 * Created by john.miller415 on 3/28/18.
 */

public class Category implements Serializable{

    private String categoryName;
    private UUID categoryId;

    public Category(String categoryName, UUID categoryId){
        this.categoryName = categoryName;
        this.categoryId= categoryId;
    }

    public String getCategoryName(){
        return this.categoryName;
    }

    public UUID getCategoryId(){
        return this.categoryId;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public void setCategoryId(UUID categoryId){
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
