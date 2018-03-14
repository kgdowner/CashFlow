package edu.csuci.myci.cashflow;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

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
                    //case 6: System.exit(0); break;
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
                    case 3: AddTransactionCustomDialog(); break;
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
}
