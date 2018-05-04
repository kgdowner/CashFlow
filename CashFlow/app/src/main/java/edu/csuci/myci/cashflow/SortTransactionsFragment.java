package edu.csuci.myci.cashflow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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

public class SortTransactionsFragment extends DialogFragment {
    public static void display(Context context) {
        DialogFragment df = new SortTransactionsFragment();
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        df.setTargetFragment(((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("List_View_Fragment"), 0);

        df.show(ft, "SortTransactionsFragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dropdown_sort_transactions, container, true);
        // create the dialog and assign it the proper layout
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM|Gravity.RIGHT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setDimAmount(0);

        // ... ?  (TODO?)

        // get button handles

        Button sortDate = (Button)view.findViewById(R.id.sort_by_date);
        Button sortCategory = (Button)view.findViewById(R.id.sort_by_category);
        Button sortAmount = (Button)view.findViewById(R.id.sort_by_amount);


        //register button listener functions
//        menuTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onMenuTitle();
//            }
//        });
        sortDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewFragment.sortOrder =1;
                getTargetFragment().onActivityResult(0, 0, null);

                dismiss();
            }
        });
        sortCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewFragment.sortOrder =2;
                getTargetFragment().onActivityResult(0, 0, null);

                dismiss();
            }
        });
        sortAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewFragment.sortOrder =3;
                getTargetFragment().onActivityResult(0, 0, null);

                dismiss();
            }
        });


        // show the dialog
        return view;
    }
}
