package com.example.superapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.superapp.core.base.BaseActivity;
import com.example.superapp.core.navigation.NavigationManager;
import com.example.superapp.feature.auth.data.AuthRepository;
import com.example.superapp.feature.auth.data.model.LoggedInUser;
import com.google.android.material.card.MaterialCardView;

/**
 * Dashboard showing available mini-apps and user information
 */
public class DashboardActivity extends BaseActivity {

    private AuthRepository authRepository;
    private TextView tvWelcome, tvEmail;
    private MaterialCardView cardCalculator, cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        authRepository = AuthRepository.getInstance();

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

    private void setupUserInfo() {
        LoggedInUser currentUser = authRepository.getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                tvWelcome.setText("Welcome, " + displayName + "!");
            } else {
                tvWelcome.setText("Welcome to SuperApp!");
            }
            tvEmail.setText(currentUser.getEmail());
        }
    }

    private void setupClickListeners() {
        cardCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToCalculator(DashboardActivity.this);
            }
        });

        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        authRepository.logout();
        NavigationManager.navigateToLogin(this);
        finish();
    }
}
