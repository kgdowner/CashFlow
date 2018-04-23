package edu.csuci.myci.cashflow;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

        categoryList = CategoryList.get(getActivity());
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

    private void setLimitonOkButton(){
        BigDecimal actualAmount;
        String amount = mCustomAmount.getText().toString();


        if(spinnerSelect.isEmpty()){
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
        //categoryList.updateCategoryLimits(spinnerSelect,actualAmount);
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

}
