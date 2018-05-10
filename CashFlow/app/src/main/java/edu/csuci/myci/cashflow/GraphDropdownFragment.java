package edu.csuci.myci.cashflow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setDimAmount(0);

        // get button handles
        Button invisible = (Button) view.findViewById(R.id.graph_dropdown_invisible_button);

        Button graphLine = (Button)view.findViewById(R.id.dropdown_graph_1);
        Button graphBar = (Button)view.findViewById(R.id.dropdown_graph_2);


         //register button listener functions
//        menuTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onMenuTitle();
//            }
//        });
        invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        graphBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphViewBarFragment.display(getActivity(),2);
                dismiss();
            }
        });
        graphLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphViewLineFragment.display(getActivity(),1);
                //TODO: Either remove or fix
                dismiss();
            }
        });


        // show the dialog
        return view;
    }
}
