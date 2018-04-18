package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LoaderFragment extends Fragment {
    private static final int REQUEST_TRANSACTION = 0;

    private Button mManageProfiles;
    private Button mManageCategories;
    private Button mAddTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loader, container, false);

        mManageCategories = (Button)v.findViewById(R.id.manage_category_button);
        mManageProfiles = (Button)v.findViewById(R.id.manage_profile_button);
        mAddTransaction = (Button)v.findViewById(R.id.add_transaction_button);

        addListenerOnDialogButton(getActivity());

        return v;
    }

    public void addListenerOnDialogButton(final Context context) {
        mAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomOnItemSelectedListener(context).AddTransactionCustomDialog();
            }
        });

        mManageProfiles.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //new CustomOnItemSelectedListener(context).ManageProfilesCustomDialog();
                new ProfileManagementFragment(context);
            }
        });

        mManageCategories.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new CustomOnItemSelectedListener(context).ManageCategoriesCustomDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){return;}
        if(requestCode == REQUEST_TRANSACTION){
            Transaction date = (Transaction) data.getSerializableExtra(AddTransactionDialogFragment.ADD_TRANSACTION);
            Profile.get(getActivity()).addTransaction(date);
            //TODO: display list fragment after receiving new transacton on loader page.
            //Transaction date = (Transaction) data.getSerializableExtra(AddTransactionFragment.ADD_TRANSACTION);
            //Profile.get(getActivity()).addTransaction(date);
            //TODO: not 100% sure what this was trying to accomplish, but it needs to be done differently


        }
    }
}
