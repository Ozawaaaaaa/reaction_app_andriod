package com.sxz.reaction.activity;

import android.os.Bundle;

import com.sxz.reaction.R;
import com.sxz.reaction.fragment.UserProfileFragment;

public class UserProfileActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        switchFragment(new UserProfileFragment(), false);

    }
}
