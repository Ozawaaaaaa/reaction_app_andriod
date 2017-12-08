package com.sxz.reaction;

//import android.graphics.Color;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class ColorModeFragment extends Fragment{

    private Button mStartColorChange;
    private Button mQuit;
    private ConstraintLayout mBackground;
    private ArrayList<String> colors = new ArrayList<>();
    private int current_color = 0;
    private long start_time;
    private long end_time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_color_mode, container, false);

        colors.add("#EF5350"); // red
        colors.add("#FFF176"); // yellow
        colors.add("#C6FF00"); // green

        mStartColorChange = view.findViewById(R.id.start_color_change);
        mBackground = view.findViewById(R.id.color_mode_background);
        mQuit = view.findViewById(R.id.quit_to_choose_mode);

        mBackground.setBackgroundColor(Color.parseColor(colors.get(0)));

        mStartColorChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mStartColorChange.getText().equals("Start")) {
                    mBackground.setBackgroundColor(Color.parseColor(colors.get(1)));

                    current_color = 1; // now yellow

                    mStartColorChange.setText("Restart");

                    mStartColorChange.setVisibility(View.GONE);
                    mQuit.setVisibility(View.GONE);

                    Random random = new Random(System.currentTimeMillis());
                    int n = random.nextInt(5) + 1;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBackground.setBackgroundColor(Color.parseColor(colors.get(2)));
                            current_color = 2;

                            start_time = System.nanoTime();
                        }
                    }, n*1000);

                } else {

                    Fragment fragment = new ColorModeFragment();
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
                if (current_color == 2) {
                    end_time = System.nanoTime();
                    double time_difference = (end_time - start_time)/1e6;
                    Toast.makeText(getContext(), Double.toString(time_difference), Toast.LENGTH_SHORT).show();

                    mStartColorChange.setVisibility(View.VISIBLE);
                    mQuit.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getContext(), "Color not changed yet", Toast.LENGTH_SHORT).show();

                    Fragment fragment = new ColorModeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
