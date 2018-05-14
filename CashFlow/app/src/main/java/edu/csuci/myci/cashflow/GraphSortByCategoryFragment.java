package edu.csuci.myci.cashflow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;

import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphSortByCategoryFragment extends DialogFragment {
    private RecyclerView graphRecyclerView;
    private CategoryAdapter categoryAdapter;
    //private CheckedTextView mCheckedTextView;

    private CheckBox categoryAllCheck;

    private CategoryList categoryList;
    List<Category> categories;

    private SparseBooleanArray itemStateArray= new SparseBooleanArray();


    public static void display(Context context) {
        DialogFragment df = new GraphSortByCategoryFragment();
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        df.setTargetFragment(((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("List_View_Fragment"), 0);

        df.show(ft, "GraphSortByCategoryFragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dropdown_sort_by_category, container, true);
        categoryList = new CategoryList(getActivity());

        // create the dialog and assign it the proper layout
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM|Gravity.RIGHT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setDimAmount(0);

        // get button handles
        graphRecyclerView = (RecyclerView) view.findViewById(R.id.sort_by_category_graph);
        graphRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateList();

        categoryAllCheck = (CheckBox)view.findViewById(R.id.checkboxAll);
        categoryAllCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                performActionOnClick();
            }
        });

        Button invisible = (Button)view.findViewById(R.id.button_invisible);






        //register button listener functions
//        menuTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onMenuTitle();
//            }
//        });
        invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        // show the dialog
        return view;
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
        private List<Category> mCategories;


        public CategoryAdapter(List<Category> categories) {
            mCategories = categories;

        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CategoryHolder(layoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, final int position) {
            Category category = mCategories.get(position);
            holder.bind(category);


        }

        @Override
        public int getItemCount() {
            return mCategories.size();

        }

        //method stub for deleting items from list.
        public void delete(int position) { //removes the row
            mCategories.remove(position);
            notifyItemRemoved(position);
        }

        public class CategoryHolder extends RecyclerView.ViewHolder {
            private Category mCategory;
            private CheckedTextView mCheckedTextView;

            public CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_category_check, parent, false));

                mCheckedTextView = (CheckedTextView) itemView.findViewById(R.id.simpleCheckedTextView);
                //mCheckedTextView.setCheckMarkDrawable(R.drawable.ic_check);




                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = getAdapterPosition();
                        if (!itemStateArray.get(adapterPosition, false)) {
                            mCheckedTextView.setChecked(true);
                            itemStateArray.put(adapterPosition, true);
                            //check all checks through boolean array, if all true, set checkAll true

                            //categoryAllCheck.setChecked(true);
                            for (int i = 0; i<itemStateArray.size(); i++){
                                if(itemStateArray.get(i) == false){
                                    categoryAllCheck.setChecked(false);
                                }
                            }
                        }
                        else  {

                            mCheckedTextView.setChecked(false);
                            itemStateArray.put(adapterPosition, false);
                            categoryAllCheck.setChecked(false);
                        }
                        performActionOnClick();

                    }
                });
            }

            public void bind(Category limit) {
                mCategory = limit;

                mCheckedTextView.setText(mCategory.getCategoryName());

                mCheckedTextView.setChecked(true);
                itemStateArray.put(getAdapterPosition(), true);
            }

        }

        public void setCategories(List<Category> categories) {
            mCategories = categories;
        }
    }
    public void updateList() {
        categories = categoryList.getCategories();

        categoryAdapter = new CategoryAdapter(categories);
        graphRecyclerView.setAdapter(categoryAdapter);

        if (categoryAdapter == null) {
            categoryAdapter = new CategoryAdapter(categories);
            graphRecyclerView.setAdapter(categoryAdapter);
        } else {
            categoryAdapter.setCategories(categories);
        }
    }
    public void performActionOnClick(){
        if (categoryAllCheck.isChecked()){
            GraphViewLineFragment.graph.removeAllSeries();
            LineGraphSeries<DataPoint> series =
                    new LineGraphSeries<>(GlobalScopeContainer.activeProfile.getSumSeries(GraphViewLineFragment.modifier ));
            GraphViewLineFragment.graph.addSeries(series);
            series.setTitle("Balance over time");
            updateList();


            //or clear all, run basic getSumSeries()
        }else{
            GraphViewLineFragment.graph.removeAllSeries();
            for (int i = 0; i<itemStateArray.size(); i++){
                if(itemStateArray.get(i) == true){
                    categories.get(i).getCategoryId();
                    LineGraphSeries<DataPoint> series =
                            new LineGraphSeries<>(GlobalScopeContainer.activeProfile.getSumSeriesByCategory(GraphViewLineFragment.modifier,  categories.get(i).getCategoryId() ));
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    series.setDrawDataPoints(true);
                    series.setDataPointsRadius(10);

                    series.setColor(color);
                    GraphViewLineFragment.graph.addSeries(series);
                    series.setTitle(categories.get(i).getCategoryName());
                    GraphViewLineFragment.graph.getLegendRenderer().setVisible(true);
                }
            }
            //get checked category id, run getSumSeries by category id
            GraphViewLineFragment.graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        }
    }



    @Override
    public void onSaveInstanceState(Bundle state) {

        super.onSaveInstanceState(state);

        //state.putBooleanArray("startArray", itemStateArray);

    }


}
