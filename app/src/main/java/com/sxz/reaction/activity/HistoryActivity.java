package com.sxz.reaction.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.sxz.reaction.R;
import com.sxz.reaction.fragment.HistoryFragment;
import com.sxz.reaction.model.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends SingleFragmentActivity {

    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mViewPager = (ViewPager) findViewById(R.id.history_view_pager);

        //Set up view pager
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            private List<Integer> types = Arrays.asList(Record.Type.AUDITORY, Record.Type.VISUAL);
            @Override
            public Fragment getItem(int position) {
                int type = types.get(position);
                return HistoryFragment.newInstance(type);
            }

            @Override
            public int getCount() {
                return types.size();
            }

//            @Override
//            public CharSequence getPageTitle(int position) {
//                int type = types.get(position);
//                switch (type){
//                    case Record.Type.AUDITORY:
//                        return "Auditory";
//                    case Record.Type.VISUAL:
//                        return "Visual";
//                    default:
//                        return "";
//                }
//            }
        });



    }


}
