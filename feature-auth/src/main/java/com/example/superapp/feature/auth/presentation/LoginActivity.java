package com.example.superapp.feature.auth.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.superapp.core.navigation.NavigationManager;
import com.example.superapp.feature.auth.R;
import com.example.superapp.feature.auth.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;

    // Callback interface for navigation after successful login
    public interface LoginSuccessCallback {
        void onLoginSuccess();
    }

    private static LoginSuccessCallback loginSuccessCallback;

    public static void setLoginSuccessCallback(LoginSuccessCallback callback) {
        loginSuccessCallback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loading.setVisibility(View.VISIBLE);
                login(binding.username.getText().toString(),
                        binding.password.getText().toString());
            }
        });

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            binding.username.setError("Email is required");
            binding.loading.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.password.setError("Password is required");
            binding.loading.setVisibility(View.GONE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.loading.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();

                            // Navigate to Dashboard
                            if (loginSuccessCallback != null) {
                                loginSuccessCallback.onLoginSuccess();
                            } else {
                                // Fallback navigation using NavigationManager
                                NavigationManager.navigateToDashboard(
                                        LoginActivity.this,
                                        "com.example.superapp.DashboardActivity"
                                );
                            }
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
