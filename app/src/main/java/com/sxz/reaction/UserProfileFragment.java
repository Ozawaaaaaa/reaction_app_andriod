package com.sxz.reaction;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Shihao on 12/4/17.
 */

public class UserProfileFragment extends Fragment {

    private ImageView mProfileImageView;
    private EditText mEmailEditText;
    private EditText mDisplayNameEditText;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mProfileImageView = v.findViewById(R.id.user_profile_image);
        mEmailEditText = v.findViewById(R.id.user_profile_email_text);
        mDisplayNameEditText = v.findViewById(R.id.user_profile_display_name_text);

        FirebaseUser user = mAuth.getCurrentUser();

        Uri imageURL = user.getPhotoUrl();
        String email = user.getEmail();
        String displayName = user.getDisplayName();

        if (imageURL != null){
            mProfileImageView.setImageURI(imageURL);
        }

        if ( email != null && !email.isEmpty()){
            mEmailEditText.setText(email);
        }

        if ( displayName != null && !displayName.isEmpty()){
            mDisplayNameEditText.setText(displayName);
        }

        return v;
    }
}
