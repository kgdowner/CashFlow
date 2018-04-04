package edu.csuci.myci.cashflow;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LoaderFragment extends Fragment {
    private Button mManageProfiles;
    private Button mManageCategories;
    private Button mAddTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.loader, container, false);

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
                new CustomOnItemSelectedListener(context).ManageProfilesCustomDialog();
            }
        });

        mManageCategories.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new CustomOnItemSelectedListener(context).ManageCategoriesCustomDialog();
            }
        });
    }
}
