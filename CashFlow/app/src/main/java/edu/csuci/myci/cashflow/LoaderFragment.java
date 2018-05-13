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

    private Button buttonAddTransaction;
    private Button buttonManageCategories;
    private Button buttonManageProfiles;
    private Button buttonManageLimits;
    private TextView textCurrentProfileName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loader, container, false);

        buttonManageCategories  = (Button)      v.findViewById(R.id.manage_category_button);
        buttonManageProfiles    = (Button)      v.findViewById(R.id.manage_profile_button);
        buttonAddTransaction    = (Button)      v.findViewById(R.id.add_transaction_button);
        buttonManageLimits      = (Button)      v.findViewById(R.id.manage_limits_button);
        textCurrentProfileName  = (TextView)    v.findViewById(R.id.profile_name);

        textCurrentProfileName.setText(GlobalScopeContainer.activeProfile.getName().replace(".db", ""));

        addListenerOnDialogButton(getActivity());

        return v;
    }

    public void addListenerOnDialogButton(final Context context) {
        buttonAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTransactionDialogFragment.display(context);
            }
        });

        buttonManageProfiles.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProfileManagementFragment.display(context);
            }
        });

        buttonManageCategories.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CategoryManagementDialogFragment.display(context);
            }
        });
        buttonManageLimits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LimitsDialogFragment.display(context);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            textCurrentProfileName.setText(GlobalScopeContainer.activeProfile.getName().replace(".db", ""));



    }
}
