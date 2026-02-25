package com.example.superapp.core.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.superapp.core.utils.Constants;

/**
 * Centralized navigation manager for navigating between feature modules.
 * Uses Reflection (Class.forName()) to keep features decoupled from each other.
 * All navigation intents route through here so modules never import each other's Activities directly.
 */
public class NavigationManager {

    private static final String TAG = "NavigationManager";

    /**
     * Navigate to Login screen in feature-auth module.
     * Clears backstack so user cannot go back.
     */
    public static void navigateToLogin(Context context) {
        navigateToActivity(context, Constants.LOGIN_ACTIVITY,
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    /**
     * Navigate to Register screen in feature-auth module.
     */
    public static void navigateToRegister(Context context) {
        navigateToActivity(context, Constants.REGISTER_ACTIVITY, 0);
    }

    /**
     * Navigate to Calculator screen in feature-calculator module.
     */
    public static void navigateToCalculator(Context context) {
        navigateToActivity(context, Constants.CALCULATOR_ACTIVITY, 0);
    }

    /**
     * Navigate to Dashboard in app module.
     * Clears backstack so user cannot go back to login.
     */
    public static void navigateToDashboard(Context context) {
        navigateToActivity(context, Constants.DASHBOARD_ACTIVITY,
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    /**
     * Generic navigation helper using Reflection to resolve Activity class at runtime.
     * This keeps modules decoupled — no compile-time dependency between feature modules.
     *
     * @param context   The calling context
     * @param className Fully qualified class name of the target Activity
     * @param flags     Intent flags (0 for none)
     */
    private static void navigateToActivity(Context context, String className, int flags) {
        try {
            Class<?> targetClass = Class.forName(className);
            Intent intent = new Intent(context, targetClass);
            if (flags != 0) {
                intent.setFlags(flags);
            }
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Navigation failed — class not found: " + className, e);
        }
    }
}
