package com.example.superapp.core.utils;

/**
 * Application-wide constants
 */
public final class Constants {

    private Constants() {
        // Private constructor to prevent instantiation
    }

    // Feature module package names for navigation
    public static final String FEATURE_AUTH_PACKAGE = "com.example.superapp.feature.auth";
    public static final String FEATURE_CALCULATOR_PACKAGE = "com.example.superapp.feature.calculator";

    // App module package
    public static final String APP_PACKAGE = "com.example.superapp";

    // Activity class names for navigation (used by NavigationManager via Reflection)
    public static final String LOGIN_ACTIVITY = FEATURE_AUTH_PACKAGE + ".presentation.LoginActivity";
    public static final String REGISTER_ACTIVITY = FEATURE_AUTH_PACKAGE + ".presentation.RegisterActivity";
    public static final String CALCULATOR_ACTIVITY = FEATURE_CALCULATOR_PACKAGE + ".presentation.CalculatorActivity";
    public static final String DASHBOARD_ACTIVITY = APP_PACKAGE + ".DashboardActivity";

    // Intent extra keys
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_USER_EMAIL = "extra_user_email";
    public static final String EXTRA_USER_DISPLAY_NAME = "extra_user_display_name";

    // Shared Preferences
    public static final String PREF_NAME = "superapp_prefs";
    public static final String PREF_IS_LOGGED_IN = "is_logged_in";
    public static final String PREF_USER_EMAIL = "user_email";
    public static final String PREF_USER_DISPLAY_NAME = "user_display_name";
}
