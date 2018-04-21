package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoaderFragment extends Fragment {
    private static final int REQUEST_TRANSACTION = 0;

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

        mManageCategories = (Button)v.findViewById(R.id.manage_category_button);
        mManageProfiles = (Button)v.findViewById(R.id.manage_profile_button);
        mAddTransaction = (Button)v.findViewById(R.id.add_transaction_button);
        mCurrentProfileName = (TextView)v.findViewById(R.id.profile_name);

        mCurrentProfileName.setText(GlobalScopeContainer.activeProfile.getName());

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
            GlobalScopeContainer.activeProfile.addTransaction(date);
            Fragment fr = new ListViewFragment();
            FragmentManager fm = (getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr, "List_View_Fragment");
            fragmentTransaction.commit();
        }
    }
}
