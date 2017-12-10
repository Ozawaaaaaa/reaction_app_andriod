package com.sxz.reaction.fragment;

import android.content.Context;
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
import com.sxz.reaction.R;

public class SignUpFragment extends Fragment {

    private String TAG = SignUpFragment.class.getSimpleName();
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mPasswordRepeatEditText;
    private Button mSignUpButton;
    private FirebaseAuth mAuth;
    private SignInFragment.OnUserSignInListener userSignInListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            userSignInListener = (SignInFragment.OnUserSignInListener) context;
        } catch (ClassCastException castException) {
            Log.e(TAG, "The activity does not implement the listener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mEmailEditText = (EditText) v.findViewById(R.id.sign_up_email_edit);
        mPasswordEditText = (EditText) v.findViewById(R.id.sign_up_password_edit);
        mPasswordRepeatEditText = (EditText) v.findViewById(R.id.sign_up_password_repeat_edit);
        mSignUpButton = (Button) v.findViewById(R.id.sign_up_sign_up_button);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = null;
                String password = null;
                String passwordRepeat = null;
                email = mEmailEditText.getText().toString();
                password = mPasswordEditText.getText().toString();
                passwordRepeat = mPasswordRepeatEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()){
                    Toast.makeText(getContext(), "Cannot have empty fields", Toast.LENGTH_SHORT);
                    return;
                }
                if (!password.equals(passwordRepeat)){
                    Toast.makeText(getContext(), "Passwords are not same", Toast.LENGTH_SHORT);
                    return;
                }

                signUp(email, password);
            }
        });

        return v;
    }

    private void signUp(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (userSignInListener != null){
                                userSignInListener.onUserSignIn(user);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, task.getException());
                            Toast.makeText(getActivity(), task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });

    }

}
