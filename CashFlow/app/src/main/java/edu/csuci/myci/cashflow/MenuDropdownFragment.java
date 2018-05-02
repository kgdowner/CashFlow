package edu.csuci.myci.cashflow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuDropdownFragment extends DialogFragment {
    public static void display(Context context) {
        DialogFragment df = new MenuDropdownFragment();
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        df.show(ft, "MenuDropdownFragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_profile_management, container, true);
        // create the dialog and assign it the proper layout
        // ... ?  (TODO?)

        // get button handles
        Button menuTitle = (Button) view.findViewById(R.id.dropdown_menu_title);

        // register button listener functions
        menuTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuTitle();
            }
        });

        // show the dialog
        return view;
    }

    // button functions
    private void onMenuTitle() {

    }
}
