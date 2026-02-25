package com.example.superapp.feature.auth.presentation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.superapp.core.base.BaseActivity;
import com.example.superapp.feature.auth.R;
import com.example.superapp.feature.auth.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Registration screen for new users.
 *
 * Flow:
 * - Validates: name, email, password (>=6 chars), confirm match, terms checkbox
 * - Creates user via Firebase Auth createUserWithEmailAndPassword
 * - Updates Firebase User profile with displayName
 * - On success → Toast + finish() (returns to LoginActivity)
 * - On failure → Toast Firebase error message
 */
public class RegisterActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = binding.edtName.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();

        // --- Input Validation ---
        if (TextUtils.isEmpty(name)) {
            binding.edtName.setError(getString(R.string.name_required));
            binding.edtName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            binding.edtEmail.setError(getString(R.string.email_required));
            binding.edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.edtPassword.setError(getString(R.string.password_required));
            binding.edtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            binding.edtPassword.setError(getString(R.string.password_min_length));
            binding.edtPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            binding.edtConfirmPassword.setError(getString(R.string.passwords_not_match));
            binding.edtConfirmPassword.requestFocus();
            return;
        }

        if (!binding.cbTerms.isChecked()) {
            Toast.makeText(this, R.string.agree_terms, Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Show loading ---
        setFormEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        // --- Firebase Create User ---
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Update Firebase profile with display name
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdate =
                                    new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build();

                            user.updateProfile(profileUpdate)
                                    .addOnCompleteListener(profileTask -> {
                                        binding.progressBar.setVisibility(View.GONE);
                                        setFormEnabled(true);
                                        Toast.makeText(RegisterActivity.this,
                                                R.string.registration_success,
                                                Toast.LENGTH_SHORT).show();
                                        // Sign out so user must explicitly login
                                        mAuth.signOut();
                                        finish();
                                    });
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            setFormEnabled(true);
                            Toast.makeText(RegisterActivity.this,
                                    R.string.registration_success,
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        setFormEnabled(true);
                        String errorMsg = task.getException() != null
                                ? task.getException().getMessage()
                                : getString(R.string.registration_failed);
                        Toast.makeText(RegisterActivity.this,
                                getString(R.string.registration_failed) + ": " + errorMsg,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Enable/disable all form fields to prevent double-submission.
     */
    private void setFormEnabled(boolean enabled) {
        binding.edtName.setEnabled(enabled);
        binding.edtEmail.setEnabled(enabled);
        binding.edtPassword.setEnabled(enabled);
        binding.edtConfirmPassword.setEnabled(enabled);
        binding.cbTerms.setEnabled(enabled);
        binding.btnRegister.setEnabled(enabled);
    }
}
