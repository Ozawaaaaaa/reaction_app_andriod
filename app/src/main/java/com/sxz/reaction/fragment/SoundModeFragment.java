package com.sxz.reaction.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sxz.reaction.ChooseModeFragment;
import com.sxz.reaction.R;

import java.util.Random;

public class SoundModeFragment extends Fragment {

    private Button mStartSound;
    private Button mQuit;
    private ConstraintLayout mBackground;
    private MediaPlayer sound_high;
    private MediaPlayer sound_low;
    private long start_time;
    private long end_time;
    private int current_played = 0;
    private boolean rule_broken = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sound_mode, container, false);

        sound_high = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.high);
        sound_low = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.low);

        mStartSound = view.findViewById(R.id.start_sound_change);
        mQuit = view.findViewById(R.id.quit_to_choose_mode_from_sound_mode);
        mBackground = view.findViewById(R.id.sound_mode_background);



        mStartSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStartSound.getText().equals("Start")) {

                    mStartSound.setText("Restart");
                    sound_high.start();
                    mStartSound.setVisibility(View.GONE);
                    mQuit.setVisibility(View.GONE);

                    current_played = 1;

                    Random random = new Random(System.currentTimeMillis());
                    int n = random.nextInt(5) + 1;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (!rule_broken) {
                                sound_high.stop();
                                sound_high = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.high);
                                sound_low.start();
                                current_played = 2;
                                start_time = System.nanoTime();
                            }

                        }
                    }, n*1000);

                } else { // if restart

                    Fragment fragment = new SoundModeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });


        mBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_played == 2) {
                    mStartSound.setVisibility(View.VISIBLE);
                    mQuit.setVisibility(View.VISIBLE);

                    end_time = System.nanoTime();
                    double time_difference = (end_time - start_time)/1e6;
                    Toast.makeText(getContext(), Double.toString(time_difference), Toast.LENGTH_SHORT).show();

                    sound_low.stop();
                    current_played = 0;
                    sound_low = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.low);

                } else if (current_played == 1) {

                    mStartSound.setVisibility(View.VISIBLE);
                    mQuit.setVisibility(View.VISIBLE);

                    Toast.makeText(getContext(), "Sound not changed yet", Toast.LENGTH_SHORT).show();
//                    sound_high.stop();
//                    sound_high = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.high);
//                    current_played = 0;
//                    mStartSound.setText("Start");
//                    rule_broken = true;

                    sound_low.stop();
                    sound_high.stop();

                    Fragment fragment = new SoundModeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else {} // current nothing playing
            }
        });

        mQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ChooseModeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                sound_high.stop();
                sound_low.stop();
            }
        });

        return view;
    }
}
