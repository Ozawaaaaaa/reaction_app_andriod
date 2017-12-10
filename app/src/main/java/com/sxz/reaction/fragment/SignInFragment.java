package com.sxz.reaction.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.sxz.reaction.R;
import com.sxz.reaction.activity.SingleFragmentActivity;

public class SignInFragment extends Fragment {

    private static final int RC_SIGN_IN = 1;
    private String TAG = SignInFragment.class.getSimpleName();

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Shimmer mShimmer;

    private ShimmerTextView mTitleShimmerTextView;
    private Button mSignUpButton;
    private Button mSignInWithEmailButton;
    private Button mSignInWithGoogleButton;
    private Button mSignInWithAnonymousButton;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private OnUserSignInListener userSignInListener;

    private OnCompleteListener<AuthResult> onAuthResult;

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
        // Initialize auth result callback

        onAuthResult =  new OnCompleteListener<AuthResult>() {
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
                    Toast.makeText(getActivity(), task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        };

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null){
            firebaseAuthWithGoogle(account);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mSignUpButton = (Button)v.findViewById(R.id.sign_in_sign_up_button);
        mSignInWithEmailButton = (Button)v.findViewById(R.id.sign_in_sign_in_button);
        mSignInWithGoogleButton = (Button) v.findViewById(R.id.sign_in_google_button);
        mSignInWithAnonymousButton = (Button) v.findViewById(R.id.sign_in_anon_button);
        mEmailEditText = (EditText) v.findViewById(R.id.sign_in_email_edit);
        mPasswordEditText = (EditText) v.findViewById(R.id.sign_in_password_edit);
        mTitleShimmerTextView = (ShimmerTextView) v.findViewById(R.id.sign_in_text_title);

        mShimmer = new Shimmer();
        mShimmer.start(mTitleShimmerTextView);
        // OnClick for sign up button
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleFragmentActivity mainActivity = (SingleFragmentActivity) getActivity();
                mainActivity.switchFragment(new SignUpFragment(), true);
            }
        });

        // OnClick for sign in with email button
        mSignInWithEmailButton.setOnClickListener(new View.OnClickListener() {
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
                signInWithEmail(email, password);
            }
        });

        // OnClick for sign in with google button
        mSignInWithGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });


        mSignInWithAnonymousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithAnonymous();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    Log.w(TAG, "Google sign in failed", e);
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signInWithEmail(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), this.onAuthResult);
    }

    private void signInWithAnonymous(){
        mAuth.signInAnonymously()
                .addOnCompleteListener(getActivity(), this.onAuthResult);
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        String idToken = acct.getIdToken();
        if (idToken == null || idToken.isEmpty()){
            Toast.makeText(getActivity(), "Google Authentication failed",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), this.onAuthResult);
    }

    public interface OnUserSignInListener {
        public void onUserSignIn(FirebaseUser user);
    }
}
