package edu.csuci.myci.cashflow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

public class GraphDropdownFragment extends DialogFragment {
    public static void display(Context context) {
        DialogFragment df = new GraphDropdownFragment();
        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        df.show(ft, "GraphDropdownFragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dropdown_graphs, container, true);
        // create the dialog and assign it the proper layout
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP|Gravity.RIGHT);

        // ... ?  (TODO?)

        // get button handles

        Button graphLine = (Button)view.findViewById(R.id.dropdown_graph_line);
        Button graphBar = (Button)view.findViewById(R.id.dropdown_graph_bar);

         //register button listener functions
//        menuTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onMenuTitle();
//            }
//        });
        graphBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphViewFragment.display(getActivity(),2);
                dismiss();
            }
        });
        graphLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphViewFragment.display(getActivity(),1);
                //TODO: Either remove or fix
                dismiss();
            }
        });


        // show the dialog
        return view;
    }
}
