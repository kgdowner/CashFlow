package edu.csuci.myci.cashflow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by viktoriya on 3/26/18.
 */

public class GraphViewFragment extends Fragment {
    private static final String ARG_GRAPHTYPE = "type";


    public static boolean GraphisInFront;
    public Context mActivity;

    private Spinner mTimeRangeSpinner;
    private Spinner mSelectCategorySpinner;


    public static GraphViewFragment newInstance(int typeOfGraphInt){
        Bundle args = new Bundle();
        args.putSerializable(ARG_GRAPHTYPE, typeOfGraphInt);

        GraphViewFragment fragment = new GraphViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Bundle args = getArguments();
        int graphType = (int) getArguments().getSerializable("type");


        View v = inflater.inflate(R.layout.fragment_graph_view, container, false);

        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        if(graphType==1){
            imageView.setImageResource(R.drawable.graph_view_line);
        } else {
            imageView.setImageResource(R.drawable.graph_view_bar);
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
        GraphisInFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        GraphisInFront = false;
    }
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
