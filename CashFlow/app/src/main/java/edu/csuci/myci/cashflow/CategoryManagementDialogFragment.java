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
    private CategoryList categoryList;
    private List<String> categoryNames;

    private EditText textNewCategoryName;
    private RadioGroup radioGroupCategories;
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
        this.radioGroupCategories       = (RadioGroup)  view.findViewById(R.id.radio_group_category_list);
        this.buttonAddCategory          = (Button)      view.findViewById(R.id.button_add_category);
        this.buttonRemoveCategory       = (Button)      view.findViewById(R.id.button_remove_category);
        this.buttonCancel               = (Button)      view.findViewById(R.id.button_cancel);
        this.textNewCategoryName        = (EditText)    view.findViewById(R.id.new_category_name);
        this.renameCategoryButton       = (Button)      view.findViewById(R.id.button_rename_category);


        //Transferring Category Names to RadioGroup
        this.categoryList = new CategoryList(getContext());

        //will be removed when we add manipulation of this.this.radioGroupCategories.
        if (this.categoryList.getCategories().size() == 0) {
            this.categoryList.populateCatList();
        }

        this.categoryNames = new ArrayList<String>();
        this.categoryNames.addAll(this.categoryList.getCategories());

        //RadioGroup Set up
        populateRadioGroup(this.categoryNames);

        this.radioGroupCategories.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
            this.radioGroupCategories.addView(rb);
        }
    }

    private void onAddCategory() {
        String name = this.textNewCategoryName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            this.textNewCategoryName.setError("Name your Category please.");
            return;
        }
        if (this.categoryList.getCategory(name) != null) {
            this.textNewCategoryName.setError("Pick a different name please");
            return;
        }

        Category tempCat = new Category(name, UUID.randomUUID());
        this.categoryList.addCategory(tempCat);

        RadioButton rb = new RadioButton(getContext());
        rb.setText(name);
        this.radioGroupCategories.addView(rb);

        // FIXME: add non-activity-result updating of list\graph views
        getTargetFragment().onActivityResult(0, 0, null);  // FIXME: for now just update list view

        this.textNewCategoryName.setText("");
    }

    private void onRemoveCategory() {
        int selectedCat = this.checkedButtonId;

        if (selectedCat == -1) {
            Toast.makeText(getActivity(), "Please select category to remove ", Toast.LENGTH_LONG).show();


        } else {

            RadioButton button = (RadioButton) this.radioGroupCategories.findViewById(selectedCat);

            String name = (String) button.getText();

            this.categoryList.removeCategory(name);
            this.categoryNames.remove(name);

            // FIXME: add non-activity-result updating of list\graph views
            getTargetFragment().onActivityResult(0, 0, null);  // FIXME: for now just update list view

            this.radioGroupCategories.removeView(button);
        }
    }


    private void onCancel() {
        dismiss();
    }

    private void onRename() {
        if (this.checkedButtonId == -1) {
            Toast.makeText(getActivity(), "Please select category to rename ", Toast.LENGTH_LONG).show();
        } else {
            String newCategoryName = this.textNewCategoryName.getText().toString();
            if (TextUtils.isEmpty(newCategoryName)) {
                this.textNewCategoryName.setError("Name your Category please.");
                return;
            }
            RadioButton button = (RadioButton) this.radioGroupCategories.findViewById(this.checkedButtonId);

            //get category from button name and rename
            String oldCategoryName = (String) button.getText();
            Category category = this.categoryList.getCategory(oldCategoryName);
            category.setCategoryName(newCategoryName);

            this.categoryList.updateCategory(category);

            //remove old button and name from nameList
            this.radioGroupCategories.removeView(button);
            this.categoryNames.remove(oldCategoryName);

            //create new Button with new name
            RadioButton rb = new RadioButton(getContext());
            rb.setText(newCategoryName);
            this.radioGroupCategories.addView(rb);

            getTargetFragment().onActivityResult(0, 0, null);  // FIXME: for now just update list view


        }

    }
}
