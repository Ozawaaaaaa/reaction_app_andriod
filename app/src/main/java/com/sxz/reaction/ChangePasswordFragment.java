package com.sxz.reaction;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {

    private EditText mNewPasswordEditText;
    private EditText mNewRepeatPasswordEditText;
    private Button mSubmitButton;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v  = inflater.inflate(R.layout.fragment_change_password, container, false);

        mNewPasswordEditText = (EditText) v.findViewById(R.id.change_password_new_edit);
        mNewRepeatPasswordEditText = (EditText) v.findViewById(R.id.change_password_new_repeat_edit);
        mSubmitButton = (Button) v.findViewById(R.id.change_password_submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = mNewPasswordEditText.getText().toString();
                String newRepeatPassword = mNewRepeatPasswordEditText.getText().toString();

                if(! newPassword.equals(newRepeatPassword)){
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }else{
                    changePassword(newPassword, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                getActivity().onBackPressed();
                            }else {
                                Toast.makeText(getContext(), task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return v;
    }

    private void changePassword(String newPassword, OnCompleteListener<Void> callback){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            return;
        }
        user.updatePassword( newPassword ).addOnCompleteListener(callback);
    }

}
