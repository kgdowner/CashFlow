package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphViewFragment extends Fragment {
    private static final String ARG_GRAPH_TYPE = "type";
    public static final int GRAPH_TYPE_BAR = 0;
    public static final int GRAPH_TYPE_LINE = 1;

    public static boolean GraphIsInFront;
    public Context mActivity;

    private Spinner mTimeRangeSpinner;
    private Spinner mSelectCategorySpinner;

    BarGraphSeries<DataPoint> series1;
    LineGraphSeries<DataPoint> series2;

    public static void display(Context context, int typeOfGraph) {
        GraphViewFragment fragment = new GraphViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GRAPH_TYPE, typeOfGraph);   // FIXME: does this really need to be done this way?  maybe parameters?
        fragment.setArguments(args);

        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fragment, "List_View_Fragment");
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Bundle args = getArguments();
        int graphType = (int) getArguments().getSerializable("type");


        View v = inflater.inflate(R.layout.fragment_graph_view, container, false);

        GraphView graph = (GraphView) v.findViewById(R.id.graph);


        if (graphType == 1) {
            setLineGraph(graph);
        } else {
            setBarGraph(graph);
        }


        mTimeRangeSpinner = (Spinner) v.findViewById(R.id.time_range_spinner);
        mSelectCategorySpinner = (Spinner) v.findViewById(R.id.select_category_spinner);

        mSelectCategorySpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(getActivity()));
        mTimeRangeSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener(getActivity()));


        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onResume() {
        super.onResume();
        GraphIsInFront = true;
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        GraphIsInFront = false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    public void setBarGraph(GraphView graph) {
        graph.removeAllSeries();
        graph.getGridLabelRenderer().resetStyles();
        //graph.invalidate();

        series1 = new BarGraphSeries<>(GlobalScopeContainer.activeProfile.getBarSeries());
        series1.setSpacing(10);
        series1.setDrawValuesOnTop(true);
        series1.setValuesOnTopColor(Color.RED);

        graph.addSeries(series1);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().calcCompleteRange();
        graph.getViewport().setMaxX(graph.getViewport().getMaxX(true));

    }

    public void setLineGraph(GraphView graph) {
        graph.removeAllSeries();
        graph.getGridLabelRenderer().resetStyles();
        //graph.invalidate();


        series2 = new LineGraphSeries<>(GlobalScopeContainer.activeProfile.getSeries());


        graph.addSeries(series2);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setVerticalAxisTitle("Amount");
        graph.getViewport().setScrollable(true);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // FIXME: updating the UI like this does not seem logical
        // FIXME: for instance: it precludes updating the graph view if one of these
        // FIXME: sub-fragments was opened there, since they can only have one Target Fragment

        updateUI();
    }

    private void updateUI() {
        if (series2 != null) {
            series2.resetData(GlobalScopeContainer.activeProfile.getSeries());
        }

        if (series1 != null) {
            series1.resetData(GlobalScopeContainer.activeProfile.getBarSeries());
        }
    }
}
