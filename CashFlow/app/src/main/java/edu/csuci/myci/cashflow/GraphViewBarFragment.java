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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;
import java.util.Random;

public class GraphViewBarFragment extends Fragment {
    private static final String ARG_GRAPH_TYPE = "type";
    public static final int GRAPH_TYPE_BAR = 0;
    public static final int GRAPH_TYPE_LINE = 1;

    public static boolean graphIsInFront;
    public Context context;

    private Spinner spinnerTimeRange;
    private Button spinnerCategories;

    GraphView graph;

    BarGraphSeries<DataPoint> series1;

    String modifier = "date('now','-30 years')";

    public static boolean graphByDate = true;

    public static List<String> barName;

    public static void display(Context context, int typeOfGraph) {
        GraphViewBarFragment fragment = new GraphViewBarFragment();
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


        View v = inflater.inflate(R.layout.fragment_graph_view_bar, container, false);

        graph = (GraphView) v.findViewById(R.id.graph);



            updateUI();


        spinnerTimeRange = (Spinner) v.findViewById(R.id.time_range_spinner);
        spinnerCategories = (Button) v.findViewById(R.id.select_category_spinner);

        spinnerCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchBarTypeFragment.display(getActivity());
            }
        });
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUI();
    }

    private void updateUI() {

            graph.removeAllSeries();
            graph.getGridLabelRenderer().resetStyles();

            //series1 = new BarGraphSeries<>(GlobalScopeContainer.activeProfile.getBarSeries(modifier));
            if(graphByDate) {
                series1 = new BarGraphSeries<>(GlobalScopeContainer.activeProfile.getSumBarSeries(modifier));
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
                series1.setSpacing(10);
                series1.setDrawValuesOnTop(true);
                series1.setValuesOnTopColor(Color.RED);
                graph.getViewport().setMaxX(series1.getHighestValueX());
                graph.getViewport().setMinX(series1.getLowestValueX());
                graph.getViewport().setScalable(true);

                graph.addSeries(series1);

            } else {
                DataPoint[] series = GlobalScopeContainer.activeProfile.getBarSeries(modifier);
                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter());
                graph.getViewport().setMaxX(series[series.length-1].getX()+1);
                graph.getViewport().setMinX(series[0].getX());
                graph.getViewport().setScalable(true);
                for (DataPoint d : series) {
                    BarGraphSeries<DataPoint> seriesX = new BarGraphSeries<>(new DataPoint[] {d});
                    //series1.setTitle(barName.toString());
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));


                    seriesX.setColor(color);
                    seriesX.setTitle(barName.get((int)d.getX()));
                    graph.getLegendRenderer().setVisible(true);
                    seriesX.setSpacing(5);
                    seriesX.setDrawValuesOnTop(true);
                    seriesX.setValuesOnTopColor(Color.RED);
                    graph.addSeries(seriesX);



                }
            graph.getLegendRenderer().setBackgroundColor(Color.WHITE);
                graph.getLegendRenderer().setSpacing(30);



            }

;
            graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }
}
