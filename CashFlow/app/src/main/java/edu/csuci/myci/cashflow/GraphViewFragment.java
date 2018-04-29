package edu.csuci.myci.cashflow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by viktoriya on 3/26/18.
 */

public class GraphViewFragment extends Fragment {
    private static final String ARG_GRAPHTYPE = "type";
    public static final int GRAPH_TYPE_BAR = 0;
    public static final int GRAPH_TYPE_LINE = 1;

    public static boolean GraphisInFront;
    public Context mActivity;

    private Spinner mTimeRangeSpinner;
    private Spinner mSelectCategorySpinner;

    BarGraphSeries<DataPoint> series1;
    LineGraphSeries<DataPoint> series2;


    public static void display(Context context, int typeOfGraph) {
        GraphViewFragment fragment = new GraphViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GRAPHTYPE, typeOfGraph);   // FIXME: does this really need to be done this way?  maybe parameters?
        fragment.setArguments(args);

        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Bundle args = getArguments();
        int graphType = (int) getArguments().getSerializable("type");


        View v = inflater.inflate(R.layout.fragment_graph_view, container, false);

        GraphView graph = (GraphView)v.findViewById(R.id.graph);


        if(graphType==1){
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


    public void setBarGraph(GraphView graph) {
//        graph.removeAllSeries();
//        series1 = GlobalScopeContainer.activeProfile.getBarSeries();
//        series1.setSpacing(10);
//        series1.setDrawValuesOnTop(true);
//        series1.setValuesOnTopColor(Color.RED);
//        graph.addSeries(series1);
//
//        graph.getViewport().setXAxisBoundsManual(true);
//
//        graph.getViewport().calcCompleteRange();
//        graph.getViewport().setMaxX(graph.getViewport().getMaxX(true));

    }

    public void setLineGraph(GraphView graph) {
        graph.removeAllSeries();

        series2 = new LineGraphSeries<>(GlobalScopeContainer.activeProfile.getSeries());

        graph.addSeries(series2);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setVerticalAxisTitle("Amount");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onResume() {
        super.onResume();
        GraphisInFront = true;
        //series1.resetData(GlobalScopeContainer.activeProfile.getSeries());
    }

    @Override
    public void onPause() {
        super.onPause();
        GraphisInFront = false;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
