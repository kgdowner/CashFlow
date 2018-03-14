package edu.csuci.myci.cashflow;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class GraphView extends AppCompatActivity {
    final Context context = this;

    private Spinner mGraphViewSpinner;
    private Spinner mMainMenuSpinner;
    private Spinner mSelectCategorySpinner;
    private Spinner mSelectTimeRangeSpinner;

    private Button mListView;

    static boolean isInFront;

    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isInFront = false;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph_view);

        addListenerOnSpinnerItemSelection();

        mListView=(Button)findViewById(R.id.list_view_button);
        mListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListView.class);
                startActivity(intent);
            }
        });
    }



    public void addListenerOnSpinnerItemSelection(){
        mGraphViewSpinner = (Spinner)findViewById(R.id.graph_view_spinner);
        mMainMenuSpinner = (Spinner)findViewById(R.id.menu_spinner);
        mSelectCategorySpinner = (Spinner)findViewById(R.id.select_category_spinner);
        mSelectTimeRangeSpinner = (Spinner)findViewById(R.id.time_range_spinner);


        mGraphViewSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(this));
        mMainMenuSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(this));
        mSelectTimeRangeSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(this));
        mSelectCategorySpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(this));


    }



}


