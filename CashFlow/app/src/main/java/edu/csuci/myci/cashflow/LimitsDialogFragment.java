package edu.csuci.myci.cashflow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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


public class LimitsDialogFragment extends DialogFragment {
    private Spinner spinnerCategories;
    private RadioGroup radioGroupAmount;
    private EditText editTextCustomAmount;
    private Button buttonOk;
    private Button buttonCancel;

    private RecyclerView recyclerViewLimits;
    private LimitAdapter adapterLimit;
    private TextView textCategoryName;
    private TextView textCategoryLimitAmount;

    private CategoryList categoryList;
    private String spinnerSelect;
    private int radioSelect = -1;
    private BigDecimal amountFromRadioButton;

    public static void display(Context context) {
        DialogFragment df = new LimitsDialogFragment();
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        df.setTargetFragment(((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("List_View_Fragment"), 0);
        df.show(ft, "Limits_Manipulation_Fragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_set_alert, container, true);

        this.spinnerCategories = (Spinner) view.findViewById(R.id.category_spinner);
        this.radioGroupAmount = (RadioGroup) view.findViewById(R.id.limits_radioGroup);
        this.editTextCustomAmount = (EditText) view.findViewById(R.id.custom_amount_limits);
        this.buttonOk = (Button) view.findViewById(R.id.add_limit_ok);
        this.buttonCancel = (Button) view.findViewById(R.id.add_limit_cancel);

        this.recyclerViewLimits = (RecyclerView) view.findViewById(R.id.limits_recyclerView);
        recyclerViewLimits.setLayoutManager(new LinearLayoutManager(getActivity()));

        categoryList = new CategoryList(getActivity());
        updateList();

        final List<String> textCategoryNames = new ArrayList<String>();
        textCategoryNames.add("");
        textCategoryNames.addAll(categoryList.getCategoryNames());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, textCategoryNames);
        spinnerCategories.setAdapter(dataAdapter);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) spinnerSelect = textCategoryNames.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        populateRadioButtons();

        radioGroupAmount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioSelect = checkedId;
                RadioButton button = (RadioButton) radioGroupAmount.findViewById(checkedId);

                amountFromRadioButton = new BigDecimal((String) button.getText());
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLimitOnOkButton();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }


    private class LimitAdapter extends RecyclerView.Adapter<LimitAdapter.LimitHolder> {
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

                textCategoryName = (TextView) itemView.findViewById(R.id.limit_category);
                textCategoryLimitAmount = (TextView) itemView.findViewById(R.id.limit_amount);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        categoryList.removeLimit(mLimit);
                        adapterLimit.notifyItemRemoved(getAdapterPosition());
                        updateList();
                    }
                });
            }

            public void bind(Limit limit) {
                mLimit = limit;

                textCategoryLimitAmount.setText(String.format("$%.2f", mLimit.getAmount()));
                textCategoryName.setText(mLimit.getName());
            }

        }

        public void setLimits(List<Limit> limits) {
            mLimits = limits;
        }
    }

    private void setLimitOnOkButton() {
        BigDecimal actualAmount;
        String amount = editTextCustomAmount.getText().toString();


        if (spinnerSelect.isEmpty()) {
            Toast.makeText(getActivity(), "please make a selection", Toast.LENGTH_LONG).show();
        }

        if (radioSelect == -1) {
            if (TextUtils.isEmpty(amount)) {
                editTextCustomAmount.setError("Name your Category please.");
                return;
            }
            try {
                actualAmount = new BigDecimal(amount);
            } catch (NumberFormatException e) {
                editTextCustomAmount.setError("Please enter number.");
                return;
            }
        } else {
            actualAmount = amountFromRadioButton;

        }
        categoryList.addLimit(new Limit(actualAmount, spinnerSelect));
        updateList();
    }

    private void populateRadioButtons() {
        String[] amounts = {"100", "200", "500", "1000", "1500"};
        for (String c : amounts) {
            RadioButton button = new RadioButton(getActivity());
            button.setText(c);
            radioGroupAmount.addView(button);
        }
    }

    public void updateList() {
        List<Limit> limits;
        limits = categoryList.getLimits();

        adapterLimit = new LimitAdapter(limits);
        recyclerViewLimits.setAdapter(adapterLimit);

        if (adapterLimit == null) {
            adapterLimit = new LimitAdapter(limits);
            recyclerViewLimits.setAdapter(adapterLimit);
        } else {
            adapterLimit.setLimits(limits);
        }
    }

}
