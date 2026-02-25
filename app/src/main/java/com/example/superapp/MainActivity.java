package com.example.superapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.superapp.core.base.BaseActivity;
import com.example.superapp.core.navigation.NavigationManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Main entry point of the SuperApp.
 * This is a "routing" Activity that checks authentication state and navigates accordingly.
 *
 * Flow:
 * 1. Check FirebaseAuth.getInstance().getCurrentUser()
 * 2. If user exists (logged in)  -> navigate to DashboardActivity
 * 3. If user is null (logged out) -> navigate to LoginActivity (feature-auth)
 * 4. Finish immediately so it's never on the backstack.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeToNextScreen();
    }

    private void routeToNextScreen() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // User is authenticated -> go to Dashboard
            startActivity(new Intent(this, DashboardActivity.class));
        } else {
            // User is not authenticated -> go to Login (in feature-auth)
            NavigationManager.navigateToLogin(this);
        }
        finish();
    }
}
