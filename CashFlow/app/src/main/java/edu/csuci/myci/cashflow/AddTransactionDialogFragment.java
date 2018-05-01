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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AddTransactionDialogFragment extends DialogFragment {
    private EditText sEditAmount;
    private EditText sEditName;
    private TextView mSelectedCategoryTextView;
    private Spinner mCategorySpinner;
    private CategoryList categoryList;


    public static void display(Context context) {
        DialogFragment df = new AddTransactionDialogFragment();
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        df.setTargetFragment(((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("List_View_Fragment"), 0);
        df.show(ft, "Add_Transaction_Fragment");
    }

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

            mSelectedCategoryTextView = (TextView)view.findViewById(R.id.selected_category_names);
            mSelectedCategoryTextView.setText("");
            mSelectedCategoryTextView.setVisibility(View.INVISIBLE);


            //Transferring Category Names to spinner
            categoryList = new CategoryList(getActivity());

            //will be removed when we add manipulation of categories.
            if(categoryList.getCategories().size()==0){categoryList.populateCatList();}

            final List<String> categoryNames = new ArrayList<String>();
            categoryNames.add(getResources().getString(R.string.category_hint));
            categoryNames.addAll(categoryList.getCategories());

            //Spinner set up
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, categoryNames);

            mCategorySpinner.setAdapter(dataAdapter);
            final UUID tempTransactionID = UUID.randomUUID();
            final ArrayList<UUID>temCatStorage=new ArrayList<>();


            //Spinner action
            mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                    if(position!=0) {
                        temCatStorage.add(categoryList.getCategory(mCategorySpinner.getSelectedItem().toString()).getCategoryId());
                       mSelectedCategoryTextView.append(categoryNames.get(position)+", ");
                       mSelectedCategoryTextView.setVisibility(View.VISIBLE);
                       //mSelectedCategoryTextView.setLayoutParams();
                    }

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

                    BigDecimal actualAmount;



                    //Validation
                    if(TextUtils.isEmpty(name)) {
                        sEditName.setError("Name your transaction please.");
                        return;
                    }

                    try {
                        actualAmount =  new BigDecimal(amount);
                    } catch (NumberFormatException e) {
                        sEditAmount.setError("Please enter number.");
                        return;
                    }

                    for (UUID id : temCatStorage)
                    {
                        categoryList.addCategoryTransaction(id, tempTransactionID);
                    }

                    if(temCatStorage.size()==0){
                        ((TextView) mCategorySpinner.getSelectedView()).setError("Please choose category");
                        return;
                    }

                    Transaction resultTransaction = new Transaction(actualAmount, name);
                    resultTransaction.setID(tempTransactionID);
                    GlobalScopeContainer.activeProfile.addTransaction(resultTransaction);


                    // TODO: replace this with code calling ListViewFragment.updateUI() here
                    // TODO: and add similar method + code for the graph views
                    getTargetFragment().onActivityResult(0, 0, null);


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
}



