package com.sxz.reaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment {

    private static final int SIGNUP_REQUEST = 1;
    private String TAG = SignInFragment.class.getSimpleName();

    private FirebaseAuth mAuth;

    private Button mSignUpButton;
    private Button mSignInButton;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private OnUserSignInListener userSignInListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            userSignInListener = (OnUserSignInListener) context;
        } catch (ClassCastException castException) {
            Log.e(TAG, "The activity does not implement the listener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mSignUpButton = (Button)v.findViewById(R.id.sign_in_sign_up_button);
        mSignInButton = (Button)v.findViewById(R.id.sign_in_sign_in_button);
        mEmailEditText = (EditText) v.findViewById(R.id.sign_in_email_edit);
        mPasswordEditText = (EditText) v.findViewById(R.id.sign_in_password_edit);


        // OnClick for sign up button
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleFragmentActivity mainActivity = (SingleFragmentActivity) getActivity();
                mainActivity.switchFragment(new SignUpFragment(), true);
            }
        });

        // OnClick for sign in button
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = null;
                String password = null;

                email = mEmailEditText.getText().toString();
                password = mPasswordEditText.getText().toString();

                if (email.isEmpty()){
                    Toast.makeText(getContext(), "Email is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()){
                    Toast.makeText(getContext(), "Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                signIn(email, password);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SIGNUP_REQUEST:
                break;
            default:
                break;
        }
    }

    private void signIn(String email, String password){
        final Activity Self = getActivity();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Self, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "OnComplete");
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (userSignInListener != null){
                                userSignInListener.onUserSignIn(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Self, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }
}
