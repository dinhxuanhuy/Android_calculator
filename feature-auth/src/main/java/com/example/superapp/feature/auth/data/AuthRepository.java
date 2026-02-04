package com.example.superapp.feature.auth.data;

import com.example.superapp.feature.auth.data.model.LoggedInUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Repository class that handles authentication operations
 */
public class AuthRepository {

    private static volatile AuthRepository instance;
    private final FirebaseAuth firebaseAuth;
    private LoggedInUser user = null;

    private AuthRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public static AuthRepository getInstance() {
        if (instance == null) {
            synchronized (AuthRepository.class) {
                if (instance == null) {
                    instance = new AuthRepository();
                }
            }
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public LoggedInUser getCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            return new LoggedInUser(
                    firebaseUser.getUid(),
                    firebaseUser.getDisplayName(),
                    firebaseUser.getEmail()
            );
        }
        return null;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void logout() {
        firebaseAuth.signOut();
        user = null;
    }

    public void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }
}
