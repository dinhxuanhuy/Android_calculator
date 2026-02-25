package com.example.superapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.superapp.core.base.BaseActivity;
import com.example.superapp.core.utils.Constants;
import com.example.superapp.feature.auth.data.AuthRepository;
import com.example.superapp.feature.auth.presentation.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Main entry point of the SuperApp.
 * Checks authentication status and navigates accordingly.
 *
 * Logic:
 * 1. Check if user is logged in via FirebaseAuth
 * 2. If logged in -> navigate to DashboardActivity
 * 3. If not logged in -> navigate to LoginActivity (feature-auth)
 * 4. Finish this activity so back press doesn't return here
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the callback so LoginActivity knows where to go after success
        LoginActivity.setLoginSuccessCallback(() -> {
            navigateToDashboard();
            // Save login state to SharedPreferences
            saveLoginState(true);
        });

        checkAuthenticationAndNavigate();
    }

    private void checkAuthenticationAndNavigate() {
        if (isLoggedIn()) {
            navigateToDashboard();
        } else {
            navigateToLogin();
        }
        finish();
    }

    /**
     * Check authentication using FirebaseAuth (primary) and SharedPreferences (fallback).
     */
    private boolean isLoggedIn() {
        // Primary check: Firebase Auth current user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            return true;
        }

        // Fallback: check SharedPreferences
        SharedPreferences prefs = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        return prefs.getBoolean(Constants.PREF_IS_LOGGED_IN, false);
    }

    private void saveLoginState(boolean isLoggedIn) {
        SharedPreferences prefs = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(Constants.PREF_IS_LOGGED_IN, isLoggedIn).apply();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
