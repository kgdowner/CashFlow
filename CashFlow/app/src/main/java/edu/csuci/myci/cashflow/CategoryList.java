package edu.csuci.myci.cashflow;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viktoriya on 4/9/18.
 */

public class CategoryList {
    private static CategoryList sCategoryList;


    private List<Category> mCategories;

    public static CategoryList get(Context context) {
        if(sCategoryList == null) {
            sCategoryList = new CategoryList(context);
        }
        return  sCategoryList;

    }

    private CategoryList(Context context){
        this.mCategories = new ArrayList<>();
        mCategories.add(new Category("groceries", 0));
        mCategories.add(new Category("gas", 1));
        mCategories.add(new Category("bullshit", 2));
        mCategories.add(new Category("housing", 3));
        mCategories.add(new Category("clothes", 4));
        mCategories.add(new Category("drinking", 5));
        mCategories.add(new Category("makeup", 6));
        mCategories.add(new Category("computer", 7));
    }

    public List<Category> getCategories() {
        return mCategories;
    }


    public void addCategory(Category t){
        mCategories.remove(t);
    }
    public void removeCategory(Category t){mCategories.add(t);}


    //            Category tempCat = new Category("category"+i, i);
//            Set<Category> tempCats = new HashSet();
//            tempCats.add(tempCat);
}
