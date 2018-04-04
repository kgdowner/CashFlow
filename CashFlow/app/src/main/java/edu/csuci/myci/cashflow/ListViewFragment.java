package edu.csuci.myci.cashflow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Created by viktoriya on 3/26/18.
 */

public class ListViewFragment extends Fragment {
    private RecyclerView mTransactionRecyclerView;
    private TransactionAdapter mAdapter;

    private TextView mDateTextView;
    private TextView mAmountTextView;
    private TextView mCategoryTextView;

    private Button mAddTransaction;
    private Button mRemoveTransaction;
    private Button mSetLimits;
    private Button mListViewButton;


    private Spinner mCategorySpinner;

    private static int sPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_view, container, false);

        mTransactionRecyclerView = (RecyclerView) v.findViewById(R.id.transaction_recycler_view);
        mTransactionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        mCategorySpinner = (Spinner) v.findViewById(R.id.sort_list_spinner);
        mCategorySpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(getActivity()));

        mAddTransaction = (Button) v.findViewById(R.id.add_transaction_button);
        mRemoveTransaction = (Button) v.findViewById(R.id.remove_transaction_button);
        mSetLimits = (Button) v.findViewById(R.id.set_allert_button);

        addListenerOnDialogButton(getActivity());

        return v;
    }


    private class TransactionHolder extends RecyclerView.ViewHolder {
        private Transaction mTransaction;

        public TransactionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_transaction, parent, false));

            // mRemoveTransaction.setOnClickListener(this);


            mDateTextView = (TextView) itemView.findViewById(R.id.transaction_date);
            mAmountTextView = (TextView) itemView.findViewById(R.id.transaction_amount);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.transaction_category);


        }

        public void bind(Transaction transaction) {
            // TODO: if you want this, redo this section; only fixed merge conflict
            //SimpleDateFormat df = new SimpleDateFormat( "EEE, MMM d, yyyy");

            mTransaction = transaction;

            mDateTextView.setText(mTransaction.getID().getDate().toString());
            mAmountTextView.setText(String.format("$%.2f", mTransaction.getAmount()));
            mCategoryTextView.setText(mTransaction.getCategories().toArray()[0].toString());
        }


    }

    private class TransactionAdapter extends RecyclerView.Adapter<TransactionHolder>{
        private List<Transaction> mTransactions;


        public TransactionAdapter(List<Transaction> transactions) {
            mTransactions = transactions;
        }

        @Override
        public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new TransactionHolder(layoutInflater, parent);
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



    }


    private void updateUI() {
        Profile crimeLab = Profile.get(getActivity());
        List<Transaction> transactions = crimeLab.getTransactions();

        mAdapter = new TransactionAdapter(transactions);
        mTransactionRecyclerView.setAdapter(mAdapter);

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
    }




    }



