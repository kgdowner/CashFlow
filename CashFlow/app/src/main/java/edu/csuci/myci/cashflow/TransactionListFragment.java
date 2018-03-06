package edu.csuci.myci.cashflow;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;


/**
 * Created by viktoriya on 3/6/18.
 */

public class TransactionListFragment extends Fragment {
    private RecyclerView mTransactionReyclerView;
    private TransactionAdapter mAdapter;
    private TextView mDateTextView;
    private TextView mCategoryTextView;
    private TextView mAmountTextView;

    private class TransactionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Transaction mTransaction;



        public TransactionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_transaction, parent, false));
            itemView.setOnClickListener(this);

            mCategoryTextView = (TextView) itemView.findViewById(R.id.transaction_category);
            mDateTextView = (TextView) itemView.findViewById(R.id.transaction_date);
            mAmountTextView = (TextView) itemView.findViewById(R.id.amount);
        }
        public void bind(Transaction transaction){
            mTransaction = transaction;
            mDateTextView.setText(mTransaction.getDate().toString());
            mCategoryTextView.setText(Arrays.toString(mTransaction.getCategory()));
            mAmountTextView.setText(mTransaction.getAmount().toString());
        }

        @Override
        public void onClick(View view){
            Toast.makeText(getActivity(), mTransaction.getId() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }



    }

    private class TransactionAdapter extends RecyclerView.Adapter<TransactionHolder>{
        private List<Transaction> mTransactions;

        public TransactionAdapter(List<Transaction> transactions){
            mTransactions = transactions;
        }

        @Override
        public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new TransactionHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TransactionHolder holder, int position){
            Transaction transaction = mTransactions.get(position);
            holder.bind(transaction);

        }

        @Override
        public int getItemCount() {
            return mTransactions.size();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        mTransactionReyclerView = (RecyclerView) view.findViewById(R.id.transaction_recyler_view);
        mTransactionReyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        /*
        recycler views make the list, recycle containers for members of list
        recycler view commands layout manager to do the actual positioning of items
        recycler view calls viewHolders to collect thingies to put into "views"...
        */
        return view;
    }

    private void updateUI(){
        Profile profile = Profile.get(getActivity());
        List<Transaction> transactions = profile.getTransactions();

        mAdapter = new TransactionAdapter(transactions);
        mTransactionReyclerView.setAdapter(mAdapter);

    }
}
