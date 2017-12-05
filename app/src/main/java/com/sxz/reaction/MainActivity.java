package com.sxz.reaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class MainActivity extends SingleFragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener,
OnUserSignInListener {
    private String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private ActionBarDrawerToggle mDrawerToggle;

    private ImageView mUserPortraitImageView;
    private TextView mUserDisplayNameText;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mUserPortraitImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.drawer_profile_imageview);
        mUserDisplayNameText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_user_displayname_text);

        // After initialize all of UI, check user sign-in status
        mAuth = FirebaseAuth.getInstance();
        updateUI(mAuth.getCurrentUser());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.drawer_setting:
                break;
            case R.id.drawer_user_profile:
                openUserProfile();
                break;
            case R.id.drawer_sign_out:
                signOut();
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openUserProfile(){
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUserSignIn(FirebaseUser user) {
        Log.d(TAG,String.format("User [%s]:[%s] sign in", user.getDisplayName(), user.getEmail()));
        updateUI(user);
    }

    private void updateUI(FirebaseUser user){
        if (user == null){
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            switchFragment(new SignInFragment(), false);
        }else {
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            //TODO: switch to game
            //switchFragment(null, false);
        }
        updateDrawerUI(user);
    }

    private void updateDrawerUI(FirebaseUser user){
        if(user == null){
            mUserDisplayNameText.setText("");
            //TODO: set a default image

        }else {
            Uri imageUri = user.getPhotoUrl();
            String displayName = user.getDisplayName();

            if (imageUri != null){
                Picasso.with(this).load(imageUri).into(mUserPortraitImageView);
            }

            if ( displayName != null && !displayName.isEmpty()){
                mUserDisplayNameText.setText(displayName);
            }
        }
    }

    private void signOut(){
        mAuth.signOut();
        updateUI(null);
    }
}
