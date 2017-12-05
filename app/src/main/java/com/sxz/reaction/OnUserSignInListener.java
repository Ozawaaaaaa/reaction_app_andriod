package com.sxz.reaction;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Shihao on 12/3/17.
 */

public interface OnUserSignInListener {
    public void onUserSignIn(FirebaseUser user);
}
