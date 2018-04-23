package edu.csuci.myci.cashflow;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;        // FIXME: remove this when hardcoded test arrays are taken out

import javax.microedition.khronos.opengles.GL;


public class MainActivity extends AppCompatActivity {
    private Button mListViewButton;
    private Spinner mGraphViewSpinner;
    private Spinner mMainMenuSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // restore previous instance of application
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if(savedInstanceState==null) {
            Fragment fr = new LoaderFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fr, "List_View_Fragment"); //this needs to remain for now - insures we can add transactions from loader
            fragmentTransaction.commit();
        }

        if(GlobalScopeContainer.activeProfile == null) {
            GlobalScopeContainer.profileList = new ArrayList<String>();
            if(getApplicationContext().databaseList()!=null) {
                for (String s : getApplicationContext().databaseList()) {
                    if (!s.contains("journal"))
                        GlobalScopeContainer.profileList.add(s);
                }
            } else GlobalScopeContainer.profileList.add("defaultProfile.db");
        GlobalScopeContainer.activeProfile = Profile.get(getApplicationContext(), GlobalScopeContainer.profileList.get(0));

        }
//
//        Toast.makeText(getApplicationContext(), Arrays.toString(getApplicationContext().databaseList()),
//                Toast.LENGTH_LONG).show();



        Toast.makeText(getApplicationContext(), GlobalScopeContainer.profileList.toString(),
                Toast.LENGTH_LONG).show();

        // TODO: move this menu bar to a new java file
        // top-bar button registration
        mGraphViewSpinner = (Spinner) findViewById(R.id.graph_view_spinner);
        mMainMenuSpinner = (Spinner) findViewById(R.id.menu_spinner);
        mListViewButton = (Button) findViewById(R.id.list_view_button);


        mGraphViewSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(this));
        mMainMenuSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(this));
        mListViewButton.setOnClickListener(new View.OnClickListener() {
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
