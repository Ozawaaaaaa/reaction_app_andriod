package com.sxz.reaction.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sxz.reaction.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shihao on 12/11/17.
 */

public class ResultFragment extends Fragment {

    public static final String ARG_TIME = "time";

    @BindView(R.id.result_time_text)
    TextView mTimeTextView;

    @BindView(R.id.result_restart_button)
    Button mRestartButton;

    @BindView(R.id.result_upload_button)
    Button mUploadButton;

    FirebaseAuth mAuth;

    private float mReactionTime;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mReactionTime = getArguments().getFloat(ARG_TIME);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result, container, false);
        ButterKnife.bind(this, v);

        mTimeTextView.setText(Float.toString(mReactionTime));

        return v;
    }

    public static ResultFragment newInstance(float time){
        Bundle args = new Bundle();
        args.putFloat(ARG_TIME, time);
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
