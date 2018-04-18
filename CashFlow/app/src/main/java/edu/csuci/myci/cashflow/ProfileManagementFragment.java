package edu.csuci.myci.cashflow;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;



public class ProfileManagementFragment {
    private Dialog active_dialog;

    private RadioGroup profiles;
    private Button button_add_profile;
    private Button button_remove_profile;
    private Button button_cancel;


    public ProfileManagementFragment(Context context) {
        // create the dialog and assign it the proper layout
        this.active_dialog = new Dialog(context);
        this.active_dialog.setContentView(R.layout.dialog_profile_management);

        // acquire layout object references
        this.profiles               = (RadioGroup) this.active_dialog.findViewById(R.id.radio_group);
        this.button_add_profile     = (Button) this.active_dialog.findViewById(R.id.button_add_profile);
        this.button_remove_profile  = (Button) this.active_dialog.findViewById(R.id.button_remove_profile);
        this.button_cancel          = (Button) this.active_dialog.findViewById(R.id.button_cancel);

        // register button listener functions
        this.button_add_profile.setOnClickListener(     new View.OnClickListener() {@Override public void onClick(View v) {onButtonAddProfile();    }});
        this.button_remove_profile.setOnClickListener(  new View.OnClickListener() {@Override public void onClick(View v) {onButtonRemoveProfile(); }});
        this.button_cancel.setOnClickListener(          new View.OnClickListener() {@Override public void onClick(View v) {onButtonCancel();        }});

        // update the profile list
        RadioButton defaultProfile = (RadioButton) this.active_dialog.findViewById(R.id.default_radio_button);
        boolean first = true;
        for(String profileName : GlobalScopeContainer.profileList) {
            if(first) {
                defaultProfile.setText(profileName);
                first = false;
            } else {
                RadioButton newButton = new RadioButton(context);
                newButton.setText(profileName);
                newButton.setLayoutParams(defaultProfile.getLayoutParams());
                this.profiles.addView(newButton);
            }
        }

        // show the dialog
        this.active_dialog.show();
    }

    private void onButtonAddProfile() {
        // TODO: add database here
    }

    private void onButtonRemoveProfile() {
        // TODO: remove database here + confirm dialog
    }

    private void onButtonCancel() {
        this.active_dialog.dismiss();
    }
}
