package com.example.superapp.core.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Base Activity that all activities in the app should extend.
 * Provides common functionality across all activities.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Shows a loading indicator
     */
    protected void showLoading() {
        // Override in child classes if needed
    }

    /**
     * Hides the loading indicator
     */
    protected void hideLoading() {
        // Override in child classes if needed
    }
}
