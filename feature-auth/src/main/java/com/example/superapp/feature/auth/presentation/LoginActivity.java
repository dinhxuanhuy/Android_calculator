package com.example.superapp.feature.auth.presentation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.superapp.core.base.BaseActivity;
import com.example.superapp.core.navigation.NavigationManager;
import com.example.superapp.feature.auth.R;
import com.example.superapp.feature.auth.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Login screen for the SuperApp.
 *
 * Flow:
 * - User enters email & password
 * - Validates input (empty checks)
 * - Authenticates via Firebase Auth signInWithEmailAndPassword
 * - On success → navigates to DashboardActivity via NavigationManager
 * - On failure → shows Firebase error message via Toast
 * - "Sign up here" link → opens RegisterActivity
 */
public class LoginActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.login.setOnClickListener(v -> {
            binding.loading.setVisibility(View.VISIBLE);
            login(
                    binding.username.getText().toString().trim(),
                    binding.password.getText().toString().trim()
            );
        });

        binding.tvRegister.setOnClickListener(v ->
                NavigationManager.navigateToRegister(LoginActivity.this)
        );
    }

    private void login(String email, String password) {
        // --- Input validation ---
        if (TextUtils.isEmpty(email)) {
            binding.username.setError(getString(R.string.email_required));
            binding.loading.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.password.setError(getString(R.string.password_required));
            binding.loading.setVisibility(View.GONE);
            return;
        }

        // --- Firebase Authentication ---
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    binding.loading.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,
                                R.string.auth_success, Toast.LENGTH_SHORT).show();
                        // Navigate to Dashboard via centralized NavigationManager (Reflection)
                        NavigationManager.navigateToDashboard(LoginActivity.this);
                        finish();
                    } else {
                        String errorMsg = task.getException() != null
                                ? task.getException().getMessage()
                                : getString(R.string.auth_failed);
                        Toast.makeText(LoginActivity.this,
                                getString(R.string.auth_failed) + ": " + errorMsg,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
