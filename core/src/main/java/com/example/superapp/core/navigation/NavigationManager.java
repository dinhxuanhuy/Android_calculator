package com.example.superapp.core.navigation;

import android.content.Context;
import android.content.Intent;

import com.example.superapp.core.utils.Constants;

/**
 * Centralized navigation manager for navigating between feature modules
 */
public class NavigationManager {

    /**
     * Navigate to Login screen in feature-auth module
     */
    public static void navigateToLogin(Context context) {
        try {
            Class<?> loginClass = Class.forName(Constants.LOGIN_ACTIVITY);
            Intent intent = new Intent(context, loginClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigate to Register screen in feature-auth module
     */
    public static void navigateToRegister(Context context) {
        try {
            Class<?> registerClass = Class.forName(Constants.REGISTER_ACTIVITY);
            Intent intent = new Intent(context, registerClass);
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigate to Calculator screen in feature-calculator module
     */
    public static void navigateToCalculator(Context context) {
        try {
            Class<?> calculatorClass = Class.forName(Constants.CALCULATOR_ACTIVITY);
            Intent intent = new Intent(context, calculatorClass);
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigate to Dashboard in app module
     */
    public static void navigateToDashboard(Context context, String dashboardActivityClassName) {
        try {
            Class<?> dashboardClass = Class.forName(dashboardActivityClassName);
            Intent intent = new Intent(context, dashboardClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
