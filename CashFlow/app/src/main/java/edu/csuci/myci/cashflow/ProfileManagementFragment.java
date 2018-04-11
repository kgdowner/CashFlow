package edu.csuci.myci.cashflow;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
 * Created by keith.downer897 on 4/9/18.
 */

public class ProfileManagementFragment {
    private Dialog active_dialog;

    private Button button_add_profile;
    private Button button_remove_profile;
    private Button button_cancel;


    public ProfileManagementFragment(Context context) {
        // create the dialog and assign it the proper layout
        this.active_dialog = new Dialog(context);
        this.active_dialog.setContentView(R.layout.dialog_profile_management);

        // acquire the button references
        this.button_add_profile     = (Button) this.active_dialog.findViewById(R.id.button_add_profile);
        this.button_remove_profile  = (Button) this.active_dialog.findViewById(R.id.button_remove_profile);
        this.button_cancel          = (Button) this.active_dialog.findViewById(R.id.button_cancel);

        // set button listeners  -  FIXME: move to private methods and register those here
        this.button_add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        this.button_remove_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        this.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active_dialog.dismiss();  // FIXME: referencing with 'this' keyword is out of scope
            }
        });

        // show the dialog
        this.active_dialog.show();
    }
}
