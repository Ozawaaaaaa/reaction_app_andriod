package com.sxz.reaction;

import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ColorModeFragment extends Fragment{

    private Button mStartColorChange;
    private Button mQuit;
    private ConstraintLayout mBackground;
    private ArrayList<String> colors = new ArrayList<>();
    private int current_color = 0;
    private boolean isStarted = false;
    private long start_time;
    private long end_time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_color_mode, container, false);

        colors.add("#E0E0E0");
        colors.add("#A1887F");

        mStartColorChange = view.findViewById(R.id.start_color_change);
        mBackground = view.findViewById(R.id.color_mode_background);
        mQuit = view.findViewById(R.id.quit_to_choose_mode);


        mStartColorChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mStartColorChange.getText().equals("Start")) {

                    mStartColorChange.setText("Restart");
                }

                Random random = new Random(System.currentTimeMillis());
                int n = random.nextInt(5) + 1;
                try {
                    TimeUnit.SECONDS.sleep(n);
                } catch (InterruptedException e) {}


                current_color = (current_color + 1)%2;
                isStarted = true;
                mBackground.setBackgroundColor(Color.parseColor(colors.get(current_color)));
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
