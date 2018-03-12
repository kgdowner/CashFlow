package edu.csuci.myci.cashflow;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ListView extends AppCompatActivity {
    final Context context = this;

    private RecyclerView mTransactionRecyclerView;
    private TransactionAdapter mAdapter;
    private TextView mDateTextView;
    private TextView mAmountTextView;
    private TextView mCategoryTextView;
    private Button mAddTransaction;
    private Button mRemoveTransaction;
    private Button mSetLimits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        mTransactionRecyclerView = (RecyclerView) findViewById(R.id.transaction_recycler_view);
        mTransactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAddTransaction = (Button)findViewById(R.id.add_transaction_button);

        mAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.add_transaction_dialog);

                    Button dialogCancelButton = (Button) findViewById(R.id.add_transaction_cancel);



                    dialog.show();


            }

        });

        mRemoveTransaction = (Button)findViewById(R.id.remove_transaction_button);
        mRemoveTransaction.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"You clicked remove transactions",Toast.LENGTH_LONG).show();
            }
        }));

        mSetLimits = (Button)findViewById(R.id.set_allert_button);
        mSetLimits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.set_alert_dialog);

                dialog.show();
            }
        });






        updateUI();
    }

    private class TransactionHolder extends RecyclerView.ViewHolder{
        private Transaction mTransaction;

        public TransactionHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_transaction,parent,false));

            mDateTextView = (TextView) itemView.findViewById(R.id.transaction_date);
            mAmountTextView = (TextView) itemView.findViewById(R.id.transaction_amount);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.transaction_category);
        }

        public void bind(Transaction transaction){
            mTransaction = transaction;
            mDateTextView.setText(mTransaction.getDate().toString());
            mAmountTextView.setText(Double.toString(mTransaction.getAmount()));
            mCategoryTextView.setText(mTransaction.getCategory()[0]);
        }
    }

    private class TransactionAdapter extends RecyclerView.Adapter<TransactionHolder>{
        private List<Transaction> mTransactions;

        public TransactionAdapter(List<Transaction> transactions){mTransactions=transactions;}

        @Override
        public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());

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



    private void updateUI(){
        Profile crimeLab = Profile.get(getBaseContext());
        List<Transaction> transactions = crimeLab.getTransactions();

        mAdapter = new TransactionAdapter(transactions);
        mTransactionRecyclerView.setAdapter(mAdapter);

    }





}
