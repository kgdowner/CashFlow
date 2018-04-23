package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class ListViewFragment extends Fragment {

    private static final int REQUEST_TRANSACTION = 0;
    private static final int CATEGORY_MANIPULATE = 1;
    private static final int PROFILE_MANIPULATE = 2;

    private RecyclerView mTransactionRecyclerView;
    private TransactionAdapter mAdapter;

    private TextView mDateTextView;
    private TextView mAmountTextView;
    private TextView mCategoryTextView;
    private TextView mNameTextView;

    private Button mAddTransaction;
    private Button mRemoveTransaction;
    private Button mSetLimits;

    private Spinner mCategorySpinner;

    private static int sPosition;
    public static boolean sDeleteFlag = false;
    private static int sSortOrder = 0;
    public static boolean sListIsInFront;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sListIsInFront = true;

        View v = inflater.inflate(R.layout.fragment_list_view, container, false);

        mTransactionRecyclerView = (RecyclerView) v.findViewById(R.id.transaction_recycler_view);
        mTransactionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        mCategorySpinner = (Spinner) v.findViewById(R.id.sort_list_spinner);

        mAddTransaction = (Button) v.findViewById(R.id.add_transaction_button);
        mRemoveTransaction = (Button) v.findViewById(R.id.remove_transaction_button);
        mSetLimits = (Button) v.findViewById(R.id.set_alert_button);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sSortOrder = position;
                if(position!=0){updateUI();}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addListenerOnDialogButton(getActivity());





        return v;
    }



    private class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder>{
        private List<Transaction> mTransactions;


        public TransactionAdapter(List<Transaction> transactions) {
            mTransactions = transactions;
        }

        @Override
        public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new TransactionHolder(layoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(TransactionHolder holder, final int position) {
            Transaction transaction = mTransactions.get(position);
            holder.bind(transaction);



        }

        @Override
        public int getItemCount() {
            return mTransactions.size();

        }

        //method stub for deleting items from list.
        public void delete(int position) { //removes the row
            mTransactions.remove(position);
            notifyItemRemoved(position);

        }


        public class TransactionHolder extends RecyclerView.ViewHolder {
            private Transaction mTransaction;



            public TransactionHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_transaction, parent, false));

                mDateTextView = (TextView) itemView.findViewById(R.id.transaction_date);
                mAmountTextView = (TextView) itemView.findViewById(R.id.transaction_amount);
                mCategoryTextView = (TextView) itemView.findViewById(R.id.transaction_category);
                mNameTextView = (TextView)itemView.findViewById(R.id.transaction_name);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(sDeleteFlag == true) {

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        sPosition = getAdapterPosition();
                                        mAdapter.delete(sPosition);

                                        GlobalScopeContainer.activeProfile.removeTransaction(mTransaction);

                                        sDeleteFlag = false;

                                        updateUI();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        sDeleteFlag = false;
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();


                            //TODO: delete more than 1 transaction
                            // press trash can, highlight trashcan, set deleteFlag to true
                            //select more than 1 item...
                            //press trash can again
                            //are you sure dialog
                            //on ok, delete shit.
                        }
                    }
                });


            }

            public void bind(Transaction transaction) {

                SimpleDateFormat df = new SimpleDateFormat( " MM/dd/yy kk:mm");

                mTransaction = transaction;

                mDateTextView.setText(df.format(mTransaction.getDate()).toString());
                mAmountTextView.setText(String.format("$%7.2f", mTransaction.getAmount()));
                ArrayList<String> tempString = GlobalScopeContainer.activeProfile.getAllCategoriesForTransaction(mTransaction.getID().toString());
                StringBuilder sb = new StringBuilder();
                for (String s : tempString)
                {
                    sb.append(s);
                    sb.append(" ,");
                }
                if(!tempString.isEmpty()) {
                    mCategoryTextView.setText(sb.toString());
                }

                mNameTextView.setText(mTransaction.getName().toString());
            }





        }
        public void setTransactions(List<Transaction> transactions){
            mTransactions =transactions;
        }



    }
    @Override
    public void onResume(){
        super.onResume();
        //mAdapter.notifyDataSetChanged();
        sListIsInFront = true;
        sSortOrder=0;

        updateUI();
    }
    @Override
    public void onPause() {
        super.onPause();
        sListIsInFront = false;
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        //outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
        updateUI();
    }


    public void updateUI() {
        Profile currentProfile = GlobalScopeContainer.activeProfile;
        List<Transaction> transactions;

        if(sSortOrder ==0 || sSortOrder==1)
        {
            transactions = currentProfile.getTransactionsInOrder("date");

        }else if(sSortOrder==3){
            transactions = currentProfile.getTransactionsInOrder("amount");

        } else
        {
            //FIXME: verify what kind of results we want
            transactions= currentProfile.getTransactionsInOrder("category");

        }

        mAdapter = new TransactionAdapter(transactions);
        mTransactionRecyclerView.setAdapter(mAdapter);
        //List<Transaction> transactions = Arrays.asList(GlobalScopeContainer.transactionBuffer);

        if(mAdapter==null) {
            mAdapter = new TransactionAdapter(transactions);
            mTransactionRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTransactions(transactions);
            //mCrimeRecyclerView.swapAdapter(mAdapter,true);

        }

        mTransactionRecyclerView.setAdapter(new TransactionAdapter(transactions));
        mTransactionRecyclerView.invalidate();
    }


    public void addListenerOnDialogButton(final Context context) {
        mAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomOnItemSelectedListener(context).AddTransactionCustomDialog();

            }
        });

        mSetLimits.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new CustomOnItemSelectedListener(context).LimitsCustomDialog();
            }
        });

        mRemoveTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDeleteFlag = true;
                Toast.makeText(getActivity(), "Please make a selection", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_TRANSACTION){
            if(resultCode != Activity.RESULT_OK){return;}

            Transaction transaction = (Transaction) data.getSerializableExtra(AddTransactionDialogFragment.ADD_TRANSACTION);
            GlobalScopeContainer.activeProfile.addTransaction(transaction);
            updateUI();
        }
        if(requestCode == CATEGORY_MANIPULATE){

            if(resultCode == Activity.RESULT_OK) {
                Category category = (Category) data.getSerializableExtra(CategoryManagementDialogFragment.MANAGE_CATEGORY);
                //CategoryList.get(getActivity()).addCategory(category);
                updateUI();
                //Toast.makeText(getActivity(), "you manipulated category", Toast.LENGTH_LONG).show();
            }
            if(resultCode == Activity.RESULT_CANCELED){
                String categoryName = (String) data.getSerializableExtra(CategoryManagementDialogFragment.REMOVE_CATEGORY);
                //CategoryList.get(getActivity()).removeCategory(categoryName);
                Toast.makeText(getActivity(), "you manipulated category", Toast.LENGTH_LONG).show();
                updateUI();

            }


        }
        if(requestCode == PROFILE_MANIPULATE){
            updateUI();
        }
    }
}



