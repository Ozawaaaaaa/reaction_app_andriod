package com.sxz.reaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserProfileActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        switchFragment(new UserProfileFragment(), false);
    }
}
