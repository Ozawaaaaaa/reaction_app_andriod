package com.sxz.reaction;

import android.media.MediaPlayer;
import android.os.Bundle;
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

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SoundModeFragment extends Fragment {

    private Button mStartSound;
    private Button mQuit;
    private ConstraintLayout mBackground;
    private MediaPlayer sound_high;
    private MediaPlayer sound_low;
    private boolean isStarted = false;
    private long start_time;
    private long end_time;


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


//                sound_low = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.low);

                if (mStartSound.getText().equals("Start")) {

                    mStartSound.setText("Restart");
                }

                sound_high.start();

                Random random = new Random(System.currentTimeMillis());
                int n = random.nextInt(5) + 1;
                try {
                    TimeUnit.SECONDS.sleep(n);
                } catch (InterruptedException e) {}

                sound_high.stop();
                sound_high = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.high);

                sound_low.start();
                isStarted = true;
                start_time = System.nanoTime();

            }
        });


        mBackground.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isStarted) {
                    end_time = System.nanoTime();
                    double time_difference = (end_time - start_time)/1e6;

                    isStarted = false;
                    Toast.makeText(getContext(), Double.toString(time_difference), Toast.LENGTH_SHORT).show();

                    sound_low.stop();
                    sound_low = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.low);
                }
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
            }
        });

        return view;
    }
}
