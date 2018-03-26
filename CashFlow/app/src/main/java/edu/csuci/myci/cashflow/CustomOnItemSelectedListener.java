package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import static edu.csuci.myci.cashflow.GraphViewFragment.isInFront;

/**
 * Created by viktoriya on 3/13/18.
 */

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener{
    private Context context;
    public CustomOnItemSelectedListener(Context context){
        this.context=context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.graph_view_spinner:
                switch (position) {
                    case 0: break;
                    case 1: if(isInFront==true) {
                        ImageView image = (ImageView)((Activity)context).findViewById(R.id.imageView);
                        image.setImageResource(R.drawable.graph_view_line);
                        break;
                        } else SwitchToGraphViewLine(); break;
                    case 2: if(isInFront==true){
                        ImageView image = (ImageView)((Activity)context).findViewById(R.id.imageView);
                        image.setImageResource(R.drawable.graph_view_bar);
                        break;
                    } else {
                        SwitchToGraphViewLine();

                        //while(!isInFront){}
                        if(isInFront==true){
                            SwithGraphToBar();

                            break;}
                        break;

                    }
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
                    case 1: ManageProfilesCustomDialog();break;
                    case 2: ManageCategoriesCustomDialog();break;
                    case 3:
                        if(isInFront==true)break;
                        //need to insert if else statement (in which activity) if i want to reuse button
                        AddTransactionCustomDialog(); break;
                    case 5: LimitsCustomDialog(); break;
                    case 6: System.exit(0);break;
                    default:
                    Toast.makeText(parent.getContext(),
                            "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                } break;
            case R.id.sort_list_spinner :

                if(position==0) break;
                Toast.makeText(parent.getContext(),
                        "You are attempting to sort by : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
                break;
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
    public void LimitsCustomDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.set_alert_dialog);

        Button mDialogCancelButton = (Button) dialog.findViewById(R.id.add_limit_cancel);
        mDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public void AddTransactionCustomDialog(){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_transaction_dialog);

        Button mDialogCancelButton = (Button) dialog.findViewById(R.id.add_transaction_cancel);
        mDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();
    }
    public void ManageCategoriesCustomDialog(){
        final Dialog dialog2 = new Dialog(context);
        dialog2.setContentView(R.layout.category_management_dialog);

        Button mDialogCancelButton = (Button) dialog2.findViewById(R.id.manage_categroy_cancel_button);
        mDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });



        dialog2.show();

    }
    public void ManageProfilesCustomDialog(){
        final Dialog dialog1 = new Dialog(context);
        dialog1.setContentView(R.layout.profile_management_dialog);

        Button mDialogCancelButton = (Button) dialog1.findViewById(R.id.manage_profile_cancel_button);
        mDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });





        dialog1.show();
    }
    public void SwitchToGraphViewLine(){

        //Intent intent = new Intent(context, GraphView.class);
        //context.startActivity(intent);

    }
    public void SwithGraphToBar(){
        ImageView image = new ImageView(context);
        image = (ImageView)((Activity)context).findViewById(R.id.imageView);
        image.setImageResource(R.drawable.graph_view_bar);

    }
}
