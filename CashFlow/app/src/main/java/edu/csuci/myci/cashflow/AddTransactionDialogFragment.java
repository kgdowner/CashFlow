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
import android.widget.Spinner;
import android.widget.TextView;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by viktoriya on 4/8/18.
 */



    public  class AddTransactionDialogFragment extends DialogFragment{

        public static final String ADD_TRANSACTION = "edu.csuci.myci.cashflow.transaction";

        private EditText sEditAmount;
        private EditText sEditName;
        private Spinner mCategorySpinner;

        private Category newCategory;
        private Set<Category> tempCats = new HashSet();




    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_add_transaction, container, true);

            Button confirmButton = (Button) view.findViewById(R.id.add_transaction_ok);
            Button cancelButton = (Button) view.findViewById(R.id.add_transaction_cancel);

            sEditAmount = (EditText) view.findViewById(R.id.amountEntry);
            sEditName = (EditText)view.findViewById(R.id.transaction_name);
            mCategorySpinner = (Spinner)view.findViewById(R.id.category_spinner);


            //Transfering Category Names to spinner
            CategoryList categoryList = CategoryList.get(getActivity());
            List<Category> categories = categoryList.getCategories();


            List<String> categoryNames = new ArrayList<>();
            categoryNames.add("");
            for(Category category : categories){
                categoryNames.add(category.getCategoryName());
            }

            //Spinner set up
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, categoryNames);

            mCategorySpinner.setAdapter(dataAdapter);


            //Spinner action
            mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position!=0) {
                        newCategory = new Category(String.valueOf(mCategorySpinner.getSelectedItem()), position - 1);
                        tempCats.add(newCategory);
                        //TODO: ADD window to see all selected categories in add transaction dialog
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            confirmButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    //Validation
                    String name = sEditName.getText().toString();
                    if(TextUtils.isEmpty(name)) {
                        sEditName.setError("Name your transaction please.");
                        return;
                    }
                    String amount = sEditAmount.getText().toString();


                    BigDecimal actualAmount;
                    try {
                        actualAmount =  new BigDecimal(amount);

                    } catch (NumberFormatException e) {
                        sEditAmount.setError("Please enter number.");
                        return;
                    }

                    if(tempCats.size()==0){
                        ((TextView) mCategorySpinner.getSelectedView()).setError("Please choose category");
                        return;
                    }





                    Transaction resultTransaction = new Transaction(actualAmount,tempCats, name);
                    sendResult(Activity.RESULT_OK, resultTransaction);
                    dismiss();
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
    private void sendResult(int resultCode, Transaction transaction){
        if(getTargetFragment() == null){return;}
        Intent intent = new Intent();
        intent.putExtra(ADD_TRANSACTION, transaction);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode, intent  );

    }
}



