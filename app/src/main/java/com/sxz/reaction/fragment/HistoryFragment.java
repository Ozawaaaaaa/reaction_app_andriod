package com.sxz.reaction.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sxz.reaction.R;
import com.sxz.reaction.database.ReactionLab;
import com.sxz.reaction.model.Record;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.List;

/**
 * Created by Shihao on 12/10/17.
 */

public class HistoryFragment extends Fragment {

    private ValueLineChart mLineChart;
    private ReactionLab mLab;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLab = ReactionLab.get(getContext());
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        mLineChart = (ValueLineChart) v.findViewById(R.id.history_linechart);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            List<Record> records = mLab.getRecordsByUserID(user.getUid());

            ValueLineSeries series = new ValueLineSeries();
            series.setColor(0xFF56B7F1);

            for (Record r:records){
                series.addPoint(new ValueLinePoint(r.getDate().toString(), r.getTime()));
            }
            mLineChart.addSeries(series);
        }


        mLineChart.startAnimation();
        return v;
    }
}
