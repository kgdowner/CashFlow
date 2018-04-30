package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static edu.csuci.myci.cashflow.GraphViewFragment.GraphisInFront;
import static edu.csuci.myci.cashflow.ListViewFragment.sListIsInFront;



public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener{
    private Context context;
    private static final int REQUEST_TRANSACTION = 0;
    private static final int CATEGORY_MANIPULATE = 1;
    private static final int PROFILE_MANIPULATE = 2;


    public CustomOnItemSelectedListener(Context context){
        this.context=context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.graph_view_spinner:
                switch (position) {
                    case 0: break;
                    case 1:
                        if(GraphisInFront ==true) {
//                            ImageView image = (ImageView)((Activity)context).findViewById(R.id.imageView);
//                            image.setImageResource(R.drawable.graph_view_line);
                        } else {
                            GraphViewFragment.display(context, GraphViewFragment.GRAPH_TYPE_LINE);
                        }
                        parent.setSelection(0);
                        break;


                    case 2: if(GraphisInFront ==true){
//                        ImageView image = (ImageView)((Activity)context).findViewById(R.id.imageView);
//                        image.setImageResource(R.drawable.graph_view_bar);
                    } else {
                        GraphViewFragment.display(context, GraphViewFragment.GRAPH_TYPE_BAR);
                    }
                        parent.setSelection(0);

                        break;
                    default:
                        Toast.makeText(parent.getContext(),
                            "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                            Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case R.id.menu_spinner:
                switch (position) {
                    case 0: break;
                    case 1:
                        if(GraphisInFront ==true)break;
                        AddTransactionDialogFragment.display(context);
                        parent.setSelection(0);
                        break;
                    case 2:
                        if(sListIsInFront == false)break;
                        ListViewFragment.sDeleteFlag = true;
                        Toast.makeText(context, "Please make a selection", Toast.LENGTH_LONG).show();
                        parent.setSelection(0);
                        break;
                    case 3:
                        //Toast.makeText(context, "Not yet Implemented", Toast.LENGTH_LONG).show();
                        ProfileManagementFragment.display(context);
                        parent.setSelection(0);
                        break;
                    case 4:
                        CategoryManagementDialogFragment.display(context);
                        parent.setSelection(0);
                        break;
                    case 5:
                        //Toast.makeText(context, "Not yet Implemented", Toast.LENGTH_LONG).show();
                        LimitsDialogFragment.display(context);
                        parent.setSelection(0);
                        break;
                    case 6: System.exit(0);break;
                    default:
                    Toast.makeText(parent.getContext(),
                            "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                } break;
            case R.id.select_category_spinner:
                if(position==0) break;
                Toast.makeText(parent.getContext(),
                        "You are attempting to view only : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();

                break;
            case R.id.time_range_spinner:
                if(position==0) break;
                Toast.makeText(parent.getContext(),
                        "You are attempting to view by : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
