package com.example.superapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.superapp.core.base.BaseActivity;
import com.example.superapp.core.navigation.NavigationManager;
import com.example.superapp.feature.auth.data.AuthRepository;
import com.example.superapp.feature.auth.presentation.LoginActivity;

/**
 * Main entry point of the SuperApp.
 * Checks authentication status and navigates accordingly.
 */
public class MainActivity extends BaseActivity {

    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authRepository = AuthRepository.getInstance();

        checkAuthenticationAndNavigate();
    }

    private void checkAuthenticationAndNavigate() {
        if (authRepository.isLoggedIn()) {
            // User is logged in, navigate to Dashboard
            navigateToDashboard();
        } else {
            // User is not logged in, navigate to Login
            navigateToLogin();
        }

        // Finish MainActivity so user can't go back to it
        finish();
    }

    private void navigateToLogin() {
        // Set up callback for successful login
        LoginActivity.setLoginSuccessCallback(this::navigateToDashboard);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
