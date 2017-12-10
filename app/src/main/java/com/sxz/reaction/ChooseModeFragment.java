package com.sxz.reaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sxz.reaction.activity.SingleFragmentActivity;
import com.sxz.reaction.fragment.SoundModeFragment;

public class ChooseModeFragment extends Fragment {

    private Button mChooseColorButton;
    private Button mChooseSoundButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_mode, container, false);

        mChooseColorButton = view.findViewById(R.id.choose_color_button);
        mChooseSoundButton = view.findViewById(R.id.choose_sound_button);

        mChooseColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleFragmentActivity activity = (SingleFragmentActivity)getActivity();
                activity.switchFragment(new ColorModeFragment(), true);
            }
        });

        mChooseSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleFragmentActivity activity = (SingleFragmentActivity)getActivity();
                activity.switchFragment(new SoundModeFragment(), true);
            }
        });

        return view;
    }

}
