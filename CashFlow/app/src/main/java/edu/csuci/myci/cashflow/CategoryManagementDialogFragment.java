package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CategoryManagementDialogFragment extends DialogFragment {
    //tag for passing Transaction Object from CategoryManagement to ListFragment
    public static final String MANAGE_CATEGORY = "edu.csuci.myci.cashflow.category";

    private EditText sEditName;
    private RadioGroup mCategoryRadioGroup;

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
        //sEditName = (EditText)view.findViewById(R.id.new_category_name);
        //mCategoryRadioGroup = (RadioGroup) view.findViewById(R.id.category_management_radioGroup);


        // acquire layout object references
        this.categories             = (RadioGroup) view.findViewById(R.id.radiogroup_category_list);
        this.buttonAddCategory      = (Button) view.findViewById(R.id.button_add_category);
        this.buttonRemoveCategory   = (Button) view.findViewById(R.id.button_remove_category);
        this.buttonCancel           = (Button) view.findViewById(R.id.button_cancel);


        //Transferring Category Names to spinner
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

    private void sendResult(int resultCode, Category category) {
        //TODO: need to pass categoryList to ListView.... :(
//        if(getTargetFragment() == null){return;}
//        Intent intent = new Intent();
//        intent.putExtra(MANAGE_CATEGORY, category);
//
//        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode, intent  );
        //at result update UI.

        // TODO:
        //
    }

    private void populateRadioGroup(List<String>categoryNames) {
        for ( String currentCategory: categoryNames ) {
            RadioButton rb = new RadioButton(getContext());
            rb.setText(currentCategory);
            mCategoryRadioGroup.addView(rb);
        }

    }

    private void onAddCategory() {
        String name = sEditName.getText().toString();
        if(TextUtils.isEmpty(name)) {
            sEditName.setError("Name your transaction please.");
            return;
        }
        //FIXME: need to remove personal control over categoryID!!!
        Category tempCat = new Category(name,9);

        categoryList.addCategory(tempCat);
        RadioButton rb = new RadioButton(getContext());
        rb.setText(name);
        mCategoryRadioGroup.addView(rb);
        sendResult(Activity.RESULT_OK, tempCat);
        //populateRadioGroup(categoryNames);
    }
    private void onRemoveCategory() {}
    private void onCancel() {
        dismiss();
    }

}
