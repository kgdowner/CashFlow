package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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


public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener{
    private Context context;
    private static final int REQUEST_TRANSACTION = 0;
    private static final int CATEGORY_MANIPULATE = 1;




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
                            ImageView image = (ImageView)((Activity)context).findViewById(R.id.imageView);
                            image.setImageResource(R.drawable.graph_view_line);
                        } else {
                            SwitchToGraphViewLine(context, 1);
                        }
                        parent.setSelection(0);
                        break;


                    case 2: if(GraphisInFront ==true){
                        ImageView image = (ImageView)((Activity)context).findViewById(R.id.imageView);
                        image.setImageResource(R.drawable.graph_view_bar);
                    } else {
                        SwitchToGraphViewLine(context, 2);

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
                    //case 1: ManageProfilesCustomDialog();break;
                    case 1: new ProfileManagementFragment(this.context); break;
                    case 2: ManageCategoriesCustomDialog();break;
                    case 3:
                        if(GraphisInFront ==true)break;
                        AddTransactionCustomDialog(); break;

                    case 4:
                        //TODO: Set delete transaction to be impossible anywhere other than ListViewFragment
                        if(GraphisInFront ==true)break;
                        ListViewFragment.sDeleteFlag = true;
                        Toast.makeText(context, "Please make a selection", Toast.LENGTH_LONG).show();
                        break;
                    case 5: LimitsCustomDialog(); break;
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

    public void LimitsCustomDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_set_alert);

        Button mDialogCancelButton = (Button) dialog.findViewById(R.id.add_limit_cancel);
        mDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void AddTransactionCustomDialog() {
        DialogFragment df = new AddTransactionDialogFragment();
        FragmentTransaction ft = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        df.setTargetFragment(  ((FragmentActivity)context).getSupportFragmentManager().findFragmentByTag("List_View_Fragment"), REQUEST_TRANSACTION);
        df.show(ft,"Add_Transaction_Fragment");
    }


    public void ManageCategoriesCustomDialog() {
        DialogFragment df = new CategoryManagementDialogFragment();
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        df.setTargetFragment(((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("List_View_Fragment"), CATEGORY_MANIPULATE);
        df.show(ft, "Category_Management_Fragment");
    }

    public void SwitchToGraphViewLine(Context context, int graphType) {
        Fragment fr = GraphViewFragment.newInstance(graphType);
        FragmentManager fm;

        fm = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();


    }
}
