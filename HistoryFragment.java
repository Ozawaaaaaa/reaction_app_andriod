package com.sxz.reaction.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
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

    private static final String ARG_TYPE = "type";

    private ValueLineChart mLineChart;
    private ReactionLab mLab;
    private FirebaseAuth mAuth;
    private Shimmer mShimmer;
    private ShimmerTextView mShimmerTextView;
    private int mRecordType;

    public static HistoryFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLab = ReactionLab.get(getContext());
        mAuth = FirebaseAuth.getInstance();
        mShimmer = new Shimmer();
        mRecordType = getArguments().getInt(ARG_TYPE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        mLineChart = (ValueLineChart) v.findViewById(R.id.history_linechart);
        mShimmerTextView = (ShimmerTextView) v.findViewById(R.id.history_title_text);
        mShimmer.start(mShimmerTextView);
        switch (mRecordType){
            case Record.Type.AUDITORY:
                mShimmerTextView.setText("Auditory Record");
                break;
            case Record.Type.VISUAL:
                mShimmerTextView.setText("Visual Record");
                break;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            List<Record> records = mLab.getRecordsBy(user.getUid(), mRecordType);
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
