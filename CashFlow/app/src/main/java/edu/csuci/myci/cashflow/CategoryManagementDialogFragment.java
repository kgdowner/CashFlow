package edu.csuci.myci.cashflow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
    private EditText mNewCategoryName;

    private CategoryList categoryList;
    private List<String> categoryNames;

    private RadioGroup mCategoriesRadioGroup;
    private Button buttonAddCategory;
    private Button buttonRemoveCategory;
    private Button buttonCancel;
    private Button renameCategoryButton;

    private int checkedButtonId = -1;

    public static void display(Context context) {
        DialogFragment df = new CategoryManagementDialogFragment();
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        df.setTargetFragment(((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("List_View_Fragment"), 0);
        df.show(ft, "Category_Management_Fragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category_management, container, true);

        // acquire layout object references
        this.mCategoriesRadioGroup = (RadioGroup) view.findViewById(R.id.radiogroup_category_list);
        this.buttonAddCategory = (Button) view.findViewById(R.id.button_add_category);
        this.buttonRemoveCategory = (Button) view.findViewById(R.id.button_remove_category);
        this.buttonCancel = (Button) view.findViewById(R.id.button_cancel);
        this.mNewCategoryName = (EditText) view.findViewById(R.id.new_category_name);
        this.renameCategoryButton = (Button) view.findViewById(R.id.button_rename_category);


        //Transferring Category Names to RadioGroup
        categoryList = new CategoryList(getContext());

        //will be removed when we add manipulation of mCategoriesRadioGroup.
        if (categoryList.getCategories().size() == 0) {
            categoryList.populateCatList();
        }

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
        this.buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddCategory();
            }
        });
        this.buttonRemoveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRemoveCategory();
            }
        });
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
        this.renameCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRename();
            }
        });


        return view;
    }

    private void populateRadioGroup(List<String> categoryNames) {
        for (String currentCategory : categoryNames) {
            RadioButton rb = new RadioButton(getContext());
            rb.setText(currentCategory);
            mCategoriesRadioGroup.addView(rb);
        }
    }

    private void onAddCategory() {
        String name = mNewCategoryName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mNewCategoryName.setError("Name your Category please.");
            return;
        }
        if (categoryList.getCategory(name) != null) {
            mNewCategoryName.setError("Pick a different name please");
            return;
        }

        Category tempCat = new Category(name, UUID.randomUUID());
        categoryList.addCategory(tempCat);

        RadioButton rb = new RadioButton(getContext());
        rb.setText(name);
        mCategoriesRadioGroup.addView(rb);

        // FIXME: add non-activity-result updating of list\graph views
        getTargetFragment().onActivityResult(0, 0, null);  // FIXME: for now just update list view

        mNewCategoryName.setText("");
    }

    private void onRemoveCategory() {
        int selectedCat = checkedButtonId;

        if (selectedCat == -1) {
            Toast.makeText(getActivity(), "Please selecte category to remove ", Toast.LENGTH_LONG).show();


        } else {

            RadioButton button = (RadioButton) mCategoriesRadioGroup.findViewById(selectedCat);

            String name = (String) button.getText();

            categoryList.removeCategory(name);
            categoryNames.remove(name);

            // FIXME: add non-activity-result updating of list\graph views
            getTargetFragment().onActivityResult(0, 0, null);  // FIXME: for now just update list view

            mCategoriesRadioGroup.removeView(button);
        }
    }


    private void onCancel() {
        dismiss();
    }

    private void onRename() {
        if (checkedButtonId == -1) {
            Toast.makeText(getActivity(), "Please selecte category to rename ", Toast.LENGTH_LONG).show();
        } else {
            String newCategoryName = mNewCategoryName.getText().toString();
            if (TextUtils.isEmpty(newCategoryName)) {
                mNewCategoryName.setError("Name your Category please.");
                return;
            }
            RadioButton button = (RadioButton) mCategoriesRadioGroup.findViewById(checkedButtonId);

            //get category from button name and rename
            String oldCategoryName = (String) button.getText();
            Category category = categoryList.getCategory(oldCategoryName);
            category.setCategoryName(newCategoryName);

            categoryList.updateCategory(category);

            //remove old button and name from nameList
            mCategoriesRadioGroup.removeView(button);
            categoryNames.remove(oldCategoryName);

            //create new Button with new name
            RadioButton rb = new RadioButton(getContext());
            rb.setText(newCategoryName);
            mCategoriesRadioGroup.addView(rb);

            getTargetFragment().onActivityResult(0, 0, null);  // FIXME: for now just update list view


        }

    }
}
