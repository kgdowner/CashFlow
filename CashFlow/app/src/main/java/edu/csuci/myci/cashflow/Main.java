package edu.csuci.myci.cashflow;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

/**
 * Created by viktoriya on 3/26/18.
 */

public class Main extends AppCompatActivity {
    private Button mListViewButton;
    private Spinner mGraphViewSpinner;
    private Spinner mMainMenuSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mGraphViewSpinner = (Spinner)findViewById(R.id.graph_view_spinner);
        mMainMenuSpinner = (Spinner)findViewById(R.id.menu_spinner);
        mMainMenuSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(this));

        mListViewButton=(Button)findViewById(R.id.list_view_button);

        Fragment fr = new LoaderFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();

        mListViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                                Fragment fr = new ListViewFragment();


                                FragmentManager fm = getSupportFragmentManager();
                                //fm.popBackStack();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();

                                fragmentTransaction.replace(R.id.fragment_place, fr).addToBackStack("tag");
                                fragmentTransaction.commit();
                            }
        });
        mGraphViewSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(this));







        //mGraphViewButton=(Button)findViewById(R.id.graph_view_spinner);







    }






}
