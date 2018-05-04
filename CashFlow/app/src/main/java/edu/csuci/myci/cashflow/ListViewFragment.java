package edu.csuci.myci.cashflow;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ListViewFragment extends Fragment {
    private RecyclerView mTransactionRecyclerView;
    private TransactionAdapter mAdapter;

    private TextView mDateTextView;
    private TextView mAmountTextView;
    private TextView mCategoryTextView;
    private TextView mNameTextView;

    private Button mAddTransaction;
    private Button mRemoveTransaction;
    private Button mEditTransaction;

    private Button sortOrderButton;

    private static int sPosition;
    private UUID editTransactionID;

    public static boolean sDeleteFlag = false;
    public static int sSortOrder = 0;
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

        sortOrderButton = (Button) v.findViewById(R.id.sort_list_spinner);

        mAddTransaction = (Button) v.findViewById(R.id.add_transaction_button);
        mRemoveTransaction = (Button) v.findViewById(R.id.remove_transaction_button);
        mEditTransaction = (Button) v.findViewById(R.id.edit_transaction);
        mRemoveTransaction.setEnabled(false);
        mEditTransaction.setEnabled(false);
        updateUI();


        addListenerOnDialogButton(getActivity());


        return v;
    }


    private class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {
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
            if (position % 2 == 1) {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.recyclerColor1));
                //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.recyclerColor2));
                //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
            }


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
                mNameTextView = (TextView) itemView.findViewById(R.id.transaction_name);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!sDeleteFlag) {
                            sDeleteFlag = true;
                            mRemoveTransaction.setEnabled(true);
                            mEditTransaction.setEnabled(true);
                            sPosition = getAdapterPosition();
                            editTransactionID = mTransaction.getID();
                        } else {
                            mRemoveTransaction.setEnabled(false);
                            mEditTransaction.setEnabled(false);
                            editTransactionID = null;
                            sDeleteFlag = false;
                        }

                        //TODO: delete more than 1 transaction


                    }
                });


            }

            public void bind(Transaction transaction) {

                SimpleDateFormat df = new SimpleDateFormat(" MM/dd/yy   kk:mm");
                mTransaction = transaction;


                mDateTextView.setText(df.format(mTransaction.getDate()).toString());
                mAmountTextView.setText(String.format("$%.2f", mTransaction.getAmount()));
                ArrayList<String> categoryArray = GlobalScopeContainer.activeProfile.getAllCategoriesForTransaction(mTransaction.getID().toString());
                StringBuilder sb = new StringBuilder();
                for (String s : categoryArray) {
                    sb.append(s);
                    sb.append(", ");
                }
                if (categoryArray.size() > 0) {
                    sb.setLength(sb.length() - 2);  //THIS IS CAUSING CRASH ON REMOVAL OF CATEGORIES KEITH!!!
                    sb.append(" ");
                }
                if (!categoryArray.isEmpty()) {
                    mCategoryTextView.setText(sb.toString());
                } else mCategoryTextView.setText(R.string.emptyCategory);

                mNameTextView.setText(mTransaction.getName().toString());
            }


        }

        public void setTransactions(List<Transaction> transactions) {
            mTransactions = transactions;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        //mAdapter.notifyDataSetChanged();
        sListIsInFront = true;
        sSortOrder = 0;

        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        sListIsInFront = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
        updateUI();
    }


    public void updateUI() {
        Profile currentProfile = GlobalScopeContainer.activeProfile;
        List<Transaction> transactions;

        switch (sSortOrder) {
            case 3:
                transactions = currentProfile.getTransactionsInOrder("amount");
                break;
            case 2:
                transactions = currentProfile.getTransactionsInOrder("category");
                break;
            default:
                transactions = currentProfile.getTransactionsInOrder("date");

        }

        mAdapter = new TransactionAdapter(transactions);
        mTransactionRecyclerView.setAdapter(mAdapter);
        //List<Transaction> transactions = Arrays.asList(GlobalScopeContainer.transactionBuffer);

        if (mAdapter == null) {
            mAdapter = new TransactionAdapter(transactions);
            mTransactionRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTransactions(transactions);
            //mCrimeRecyclerView.swapAdapter(mAdapter,true);

        }

        mTransactionRecyclerView.setAdapter(new TransactionAdapter(transactions));
        mTransactionRecyclerView.invalidate();
        mRemoveTransaction.setEnabled(false);
        mEditTransaction.setEnabled(false);
        currentProfile.limitChecker();
    }


    public void addListenerOnDialogButton(final Context context) {
        sortOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SorttransactionsFragment.display(context);
            }
        });

        mAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTransactionDialogFragment.display(context);
            }
        });

        mEditTransaction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "you pressed edit transaction", Toast.LENGTH_LONG).show();
                EditTransactionFragment.display(context, editTransactionID);
            }
        });

        mRemoveTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                mAdapter.delete(sPosition);
                                Transaction temp = GlobalScopeContainer.activeProfile.getTransactions(editTransactionID);
                                GlobalScopeContainer.activeProfile.removeTransaction(temp);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                        updateUI();

                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }


        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // FIXME: updating the UI like this does not seem logical
        // FIXME: for instance: it precludes updating the graph view if one of these
        // FIXME: sub-fragments was opened there, since they can only have one Target Fragment

        updateUI();
    }
}



