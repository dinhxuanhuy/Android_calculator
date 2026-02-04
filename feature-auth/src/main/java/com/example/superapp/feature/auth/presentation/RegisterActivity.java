package com.example.superapp.feature.auth.presentation;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.superapp.feature.auth.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

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

        if (TextUtils.isEmpty(name)) {
            binding.edtName.setError("Name is required");
            binding.edtName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            binding.edtEmail.setError("Email is required");
            binding.edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.edtPassword.setError("Password is required");
            binding.edtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            binding.edtPassword.setError("Password must be at least 6 characters");
            binding.edtPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            binding.edtConfirmPassword.setError("Passwords do not match");
            binding.edtConfirmPassword.requestFocus();
            return;
        }

        if (!binding.cbTerms.isChecked()) {
            Toast.makeText(this, "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
