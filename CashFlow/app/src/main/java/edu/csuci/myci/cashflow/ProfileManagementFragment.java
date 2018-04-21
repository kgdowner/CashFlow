package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;


public class ProfileManagementFragment extends android.support.v4.app.DialogFragment{
    public static final String MANAGE_PROFILES = "edu.csuci.myci.cashflow.profile";

    private RadioGroup profiles;
    private Button button_add_profile;
    private Button button_remove_profile;
    private Button button_cancel;
    private Button button_ok;

    private int checkedButtonId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_profile_management, container, true);
        // create the dialog and assign it the proper layout


        // acquire layout object references
        this.profiles               = (RadioGroup) view.findViewById(R.id.radio_group);
        this.button_ok              = (Button) view.findViewById(R.id.button_ok);
        this.button_add_profile     = (Button) view.findViewById(R.id.button_add_profile);
        this.button_remove_profile  = (Button) view.findViewById(R.id.button_remove_profile);
        this.button_cancel          = (Button) view.findViewById(R.id.button_cancel);

        // register button listener functions
        this.button_add_profile.setOnClickListener(     new View.OnClickListener() {@Override public void onClick(View v) {onButtonAddProfile();    }});
        this.button_remove_profile.setOnClickListener(  new View.OnClickListener() {@Override public void onClick(View v) {onButtonRemoveProfile(); }});
        this.button_cancel.setOnClickListener(          new View.OnClickListener() {@Override public void onClick(View v) {onButtonCancel();        }});
        this.button_ok.setOnClickListener(              new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonOK(getActivity());
            }
        });
        this.profiles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedButtonId = checkedId;
            }
        });

        // update the profile list
        populateRadioGroup(GlobalScopeContainer.profileList);
        profiles.check(profiles.getChildAt(0).getId()); //sets default to 1st; FIXME: do we want default profile set to active?

        // show the dialog
        return view;
    }

    private void onButtonOK(Context context){
        if(checkedButtonId == -1){
            Toast.makeText(context,
                    "please make sure to select profile!",
                    Toast.LENGTH_LONG).show();

        }
        if(checkedButtonId != -1){
            RadioButton button = (RadioButton) profiles.findViewById(checkedButtonId);
            Toast.makeText(context, "current active profile is "+button.getText().toString(), Toast.LENGTH_LONG).show();

            GlobalScopeContainer.activeProfile = Profile.get(context, button.getText().toString());
            Toast.makeText(context, "current active profile is "+GlobalScopeContainer.activeProfile.getName(), Toast.LENGTH_LONG).show();
            //dismiss();
            sendResult(Activity.RESULT_OK, "test");
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }

    }

    private void onButtonAddProfile() {
        // TODO: add database here
    }

    private void onButtonRemoveProfile() {
        // TODO: remove database here + confirm dialog
    }

    private void onButtonCancel() {
        //dismiss();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();

    }

    private void populateRadioGroup(List<String> profileNames) {
        for ( String currentProfile: profileNames ) {
            RadioButton rb = new RadioButton(getContext());
            rb.setText(currentProfile);
            profiles.addView(rb);
        }
    }

    private void sendResult(int resultCode, String string) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(MANAGE_PROFILES, string);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
