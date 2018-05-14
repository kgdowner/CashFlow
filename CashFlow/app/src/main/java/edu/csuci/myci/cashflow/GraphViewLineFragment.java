package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphViewLineFragment extends Fragment {
    private static final String ARG_GRAPH_TYPE = "type";
    public static final int GRAPH_TYPE_BAR = 0;
    public static final int GRAPH_TYPE_LINE = 1;

    public static boolean graphIsInFront;
    public Context context;

    private Spinner spinnerTimeRange;
    private Button spinnerCategories;

    public static GraphView graph;

    public static String modifier = "date('now','-30 years')";

    LineGraphSeries<DataPoint> series2;

    public static void display(Context context, int typeOfGraph) {
        GraphViewLineFragment fragment = new GraphViewLineFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GRAPH_TYPE, typeOfGraph);   // FIXME: does this really need to be done this way?  maybe parameters?
        fragment.setArguments(args);

        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fragment, "List_View_Fragment");
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Bundle args = getArguments();
        int graphType = (int) getArguments().getSerializable("type");


        View v = inflater.inflate(R.layout.fragment_graph_view_line, container, false);

        graph = (GraphView) v.findViewById(R.id.graph);


            setLineGraph(graph);


        spinnerTimeRange = (Spinner) v.findViewById(R.id.time_range_spinner);
        spinnerCategories = (Button) v.findViewById(R.id.select_category_spinner);

        spinnerTimeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 1:
                        modifier = "date('now','-1 month')";
                        updateUI();
                        parent.setSelection(0);
                        break;  // month
                    case 2:
                        modifier = "date('now','-6 month')";
                        updateUI();
                        parent.setSelection(0);
                        break; //6 month
                    case 3:
                        modifier = "date('now','-1 years')";
                        updateUI();
                        parent.setSelection(0);
                        break; //year
                    case 4:
                        modifier = "date('now','-30 years')";
                        updateUI();
                        parent.setSelection(0);
                        break; //all time
                    default: break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphSortByCategoryFragment.display(getActivity());
            }
        });


        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onResume() {
        super.onResume();
        graphIsInFront = true;
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        graphIsInFront = false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }



    public void setLineGraph(GraphView graph) {

        //graph.invalidate();


        //series2 = new LineGraphSeries<>(GlobalScopeContainer.activeProfile.getSeries());
        series2 = new LineGraphSeries<>(GlobalScopeContainer.activeProfile.getSumSeries(
                modifier));

        resetGraphStyle();

        graph.addSeries(series2);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUI();
    }

    private void updateUI() {
        if (series2 != null) {

            series2.resetData(GlobalScopeContainer.activeProfile.getSumSeries(modifier));
            resetGraphStyle();
            graph.addSeries(series2);
        }
    }

    private void resetGraphStyle() {
        graph.removeAllSeries();
        graph.getGridLabelRenderer().resetStyles();
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setVerticalAxisTitle("Amount");
        graph.getViewport().setMaxX(series2.getHighestValueX());
        graph.getViewport().setMinX(series2.getLowestValueX());
        graph.getViewport().setMinY(series2.getLowestValueY());
        graph.getViewport().setMaxY(series2.getHighestValueY());
        //graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(10);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        graph.getViewport().setScalable(true);
    }
}
