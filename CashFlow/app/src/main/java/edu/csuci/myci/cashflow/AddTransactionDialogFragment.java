package edu.csuci.myci.cashflow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AddTransactionDialogFragment extends DialogFragment {
    private EditText editAmount;
    private EditText editName;
    private TextView selectedCategoryText;
    private Spinner categorySpinner;
    private CategoryList categoryList;
    private Button signButton;

    private BigDecimal actualAmount;
    private String newAmount;


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

        editAmount = (EditText) view.findViewById(R.id.amountEntry);
        editName = (EditText) view.findViewById(R.id.transaction_name);
        categorySpinner = (Spinner) view.findViewById(R.id.category_spinner);
        signButton = (Button) view.findViewById(R.id.sign_button);


        selectedCategoryText = (TextView) view.findViewById(R.id.selected_category_names);
        selectedCategoryText.setText("");
        selectedCategoryText.setVisibility(View.INVISIBLE);


        //Transferring Category Names to spinner
        categoryList = new CategoryList(getActivity());

        //will be removed when we add manipulation of categories.
        if (categoryList.getCategories().size() == 0) {
            categoryList.populateCatList();
        }

        final List<String> categoryNames = new ArrayList<String>();
        categoryNames.add(getResources().getString(R.string.category_hint));
        categoryNames.addAll(categoryList.getCategories());

        //Spinner set up
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, categoryNames);

        categorySpinner.setAdapter(dataAdapter);
        final UUID tempTransactionID = UUID.randomUUID();
        final ArrayList<UUID> tempCatStorage = new ArrayList<>();


        //Spinner action
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    tempCatStorage.clear();
                    tempCatStorage.add(categoryList.getCategory(categorySpinner.getSelectedItem().toString()).getCategoryId());
                    //selectedCategoryText.append(categoryNames.get(position) + ", ");
                    selectedCategoryText.setText(categoryNames.get(position));
                    
                    selectedCategoryText.setVisibility(View.VISIBLE);
                    //selectedCategoryText.setLayoutParams();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editAmount.addTextChangedListener(new TextWatcher() {
            boolean _ignore = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //newAmount = s.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newAmount = s.toString();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (_ignore)
                    return;

                _ignore = true; // prevent infinite loop
                // Change your text here.

                signButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "you clicked sign button", Toast.LENGTH_LONG)
                                .show();
                        if(s.toString().contains("-")){
                            signButton.setText("-");
                            String temp = s.toString();
                            editAmount.setText(temp.replace("-", ""));
                        } else {
                            signButton.setText("+");
                            String temp = s.toString();
                            editAmount.setText("-"+temp);
                        }
                        //if string has - on front, remove it, else append.
                    }
                });
                _ignore = false; // release, so the TextWatcher start to listen again.
            }
        });



        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();

                //Validation
                if (TextUtils.isEmpty(name)) {
                    editName.setError("Name your transaction please.");
                    return;
                }
                if (TextUtils.isEmpty(newAmount)) {
                    editAmount.setError("Enter amount please");
                    return;
                }

                try {
                    actualAmount = new BigDecimal(newAmount);
                } catch (NumberFormatException e) {
                    editAmount.setError("Please enter number.");
                    return;
                }

                for (UUID id : tempCatStorage) {
                    categoryList.addCategoryTransaction(id, tempTransactionID);
                }


                if (tempCatStorage.size() == 0) {
                    if(categoryList.getCategory("") == null) {
                        categoryList.addCategory(new Category("", UUID.randomUUID()));
                    }
                    categoryList.addCategoryTransaction(categoryList.getCategory("").getCategoryId(), tempTransactionID);
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



