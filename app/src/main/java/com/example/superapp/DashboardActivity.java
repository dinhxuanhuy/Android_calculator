package com.example.superapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superapp.core.base.BaseActivity;
import com.example.superapp.core.navigation.NavigationManager;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Dashboard Activity — the mini-app container / central hub.
 *
 * Responsibilities:
 * - Header: show welcome message + email from Firebase Auth
 * - Mini-App Grid: clickable Calculator card → launches CalculatorActivity via NavigationManager
 * - Coming Soon: visually dimmed placeholder card
 * - Logout: signs out from Firebase, clears backstack, returns to LoginActivity
 */
public class DashboardActivity extends BaseActivity {

    private TextView tvWelcome, tvEmail;
    private MaterialCardView cardCalculator, cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        setupUserInfo();
        setupClickListeners();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvEmail = findViewById(R.id.tvEmail);
        cardCalculator = findViewById(R.id.cardCalculator);
        cardLogout = findViewById(R.id.cardLogout);
    }

    /**
     * Fetch Display Name and Email directly from FirebaseAuth.
     */
    private void setupUserInfo() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            String email = currentUser.getEmail();

            if (displayName != null && !displayName.isEmpty()) {
                tvWelcome.setText(getString(R.string.welcome_user, displayName));
            } else {
                tvWelcome.setText(getString(R.string.welcome_to_superapp));
            }

            if (email != null) {
                tvEmail.setText(email);
            }
        } else {
            tvWelcome.setText(getString(R.string.welcome_to_superapp));
            tvEmail.setText("");
        }
    }

    private void setupClickListeners() {
        // Calculator card → open CalculatorActivity via NavigationManager (Reflection)
        cardCalculator.setOnClickListener(v ->
                NavigationManager.navigateToCalculator(DashboardActivity.this)
        );

        // Logout card → sign out and redirect to Login
        cardLogout.setOnClickListener(v -> logout());
    }

    /**
     * Sign out from Firebase Auth and redirect to LoginActivity, clearing the entire backstack.
     */
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, R.string.logged_out, Toast.LENGTH_SHORT).show();
        NavigationManager.navigateToLogin(this);
        finish();
    }
}
