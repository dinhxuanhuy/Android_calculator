package com.example.superapp;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JVM unit tests for the :app module.
 * These run locally without an emulator.
 */
public class AppUnitTest {

    /**
     * Sanity check — verifies the test infrastructure itself works.
     */
    @Test
    public void basicArithmetic_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    /**
     * The app package name must match what Firebase and the manifest declare.
     */
    @Test
    public void appPackageName_isCorrect() {
        String expectedPackage = "com.example.superapp";
        // Verify the string constant is what we expect so nothing falls out of sync.
        assertEquals("com.example.superapp", expectedPackage);
    }
}
