package com.example.superapp;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test for the :app module.
 * Runs on an Android device or emulator.
 */
@RunWith(AndroidJUnit4.class)
public class AppInstrumentedTest {

    /**
     * Verifies that the app is installed with the expected package name.
     * This confirms the manifest applicationId and Firebase configuration are aligned.
     */
    @Test
    public void appContext_hasCorrectPackageName() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.superapp", appContext.getPackageName());
    }
}
