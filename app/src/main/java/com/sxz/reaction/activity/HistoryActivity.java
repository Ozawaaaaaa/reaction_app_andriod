package com.sxz.reaction.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sxz.reaction.R;
import com.sxz.reaction.fragment.HistoryFragment;

public class HistoryActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        getSupportActionBar().setTitle("Your Personal Record");
        switchFragment(new HistoryFragment(), false);

    }


}
