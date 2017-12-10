package com.sxz.reaction.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sxz.reaction.R;

/**
 * Created by Shihao on 12/3/17.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    public void switchFragment(Fragment targetFragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container, targetFragment);
        if (addToBackStack){
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
