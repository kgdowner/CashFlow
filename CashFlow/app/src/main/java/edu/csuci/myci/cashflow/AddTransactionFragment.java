package edu.csuci.myci.cashflow;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by viktoriya on 4/8/18.
 */



    public  class AddTransactionFragment extends DialogFragment{

        private static EditText sEditAmount;
        private Spinner mCategorySpinner;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_add_transaction, container, true);

            Button confirmButton = (Button) view.findViewById(R.id.add_transaction_ok);


            sEditAmount = (EditText) view.findViewById(R.id.amountEntry);
            mCategorySpinner = (Spinner)view.findViewById(R.id.category_spinner);
            Category newCategory;



            mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //newCategory = new Category(String.valueOf(mCategorySpinner.getSelectedItem()),position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });





            confirmButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                   // Transaction resultTansaction = new Transaction(sEditAmount, newCategory);
                    //define new transaction
                    //set transaction values to amount, category, id
                    //pass transaction to fragment... list... enter it into profile array...
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
    }



