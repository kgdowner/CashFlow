package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by viktoriya on 4/25/18.
 */

public class EditTransactionFragment extends Fragment {
    public static final String EDIT_TRANSACTION = "edu.csuci.myci.cashflow.transaction_edit";


    private static final String ARG_TRANSACTION_ID = "transaction_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;


    private Transaction mTransaction;
    private Profile mCurrentProfile;

    private EditText mEditTitle;
    private EditText mEditAmount;
    private Button mChangeDate;
    private Button mOkButton;
    private Button mCancelButton;

    private String newAmount;
    private String newName; //moving actual changing of stuff to OK button
    private Date   oldDate;

    public static EditTransactionFragment newInstance(UUID transactionID){
        EditTransactionFragment fragment = new EditTransactionFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_TRANSACTION_ID, transactionID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentProfile = GlobalScopeContainer.activeProfile;
        UUID transactionID = (UUID) getArguments().getSerializable(ARG_TRANSACTION_ID);
        mTransaction = mCurrentProfile.getTransactions(transactionID);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_transaction, container, false);

        mEditTitle = (EditText)v.findViewById(R.id.transaction_name);
        mEditAmount = (EditText)v.findViewById(R.id.transaction_amount);
        mChangeDate = (Button)v.findViewById(R.id.transaction_date);
        mOkButton =(Button)v.findViewById(R.id.edit_transaction_ok);
        mCancelButton = (Button)v.findViewById(R.id.edit_transaction_cancel);

        mEditTitle.setText(mTransaction.getName());
        mEditTitle.addTextChangedListener(new TextWatcher() {
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

        mEditAmount.setText(mTransaction.getAmount().toString());
        mEditAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newAmount = s.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO: Verification!!!
                newAmount = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
               // newAmount = s.toString();



            }
        });
        updateDate();
        mChangeDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mTransaction.getDate());
                dialog.setTargetFragment(EditTransactionFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);


            }
        });
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newAmount!= null){ mTransaction.setAmount(new BigDecimal(newAmount));}
                if(newName!=null){mTransaction.setName(newName);}

                mCurrentProfile.updateTransaction(mTransaction);
                sendResult(Activity.RESULT_OK, "test");
                getActivity().getSupportFragmentManager().popBackStackImmediate() ;
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate() ;


            }
        });

        return v;
    }

    private void updateDate() {
        mChangeDate.setText(mTransaction.getDate().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){return;}
        if(requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
             mTransaction.setDate(date);
            updateDate();
        }
    }
    private void sendResult(int resultCode, String test){
        if(getTargetFragment() == null){return;}
        Intent intent = new Intent();
        intent.putExtra(EDIT_TRANSACTION, test);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode, intent  );

    }


}
