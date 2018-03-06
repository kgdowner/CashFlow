package edu.csuci.myci.cashflow;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainView extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new TransactionListFragment();
    }
}
