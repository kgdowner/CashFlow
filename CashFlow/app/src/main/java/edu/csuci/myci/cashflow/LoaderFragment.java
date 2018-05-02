package edu.csuci.myci.cashflow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LoaderFragment extends Fragment {
    private static final int PROFILE_MANIPULATE = 2;


    private Button mManageProfiles;
    private Button mManageCategories;
    private Button mAddTransaction;

    private TextView mCurrentProfileName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loader, container, false);

        mManageCategories = (Button) v.findViewById(R.id.manage_category_button);
        mManageProfiles = (Button) v.findViewById(R.id.manage_profile_button);
        mAddTransaction = (Button) v.findViewById(R.id.add_transaction_button);
        mCurrentProfileName = (TextView) v.findViewById(R.id.profile_name);

        mCurrentProfileName.setText(GlobalScopeContainer.activeProfile.getName().replace(".db", ""));

        addListenerOnDialogButton(getActivity());

        return v;
    }

    public void addListenerOnDialogButton(final Context context) {
        mAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTransactionDialogFragment.display(context);
            }
        });

        mManageProfiles.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProfileManagementFragment.display(context);
            }
        });

        mManageCategories.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CategoryManagementDialogFragment.display(context);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PROFILE_MANIPULATE) {
            mCurrentProfileName.setText(GlobalScopeContainer.activeProfile.getName());


        }
    }
}
