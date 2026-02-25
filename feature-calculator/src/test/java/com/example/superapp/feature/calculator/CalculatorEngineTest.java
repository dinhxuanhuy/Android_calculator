package com.example.superapp.feature.calculator;

import com.example.superapp.feature.calculator.logic.CalculatorEngine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JVM unit tests for {@link CalculatorEngine}.
 *
 * No Android dependencies — these run without an emulator.
 * Covers: basic ops, chaining, decimal input, divide-by-zero, error recovery, delete, clear.
 */
public class CalculatorEngineTest {

    private CalculatorEngine engine;

    @Before
    public void setup() {
        engine = new CalculatorEngine();
    }

    // ==================== Initial State ====================

    @Test
    public void initialDisplay_isZero() {
        CalculatorEngine.State s = engine.appendDigit("0"); // trigger state read
        engine.clear();
        CalculatorEngine.State cleared = engine.clear();
        assertEquals("0", cleared.display);
        assertEquals("", cleared.history);
        assertFalse(cleared.isError);
    }

    // ==================== Digit Input ====================

    @Test
    public void appendDigit_replacesLeadingZero() {
        engine.appendDigit("0");
        CalculatorEngine.State s = engine.appendDigit("5");
        assertEquals("5", s.display);
    }

    @Test
    public void appendMultipleDigits_buildsNumber() {
        engine.appendDigit("1");
        engine.appendDigit("2");
        CalculatorEngine.State s = engine.appendDigit("3");
        assertEquals("123", s.display);
    }

    // ==================== Decimal Input ====================

    @Test
    public void appendDot_addsDecimalPoint() {
        engine.appendDigit("3");
        CalculatorEngine.State s = engine.appendDot();
        assertEquals("3.", s.display);
    }

    @Test
    public void appendDot_preventsDoubleDot() {
        engine.appendDigit("3");
        engine.appendDot();
        CalculatorEngine.State s = engine.appendDot(); // second dot — should be ignored
        assertEquals("3.", s.display);
    }

    @Test
    public void appendDot_afterOperator_startsZeroDot() {
        engine.appendDigit("5");
        engine.pressOperator("+");
        CalculatorEngine.State s = engine.appendDot();
        assertEquals("0.", s.display);
    }

    // ==================== Addition ====================

    @Test
    public void addition_twoIntegers() {
        engine.appendDigit("3");
        engine.pressOperator("+");
        engine.appendDigit("5");
        CalculatorEngine.State s = engine.pressEquals();
        assertEquals("8", s.display);
        assertFalse(s.isError);
    }

    @Test
    public void addition_withDecimals() {
        engine.appendDigit("1");
        engine.appendDot();
        engine.appendDigit("5");
        engine.pressOperator("+");
        engine.appendDigit("2");
        engine.appendDot();
        engine.appendDigit("5");
        CalculatorEngine.State s = engine.pressEquals();
        assertEquals("4", s.display); // 1.5 + 2.5 = 4.0 → "4"
        assertFalse(s.isError);
    }

    // ==================== Subtraction ====================

    @Test
    public void subtraction_positiveResult() {
        engine.appendDigit("9");
        engine.pressOperator("-");
        engine.appendDigit("4");
        CalculatorEngine.State s = engine.pressEquals();
        assertEquals("5", s.display);
    }

    @Test
    public void subtraction_negativeResult() {
        engine.appendDigit("3");
        engine.pressOperator("-");
        engine.appendDigit("7");
        CalculatorEngine.State s = engine.pressEquals();
        assertEquals("-4", s.display);
    }

    // ==================== Multiplication ====================

    @Test
    public void multiplication_integers() {
        engine.appendDigit("6");
        engine.pressOperator("*");
        engine.appendDigit("7");
        CalculatorEngine.State s = engine.pressEquals();
        assertEquals("42", s.display);
    }

    @Test
    public void multiplication_byZero_givesZero() {
        engine.appendDigit("9");
        engine.pressOperator("*");
        engine.appendDigit("0");
        CalculatorEngine.State s = engine.pressEquals();
        assertEquals("0", s.display);
        assertFalse(s.isError);
    }

    // ==================== Division ====================

    @Test
    public void division_exact() {
        engine.appendDigit("1");
        engine.appendDigit("0");
        engine.pressOperator("/");
        engine.appendDigit("2");
        CalculatorEngine.State s = engine.pressEquals();
        assertEquals("5", s.display);
    }

    @Test
    public void division_byZero_returnsError() {
        engine.appendDigit("5");
        engine.pressOperator("/");
        engine.appendDigit("0");
        CalculatorEngine.State s = engine.pressEquals();
        assertTrue(s.isError);
        assertEquals("Error", s.display);
    }

    @Test
    public void afterDivisionByZero_clearResetsState() {
        engine.appendDigit("5");
        engine.pressOperator("/");
        engine.appendDigit("0");
        engine.pressEquals(); // error state
        CalculatorEngine.State cleared = engine.clear();
        assertEquals("0", cleared.display);
        assertFalse(cleared.isError);
    }

    @Test
    public void afterDivisionByZero_newDigitReplacesError() {
        engine.appendDigit("5");
        engine.pressOperator("/");
        engine.appendDigit("0");
        engine.pressEquals(); // error state
        CalculatorEngine.State s = engine.appendDigit("3");
        assertEquals("3", s.display);
        assertFalse(s.isError);
    }

    // ==================== Operator Chaining ====================

    @Test
    public void chainedAddition_3plus5plus2_equals10() {
        engine.appendDigit("3");
        engine.pressOperator("+");
        engine.appendDigit("5");
        engine.pressOperator("+"); // should chain: 3+5=8, then 8+
        engine.appendDigit("2");
        CalculatorEngine.State s = engine.pressEquals();
        assertEquals("10", s.display);
    }

    @Test
    public void chainedMultiplication_2times3times4_equals24() {
        engine.appendDigit("2");
        engine.pressOperator("*");
        engine.appendDigit("3");
        engine.pressOperator("*"); // chain: 2*3=6
        engine.appendDigit("4");
        CalculatorEngine.State s = engine.pressEquals();
        assertEquals("24", s.display);
    }

    // ==================== Result Reuse ====================

    @Test
    public void afterEquals_nextDigitStartsFresh() {
        engine.appendDigit("5");
        engine.pressOperator("+");
        engine.appendDigit("3");
        engine.pressEquals(); // result = 8
        CalculatorEngine.State s = engine.appendDigit("2"); // should start fresh, not "82"
        assertEquals("2", s.display);
    }

    // ==================== Delete ====================

    @Test
    public void delete_removesLastChar() {
        engine.appendDigit("1");
        engine.appendDigit("2");
        engine.appendDigit("3");
        CalculatorEngine.State s = engine.pressDelete();
        assertEquals("12", s.display);
    }

    @Test
    public void delete_singleDigit_givesZero() {
        engine.appendDigit("7");
        CalculatorEngine.State s = engine.pressDelete();
        assertEquals("0", s.display);
    }

    @Test
    public void delete_inErrorState_clears() {
        engine.appendDigit("5");
        engine.pressOperator("/");
        engine.appendDigit("0");
        engine.pressEquals(); // error
        CalculatorEngine.State s = engine.pressDelete();
        assertEquals("0", s.display);
        assertFalse(s.isError);
    }

    // ==================== History ====================

    @Test
    public void history_showsExpressionOnEquals() {
        engine.appendDigit("3");
        engine.pressOperator("+");
        engine.appendDigit("4");
        CalculatorEngine.State s = engine.pressEquals();
        assertEquals("3 + 4 =", s.history);
    }

    @Test
    public void history_showsDivisionSymbol() {
        engine.appendDigit("1");
        engine.appendDigit("0");
        engine.pressOperator("/");
        engine.appendDigit("2");
        CalculatorEngine.State s = engine.pressEquals();
        assertTrue("History should contain ÷", s.history.contains("÷"));
    }

    @Test
    public void history_showsMultiplicationSymbol() {
        engine.appendDigit("3");
        engine.pressOperator("*");
        engine.appendDigit("4");
        CalculatorEngine.State s = engine.pressEquals();
        assertTrue("History should contain ×", s.history.contains("×"));
    }

    @Test
    public void history_showsSubtractionSymbol() {
        engine.appendDigit("9");
        engine.pressOperator("-");
        engine.appendDigit("3");
        CalculatorEngine.State s = engine.pressEquals();
        assertTrue("History should contain −", s.history.contains("−"));
    }

    // ==================== formatNumber helper ====================

    @Test
    public void formatNumber_wholeDouble_omitsDecimalZero() {
        assertEquals("5", CalculatorEngine.formatNumber(5.0));
        assertEquals("100", CalculatorEngine.formatNumber(100.0));
        assertEquals("-4", CalculatorEngine.formatNumber(-4.0));
    }

    @Test
    public void formatNumber_fractalDouble_keeps_decimal() {
        assertEquals("3.5", CalculatorEngine.formatNumber(3.5));
    }

    // ==================== displaySymbol helper ====================

    @Test
    public void displaySymbol_convertsCorrectly() {
        assertEquals("÷", CalculatorEngine.displaySymbol("/"));
        assertEquals("×", CalculatorEngine.displaySymbol("*"));
        assertEquals("−", CalculatorEngine.displaySymbol("-"));
        assertEquals("+", CalculatorEngine.displaySymbol("+"));
    }
}
