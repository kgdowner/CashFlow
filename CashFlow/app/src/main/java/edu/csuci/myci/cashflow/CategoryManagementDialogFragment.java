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


/**
 * Created by viktoriya on 4/16/18.
 */

public class CategoryManagementDialogFragment extends DialogFragment{
    //tag for passing Transaction Object from CategoryManagement to ListFragment
    public static final String MANAGE_CATEGORY = "edu.csuci.myci.cashflow.category";
    public static final String REMOVE_CATEGORY = "edu.csuci.myci.cahsflow.categoryID";


    private EditText sEditName;
    private RadioGroup mCategoryRadioGroup;

    private CategoryList categoryList;
    private List<String> categoryNames;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category_management, container, true);

        Button addCategoryButton = (Button) view.findViewById(R.id.add_category_button);
        final Button removeCategoryButthon = (Button) view.findViewById(R.id.remove_category_button);
        Button cancelButton = (Button) view.findViewById(R.id.manage_categroy_cancel_button);

        sEditName = (EditText)view.findViewById(R.id.new_category_name);
        mCategoryRadioGroup = (RadioGroup) view.findViewById(R.id.category_management_radioGroup);


        //Transfering Category Names to spinner
        categoryList = CategoryList.get(getContext());

        //will be removed when we add manipulation of categories.
        if(categoryList.getCategories().size()==0){categoryList.populateCatList();}

        categoryNames = new ArrayList<String>();
        categoryNames.addAll(categoryList.getCategories());

        //RadioGroup Set up

        populateRadioGroup(categoryNames);






        addCategoryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = sEditName.getText().toString();
                if(TextUtils.isEmpty(name)) {
                    sEditName.setError("Name your transaction please.");
                    return;
                }
                //FIXME: need to remove personal control over categoryID!!!
                Category tempCat = new Category(name,mCategoryRadioGroup.getChildCount());
                Toast.makeText(getActivity(),"you are adding id"+mCategoryRadioGroup.getChildCount(),Toast.LENGTH_LONG).show();

                categoryList.addCategory(tempCat);
                RadioButton rb = new RadioButton(getContext());
                rb.setText(name);
                mCategoryRadioGroup.addView(rb);
                sendResult(Activity.RESULT_OK, tempCat);
                //populateRadioGrou(categoryNames);
            }
        });

        removeCategoryButthon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int selectedCat = mCategoryRadioGroup.getCheckedRadioButtonId();

                if(selectedCat == -1){
                    Toast.makeText(getActivity(),"Please selecte category to remove ",Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(getActivity(), "Category to remove id " + selectedCat, Toast.LENGTH_LONG).show();

                    RadioButton button = (RadioButton)mCategoryRadioGroup.findViewById(selectedCat);

                    String name = (String)button.getText();
                    Category tempCategory = categoryList.getCategory(name);
                    categoryList.removeCategory(tempCategory.getCategoryId());

                    categoryNames.remove(name);


                    mCategoryRadioGroup.removeViewAt(selectedCat);
                    sendResult2(Activity.RESULT_CANCELED, selectedCat - 1);
                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        return view;
    }

    private void sendResult(int resultCode, Category category){
        if(getTargetFragment() == null){return;}
        Intent intent = new Intent();
        intent.putExtra(MANAGE_CATEGORY, category);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode, intent  );
        //at result update UI.
    }
    private void sendResult2(int resultCode, int categoryID){
        if(getTargetFragment() == null){return;}
        Intent intent = new Intent();
        intent.putExtra(REMOVE_CATEGORY, categoryID);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode, intent  );
        //at result update UI.
    }

    private void populateRadioGroup(List<String>categoryNames){
        for ( String currentCategory: categoryNames ) {
            RadioButton rb = new RadioButton(getContext());
            rb.setText(currentCategory);
            mCategoryRadioGroup.addView(rb);
        }


    }


}
