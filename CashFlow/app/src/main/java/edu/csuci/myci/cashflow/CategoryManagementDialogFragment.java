package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;



public class CategoryManagementDialogFragment extends DialogFragment {
    //tag for passing Transaction Object from CategoryManagement to ListFragment
    public static final String MANAGE_CATEGORY = "edu.csuci.myci.cashflow.category";
    public static final String REMOVE_CATEGORY = "edu.csuci.myci.cahsflow.categoryID";

    private EditText mNewCategoryName;

    private CategoryList categoryList;
    private List<String> categoryNames;

    private RadioGroup categories;
    private Button buttonAddCategory;
    private Button buttonRemoveCategory;
    private Button buttonCancel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category_management, container, true);

        // acquire layout object references
        this.categories             = (RadioGroup) view.findViewById(R.id.radiogroup_category_list);
        this.buttonAddCategory      = (Button) view.findViewById(R.id.button_add_category);
        this.buttonRemoveCategory   = (Button) view.findViewById(R.id.button_remove_category);
        this.buttonCancel           = (Button) view.findViewById(R.id.button_cancel);
        this.mNewCategoryName       = (EditText) view.findViewById(R.id.new_category_name);


        //Transferring Category Names to RadioGroup
        categoryList = CategoryList.get(getContext());

        //will be removed when we add manipulation of categories.
        if(categoryList.getCategories().size()==0){categoryList.populateCatList();}

        categoryNames = new ArrayList<String>();
        categoryNames.addAll(categoryList.getCategories());

        //RadioGroup Set up
        populateRadioGroup(categoryNames);

        // register button listener functions
        this.buttonAddCategory.setOnClickListener(     new View.OnClickListener() {@Override public void onClick(View v) {onAddCategory();      }});
        this.buttonRemoveCategory.setOnClickListener(  new View.OnClickListener() {@Override public void onClick(View v) {onRemoveCategory();   }});
        this.buttonCancel.setOnClickListener(          new View.OnClickListener() {@Override public void onClick(View v) {onCancel();           }});


        return view;
    }

    private void populateRadioGroup(List<String>categoryNames) {
        for ( String currentCategory: categoryNames ) {
            RadioButton rb = new RadioButton(getContext());
            rb.setText(currentCategory);
            categories.addView(rb);
        }
    }

    private void onAddCategory() {
        String name = mNewCategoryName.getText().toString();
        if(TextUtils.isEmpty(name)) {
            mNewCategoryName.setError("Name your Category please.");
            return;
        }

        Category tempCat = new Category(name,categories.getChildCount());

        categoryList.addCategory(tempCat);
        RadioButton rb = new RadioButton(getContext());
        rb.setText(name);
        categories.addView(rb);
        sendResult(Activity.RESULT_OK, tempCat);
    }

    private void onRemoveCategory() {
        int selectedCat = categories.getCheckedRadioButtonId();

        if(selectedCat == -1){
            Toast.makeText(getActivity(),"Please selecte category to remove ",Toast.LENGTH_LONG).show();


        } else {
            Toast.makeText(getActivity(), "Category to remove id " + selectedCat, Toast.LENGTH_LONG).show();

            RadioButton button = (RadioButton)categories.findViewById(selectedCat);

            String name = (String)button.getText();
            Category tempCategory = categoryList.getCategory(name);
            categoryList.removeCategory(tempCategory.getCategoryId());

            categoryNames.remove(name);


            categories.removeViewAt(selectedCat);
            sendResult2(Activity.RESULT_CANCELED, selectedCat - 1);
        }
    }


    private void onCancel() {
        dismiss();
    }
    private void sendResult(int resultCode, Category category) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(MANAGE_CATEGORY, category);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }


    private void sendResult2(int resultCode, int categoryID){
        if(getTargetFragment() == null){return;}
        Intent intent = new Intent();
        intent.putExtra(REMOVE_CATEGORY, categoryID);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode, intent  );
        //at result update UI.
    }

}
