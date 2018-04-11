package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by viktoriya on 4/8/18.
 */



    public  class AddTransactionFragment extends DialogFragment{

        public static final String ADD_TRANSACTION = "edu.csuci.myci.cashflow.transaction";

        private EditText sEditAmount;
        private EditText sEditName;
        private Spinner mCategorySpinner;
        private Category newCategory;



    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_add_transaction, container, true);

            Button confirmButton = (Button) view.findViewById(R.id.add_transaction_ok);


            sEditAmount = (EditText) view.findViewById(R.id.amountEntry);
            sEditName = (EditText)view.findViewById(R.id.transaction_name);
            mCategorySpinner = (Spinner)view.findViewById(R.id.category_spinner);


            //TODO: more than 1 category input
            //start: will get replaced by single database query
            CategoryList categoryList = CategoryList.get(getActivity());
            List<Category> categories = categoryList.getCategories();


            List<String> categoryNames = new ArrayList<>();
            for(Category category : categories){
                categoryNames.add(category.getCategoryName());
            }
            //end;

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, categoryNames);

            mCategorySpinner.setAdapter(dataAdapter);



            mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    newCategory = new Category(String.valueOf(mCategorySpinner.getSelectedItem()),position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            confirmButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
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


                    Set<Category> tempCats = new HashSet();
                    tempCats.add(newCategory);

                    Transaction resultTransaction = new Transaction(actualAmount,tempCats, name);
                    sendResult(Activity.RESULT_OK, resultTransaction);
                    dismiss();
                }
            });

            Button cancelButton = (Button) view.findViewById(R.id.add_transaction_cancel);
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



