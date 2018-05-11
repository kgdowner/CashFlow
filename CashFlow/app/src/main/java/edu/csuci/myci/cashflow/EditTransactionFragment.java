package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EditTransactionFragment extends Fragment {
    public static final String EDIT_TRANSACTION = "edu.csuci.myci.cashflow.transaction_edit";

    private static final String ARG_TRANSACTION_ID = "transaction_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private Transaction transaction;
    private Profile currentProfile;
    private CategoryList categoryList;

    private EditText title;
    private EditText amount;
    private Button buttonChangeDate;
    private Button buttonOk;
    private Button buttonCancel;
    private Button buttonSign;
    private Spinner categorySpinner;

    private RecyclerView editTransactionRecyclerView;
    private CategoryAdapter categoryAdapter;
    private TextView categoryName;

    private String newAmount;
    private String newName; //moving actual changing of stuff to OK button
    private BigDecimal actualAmount;

    List<Category> categoriesForTransaction;


    public static void display(Context context, UUID transactionID) {
        EditTransactionFragment fragment = new EditTransactionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRANSACTION_ID, transactionID);
        fragment.setArguments(args);

        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_place, fragment, "Edit_transaction_fragment").addToBackStack("Edit_transaction_fragment");
        ft.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentProfile = GlobalScopeContainer.activeProfile;
        UUID transactionID = (UUID) getArguments().getSerializable(ARG_TRANSACTION_ID);
        transaction = currentProfile.getTransactions(transactionID);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_transaction, container, false);
        categoryList = new CategoryList(getActivity());
        categoriesForTransaction = categoryList.getAllCategoriesForTransaction(transaction.getID().toString());


        // acquire layout handles
        title               = (EditText)    v.findViewById(R.id.transaction_name);
        amount              = (EditText)    v.findViewById(R.id.transaction_amount);
        buttonChangeDate    = (Button)      v.findViewById(R.id.transaction_date);
        buttonOk            = (Button)      v.findViewById(R.id.edit_transaction_ok);
        buttonCancel        = (Button)      v.findViewById(R.id.edit_transaction_cancel);
        categorySpinner     = (Spinner)     v.findViewById(R.id.edit_transaction_category_spinner);
        buttonSign          = (Button)      v.findViewById(R.id.sign_button_edit);

        this.editTransactionRecyclerView = (RecyclerView) v.findViewById(R.id.edit_transaction_recyclerView);
        editTransactionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateList();

        // setup the spinner
        final List<String> categoryNames = new ArrayList<String>();
        categoryNames.add(getResources().getString(R.string.category_hint));
        categoryNames.addAll(categoryList.getCategories());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, categoryNames);

        categorySpinner.setAdapter(dataAdapter);
        if(!categoriesForTransaction.isEmpty()) {
            categorySpinner.setSelection(dataAdapter.getPosition(categoriesForTransaction.get(0).getCategoryName()), true);
        }
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> tempAdapter = (ArrayAdapter<String>) parent.getAdapter();

                Category newCategory;
                if(tempAdapter.getItem(position).equals("") || tempAdapter.getItem(position).equals(getResources().getString(R.string.category_hint))) {
                    if(categoryList.getCategory("") == null) {
                        categoryList.addCategory(new Category("", UUID.randomUUID()));
                    }
                    newCategory = categoryList.getCategory("");
                } else {
                    newCategory = categoryList.getCategory(categorySpinner.getSelectedItem().toString());
                }

                for(Category cat : categoryList.getAllCategoriesForTransaction(transaction.getID() + "")) {
                    categoryList.removeCategoryTransaction(cat.getCategoryId(), transaction.getID());
                }
                categoryList.addCategoryTransaction(newCategory.getCategoryId(), transaction.getID());

                updateList();
                parent.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        title.setText(transaction.getName());
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        amount.setText(transaction.getAmount().toString());

        amount.addTextChangedListener(new TextWatcher() {
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

                newAmount = s.toString();
            }
        });

        updateDate();
        buttonChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(transaction.getDate());
                dialog.setTargetFragment(EditTransactionFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);


            }
        });

        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "you clicked sign button", Toast.LENGTH_LONG)
                        .show();

                if (amount.getText().toString().contains("-")) {
                    amount.setText(amount.getText().toString().replace("-", ""));

                } else {
                    amount.setText("-" + amount.getText());

                }
            }
            //if string has - on front, remove it, else append.
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newAmount != null) {
                    try {
                        actualAmount = new BigDecimal(newAmount);
                    } catch (NumberFormatException e) {
                        amount.setError("Please enter number.");
                        return;
                    }
                    transaction.setAmount(actualAmount);
                }
                if (newName != null) {
                    transaction.setName(newName);
                }

                currentProfile.updateTransaction(transaction);
                sendResult(Activity.RESULT_OK, "test");
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(Activity.RESULT_OK, "test");
                getActivity().getSupportFragmentManager().popBackStackImmediate();


            }
        });


        return v;
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
        private List<Category> mCategories;


        public CategoryAdapter(List<Category> categories) {
            mCategories = categories;

        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CategoryHolder(layoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, final int position) {
            Category category = mCategories.get(position);
            holder.bind(category);


        }

        @Override
        public int getItemCount() {
            return mCategories.size();

        }

        //method stub for deleting items from list.
        public void delete(int position) { //removes the row
            mCategories.remove(position);
            notifyItemRemoved(position);
        }

        public class CategoryHolder extends RecyclerView.ViewHolder {
            private Category mCategory;

            public CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_category, parent, false));

                categoryName = (TextView) itemView.findViewById(R.id.category);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        categoryList.removeCategoryTransaction(mCategory.getCategoryId(), transaction.getID());
                        categoryAdapter.notifyItemRemoved(getAdapterPosition());
                        updateList();
                    }
                });
            }

            public void bind(Category limit) {
                mCategory = limit;

                categoryName.setText(mCategory.getCategoryName());
            }

        }

        public void setCategories(List<Category> categories) {
            mCategories = categories;
        }
    }

    private void updateDate() {
        buttonChangeDate.setText(transaction.getDate().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            transaction.setDate(date);
            updateDate();
        }
    }

    private void sendResult(int resultCode, String test) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EDIT_TRANSACTION, test);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);

    }

    public void updateList() {
        List<Category> categories;
        categories = categoryList.getAllCategoriesForTransaction(transaction.getID().toString());

        categoryAdapter = new CategoryAdapter(categories);
        editTransactionRecyclerView.setAdapter(categoryAdapter);

        if (categoryAdapter == null) {
            categoryAdapter = new CategoryAdapter(categories);
            editTransactionRecyclerView.setAdapter(categoryAdapter);
        } else {
            categoryAdapter.setCategories(categories);
        }
    }


}
