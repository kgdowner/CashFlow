package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Intent;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by viktoriya on 4/8/18.
 */



    public  class AddTransactionFragment extends DialogFragment{

        public static final String ADD_TRANSACTION = "edu.csuci.myci.cashflow.transaction";

        private static EditText sEditAmount;
        private static EditText sEditName;
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



            mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position!=0) {newCategory = new Category(String.valueOf(mCategorySpinner.getSelectedItem()),position);}
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });





            confirmButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String name = sEditName.getText().toString();

                    String amount = sEditAmount.getText().toString();
                    BigDecimal amount2 = new BigDecimal(amount);

                    Set<Category> tempCats = new HashSet();
                    tempCats.add(newCategory);

                    Transaction resultTransaction = new Transaction(amount2,tempCats, name);
                    sendResult(Activity.RESULT_OK, resultTransaction);
                    dismiss();


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
    private void sendResult(int resultCode, Transaction transaction){
        if(getTargetFragment() == null){return;}
        Intent intent = new Intent();
        intent.putExtra(ADD_TRANSACTION, transaction);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode, intent  );

    }
}



