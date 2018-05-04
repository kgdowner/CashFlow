package edu.csuci.myci.cashflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private Button buttonListView;
    private Button buttonGraphView;
    private Button buttonMenuDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // restore previous instance of application
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (savedInstanceState == null) {
            Fragment fr = new LoaderFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr, "List_View_Fragment"); //this needs to remain for now - insures we can add transactions from loader
            fragmentTransaction.commit();
        }


        // get the stored profiles and the active profile
        if (GlobalScopeContainer.activeProfile == null) {
            GlobalScopeContainer.profileList = new ArrayList<String>();
            if (getApplicationContext().databaseList() != null) {
                for (String s : getApplicationContext().databaseList()) {
                    if (!s.contains("journal"))
                        GlobalScopeContainer.profileList.add(s);
                }
            }


            // TODO: determine method for caching which profile was last active; for now, just get profile 0
        }
        if (GlobalScopeContainer.profileList.isEmpty()) {
            GlobalScopeContainer.profileList.add("defaultProfile.db");
        }
        GlobalScopeContainer.activeProfile = Profile.get(getApplicationContext(), GlobalScopeContainer.profileList.get(0));


        // TODO: move this menu bar to a new java file
        // top-bar button registration
        buttonGraphView = (Button) findViewById(R.id.graph_view_spinner);
        buttonMenuDropdown = (Button) findViewById(R.id.menu_spinner);
        buttonListView = (Button) findViewById(R.id.list_view_button);

        buttonGraphView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphDropdownFragment.display(MainActivity.this);
            }
        });
        //buttonGraphView.setOnItemSelectedListener(new CustomOnItemSelectedListener(this));
        buttonMenuDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuDropdownFragment.display(MainActivity.this);
            }
        });
        buttonListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new ListViewFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr, "List_View_Fragment");
                fragmentTransaction.commit();
            }
        });
    }
}
