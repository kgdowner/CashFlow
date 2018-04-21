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
import java.util.UUID;


public class CategoryManagementDialogFragment extends DialogFragment {
    //tag for passing Transaction Object from CategoryManagement to ListFragment
    public static final String MANAGE_CATEGORY = "edu.csuci.myci.cashflow.category";
    public static final String REMOVE_CATEGORY = "edu.csuci.myci.cahsflow.categoryID";

    private EditText mNewCategoryName;

    private CategoryList categoryList;
    private List<String> categoryNames;

    private RadioGroup mCategoriesRadioGroup;
    private Button buttonAddCategory;
    private Button buttonRemoveCategory;
    private Button buttonCancel;

    private int checkedButtonId = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category_management, container, true);

        // acquire layout object references
        this.mCategoriesRadioGroup = (RadioGroup) view.findViewById(R.id.radiogroup_category_list);
        this.buttonAddCategory      = (Button) view.findViewById(R.id.button_add_category);
        this.buttonRemoveCategory   = (Button) view.findViewById(R.id.button_remove_category);
        this.buttonCancel           = (Button) view.findViewById(R.id.button_cancel);
        this.mNewCategoryName       = (EditText) view.findViewById(R.id.new_category_name);


        //Transferring Category Names to RadioGroup
        categoryList = CategoryList.get(getContext());

        //will be removed when we add manipulation of mCategoriesRadioGroup.
        if(categoryList.getCategories().size()==0){categoryList.populateCatList();}

        categoryNames = new ArrayList<String>();
        categoryNames.addAll(categoryList.getCategories());

        //RadioGroup Set up
        populateRadioGroup(categoryNames);

        this.mCategoriesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedButtonId = checkedId;
            }
        });

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
            mCategoriesRadioGroup.addView(rb);
        }
    }

    private void onAddCategory() {
        String name = mNewCategoryName.getText().toString();
        if(TextUtils.isEmpty(name)) {
            mNewCategoryName.setError("Name your Category please.");
            return;
        }
        if(categoryList.getCategory(name)!=null){
            mNewCategoryName.setError("Pick a different name please");
            return;
        }

        Category tempCat = new Category(name, UUID.randomUUID());
        //FIXME: this can cause category overlay.... get some other method of setting id.

        categoryList.addCategory(tempCat);

        RadioButton rb = new RadioButton(getContext());
        rb.setText(name);
        mCategoriesRadioGroup.addView(rb);

        sendResult(Activity.RESULT_OK, tempCat);
        mNewCategoryName.setText("");
    }

    private void onRemoveCategory() {
        int selectedCat = checkedButtonId;

        if(selectedCat == -1){
            Toast.makeText(getActivity(),"Please selecte category to remove ",Toast.LENGTH_LONG).show();


        } else {

            RadioButton button = (RadioButton) mCategoriesRadioGroup.findViewById(selectedCat);

            String name = (String)button.getText();

            categoryList.removeCategory(name);
            categoryNames.remove(name);
            //FIXME: when removing mCategoriesRadioGroup, make sure to remove correct cat/transaction link and correct category

            sendResult2(Activity.RESULT_CANCELED, name);
            dismiss();
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


    private void sendResult2(int resultCode, String categoryName){
        if(getTargetFragment() == null){return;}
        Intent intent = new Intent();
        intent.putExtra(REMOVE_CATEGORY, categoryName);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode, intent  );
        //at result update UI.
    }

}
