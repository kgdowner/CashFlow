package edu.csuci.myci.cashflow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

public class MenuDropdownFragment extends DialogFragment {
    public static void display(Context context) {
        DialogFragment df = new MenuDropdownFragment();
        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        df.show(ft, "MenuDropdownFragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dropdown_menu, container, true);
        // create the dialog and assign it the proper layout
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP|Gravity.LEFT);

        // ... ?  (TODO?)

        // get button handles
        Button menuTitle = (Button) view.findViewById(R.id.dropdown_menu_title);
        menuTitle.setText(GlobalScopeContainer.activeProfile.getName());
        Button addTransaction = (Button)view.findViewById(R.id.dropdown_menu_add);
        Button removeTransaction = (Button)view.findViewById(R.id.dropdown_menu_remove);
        Button manageProfiles = (Button)view.findViewById(R.id.dropdown_menu_profiles);
        Button manageCategories = (Button)view.findViewById(R.id.dropdown_menu_categories);
        Button manageLimits = (Button)view.findViewById(R.id.dropdown_menu_limits);
        Button exitButton = (Button)view.findViewById(R.id.dropdown_menu_exit);

         //register button listener functions
//        menuTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onMenuTitle();
//            }
//        });
        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTransactionDialogFragment.display(getActivity());
                dismiss();
            }
        });
        removeTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewFragment.sDeleteFlag = true;
                //TODO: Either remove or fix
                dismiss();
            }
        });
        manageProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileManagementFragment.display(getActivity());
                dismiss();
            }
        });
        manageCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryManagementDialogFragment.display(getActivity());
                dismiss();

            }
        });
        manageLimits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LimitsDialogFragment.display(getActivity());
                dismiss();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        // show the dialog
        return view;
    }
}
