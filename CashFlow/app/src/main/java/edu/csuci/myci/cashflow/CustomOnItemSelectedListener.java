package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static edu.csuci.myci.cashflow.GraphViewLineFragment.graphIsInFront;
import static edu.csuci.myci.cashflow.ListViewFragment.listIsInFront;


public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private Context context;


    public CustomOnItemSelectedListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
                         
            case R.id.select_category_spinner:
                if (position == 0) break;
                Toast.makeText(parent.getContext(),
                        "You are attempting to view only : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();

                break;
            case R.id.time_range_spinner:
                if (position == 0) break;
                Toast.makeText(parent.getContext(),
                        "You are attempting to view by : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
