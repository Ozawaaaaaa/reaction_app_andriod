package com.sxz.reaction;

import android.support.v4.app.Fragment;

public class ChooseModeActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new ChooseModeFragment();
    }


}
