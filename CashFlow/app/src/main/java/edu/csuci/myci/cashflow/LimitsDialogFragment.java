package edu.csuci.myci.cashflow;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viktoriya on 4/21/18.
 */

public class LimitsDialogFragment extends DialogFragment {
    private Spinner mCategorySpinner;
    private RadioGroup mAmountRadioGroup;
    private EditText mCustomAmount;
    private TextView mLimitsDisplay;
    private Button mOkButton;
    private Button mCancelButton;

    private RecyclerView mLimitsRecyclerView;
    private LimitAdapter mLimitAdapter;
    private TextView categoryName;
    private TextView categoryLimitAmount;



    private CategoryList categoryList;
    private String spinnerSelect;
    private int radioSelect = -1;
    private BigDecimal customAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_set_alert, container, true);

        this.mCategorySpinner = (Spinner)view.findViewById(R.id.category_spinner);
        this.mAmountRadioGroup = (RadioGroup)view.findViewById(R.id.limits_radioGroup);
        this.mCustomAmount = (EditText)view.findViewById(R.id.custom_amount_limits);
        this.mOkButton = (Button)view.findViewById(R.id.add_limit_ok);
        this.mCancelButton = (Button)view.findViewById(R.id.add_limit_cancel);
        this.mLimitsDisplay = (TextView)view.findViewById(R.id.limits_display);

        this.mLimitsRecyclerView = (RecyclerView)view.findViewById(R.id.limits_recyclerView);
        mLimitsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));








        categoryList = CategoryList.get(getActivity());
        updateList();

        final List<String> categoryNames = new ArrayList<String>();
        categoryNames.add("");
        categoryNames.addAll(categoryList.getCategories());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, categoryNames);
        mCategorySpinner.setAdapter(dataAdapter);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) spinnerSelect = categoryNames.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        populateRadioButtons();

        mAmountRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioSelect = checkedId;
                RadioButton button = (RadioButton) mAmountRadioGroup.findViewById(checkedId);

                customAmount = new BigDecimal((String)button.getText());
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLimitonOkButton();
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }


    private class LimitAdapter extends RecyclerView.Adapter<LimitAdapter.LimitHolder>{
        private List<Limit> mLimits;


        public LimitAdapter(List<Limit> limits) {
            mLimits = limits;
        }

        @Override
        public LimitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new LimitHolder(layoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(LimitHolder holder, final int position) {
            Limit limit = mLimits.get(position);
            holder.bind(limit);



        }

        @Override
        public int getItemCount() {
            return mLimits.size();

        }

        //method stub for deleting items from list.
        public void delete(int position) { //removes the row
            mLimits.remove(position);
            notifyItemRemoved(position);

        }


        public class LimitHolder extends RecyclerView.ViewHolder {
            private Limit mLimit;



            public LimitHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_limit, parent, false));

                categoryName = (TextView) itemView.findViewById(R.id.limit_category);
                categoryLimitAmount = (TextView) itemView.findViewById(R.id.limit_amount);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        categoryList.removeLimit(mLimit);
                        mLimitAdapter.notifyItemRemoved(getAdapterPosition());
                        updateList();

                    }
                });


            }

            public void bind(Limit limit) {

                //SimpleDateFormat df = new SimpleDateFormat( " MM/dd/yy");

                mLimit = limit;

                categoryLimitAmount.setText(String.format("$%.2f", mLimit.getAmount()));

                categoryName.setText(mLimit.getName().toString());
            }





        }
        public void setLimits(List<Limit> transactions){
            mLimits =transactions;
        }



    }



    private void setLimitonOkButton(){
        BigDecimal actualAmount;
        String amount = mCustomAmount.getText().toString();


        if(spinnerSelect==null){
            Toast.makeText(getActivity(),"please make a selection", Toast.LENGTH_LONG).show();
        }

        if(radioSelect == -1){
            if(TextUtils.isEmpty(amount)) {
                mCustomAmount.setError("Name your Category please.");
                return;
            }
            try {
                actualAmount =  new BigDecimal(amount);
            } catch (NumberFormatException e) {
                mCustomAmount.setError("Please enter number.");
                return;
            }
        } else {
            Button button = (Button)mAmountRadioGroup.findViewById(radioSelect);
            actualAmount = new BigDecimal(button.getText().toString());

        }
        categoryList.addLimit(new Limit(actualAmount, spinnerSelect));
        dismiss();
        //input actualAmount and spinnerSelect (string) to database,
        //put checkLimits into updateUI method.


    }
    private void populateRadioButtons(){
        String[] amounts = {"100","200","500","1000","1500"};
        for(String c : amounts) {
            RadioButton button = new RadioButton(getActivity());
            button.setText(c);
            mAmountRadioGroup.addView(button);
        }
    }
    public void updateList(){
        List<Limit> limits;
        limits = categoryList.getLimits();

        mLimitAdapter = new LimitAdapter(limits);
        mLimitsRecyclerView.setAdapter(mLimitAdapter);
        //List<Transaction> transactions = Arrays.asList(GlobalScopeContainer.transactionBuffer);

        if(mLimitAdapter==null) {
            mLimitAdapter = new LimitAdapter(limits);
            mLimitsRecyclerView.setAdapter(mLimitAdapter);
        } else {
            mLimitAdapter.setLimits(limits);
            //mCrimeRecyclerView.swapAdapter(mAdapter,true);

        }
    }

}
