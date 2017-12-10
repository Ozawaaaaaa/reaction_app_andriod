package com.sxz.reaction.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.sxz.reaction.R;
import com.sxz.reaction.Utility;
import com.sxz.reaction.activity.SingleFragmentActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Shihao on 12/4/17.
 */

public class UserProfileFragment extends Fragment {

    private static final int RC_CAMERA = 1;
    private static final int RC_SELECT_FILE = 2;
    private ImageView mProfileImageView;
    private EditText mEmailEditText;
    private EditText mDisplayNameEditText;
    private Button mChangePasswordButton;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;

    private String mUserChoosenTask;
    private Bitmap choosenBitmap;

    private int maxProgress;
    private int currentProgress;
    private OnFailureListener onFailure;
    private OnSuccessListener<UploadTask.TaskSnapshot> onUploadFileSuccess;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        onFailure = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        };

        onUploadFileSuccess = new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                increaseProgressBar();

                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                UserProfileChangeRequest request = new UserProfileChangeRequest
                        .Builder()
                        .setPhotoUri(downloadUrl).build();

                FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .updateProfile(request)
                        .addOnFailureListener(onFailure)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finishProgressBar();
                                mProfileImageView.setImageBitmap(choosenBitmap);
                                Toast.makeText(getContext(), "Successfully changed image", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mProfileImageView = v.findViewById(R.id.user_profile_image);
        mEmailEditText = v.findViewById(R.id.user_profile_email_text);
        mDisplayNameEditText = v.findViewById(R.id.user_profile_display_name_text);
        mChangePasswordButton = v.findViewById(R.id.user_profile_change_password_button);
        mProgressBar = v.findViewById(R.id.user_profile_progress);
        mProgressBar.setVisibility(View.INVISIBLE);
        maxProgress = 4;
        mProgressBar.setMax(maxProgress);
        mChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleFragmentActivity activity = (SingleFragmentActivity) getActivity();
                activity.switchFragment(ChangePasswordFragment.newInstance(), true);
            }
        });

        mProfileImageView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = { "Take Photo", "Choose from Library",
                        "Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose a photo from");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        boolean result= Utility.checkPermission(getActivity());

                        if (items[item].equals("Take Photo")) {
                            mUserChoosenTask="Take Photo";
                            if(result) startCameraIntent();
                        } else if (items[item].equals("Choose from Library")) {
                            mUserChoosenTask="Choose from Library";
                            if(result) startGalleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        }));

        updateUI(mAuth.getCurrentUser());

        return v;
    }
    private void startCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RC_CAMERA);
    }

    private void startGalleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), RC_SELECT_FILE);
    }

    private void updateUI(FirebaseUser user){
        if (user == null){
            return;
        }
        Uri imageURI = user.getPhotoUrl();
        String email = user.getEmail();
        String displayName = user.getDisplayName();

        if (imageURI != null){
            Picasso.with(getContext()).load(imageURI).into(mProfileImageView);
        }

        if ( email != null && !email.isEmpty()){
            mEmailEditText.setText(email);
        }

        if ( displayName != null && !displayName.isEmpty()){
            mDisplayNameEditText.setText(displayName);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mUserChoosenTask.equals("Take Photo"))
                        startCameraIntent();
                    else if(mUserChoosenTask.equals("Choose from Library"))
                        startGalleryIntent();
                } else {
                    Toast.makeText(getContext(), "I need your permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == RC_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                choosenBitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mProfileImageView.setImageBitmap(bm);
            uploadImage(mAuth.getCurrentUser(), choosenBitmap, this.onFailure, this.onUploadFileSuccess);
        }
    }

    private void onCaptureImageResult(Intent data) {
        choosenBitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        choosenBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mProfileImageView.setImageBitmap(thumbnail);
        uploadImage(mAuth.getCurrentUser(), choosenBitmap, this.onFailure, this.onUploadFileSuccess);
    }

    private void increaseProgressBar(){
        if (currentProgress >= maxProgress){
            return;
        } else{
            currentProgress += 1;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mProgressBar.setProgress(currentProgress, true);
        }else{
            mProgressBar.setProgress(currentProgress);
        }
    }

    private void finishProgressBar(){
        mProgressBar.setProgress(maxProgress);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void uploadImage(FirebaseUser user, Bitmap bitmap, OnFailureListener onFailure, OnSuccessListener<UploadTask.TaskSnapshot> onSuccess ){
        mProgressBar.setVisibility(View.VISIBLE);
        currentProgress = 0;
        increaseProgressBar();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference("images").child(user.getUid()+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(onFailure).addOnSuccessListener(onSuccess);

    }
}
