package com.example.superapp.feature.calculator.logic;

/**
 * Pure-Java calculator engine — no Android dependencies.
 *
 * Responsibilities:
 * - Append digits / decimal points with validation
 * - Handle operator chaining (e.g. 3 + 5 * = 40)
 * - Evaluate the pending expression on equals
 * - Expose a clean state via {@link State}
 *
 * Keeping all math logic here (not in the Activity) makes it testable
 * as a plain JVM unit test without an emulator.
 */
public class CalculatorEngine {

    // ==================== State ====================

    public static class State {
        public final String display;   // current number on screen
        public final String history;   // expression shown above display
        public final boolean isError;  // true when "Error" is visible

        State(String display, String history, boolean isError) {
            this.display = display;
            this.history = history;
            this.isError = isError;
        }
    }

    // ==================== Fields ====================

    private String display = "0";
    private String history = "";
    private double firstNumber = 0;
    private String operator = "";
    private boolean operatorPending = false;   // operator was pressed, awaiting second number
    private boolean resultJustShown = false;   // equals was pressed; next digit starts fresh
    private boolean isError = false;

    // ==================== Public API ====================

    public State appendDigit(String digit) {
        if (isError || resultJustShown) {
            display = digit;
            isError = false;
            resultJustShown = false;
        } else if (display.equals("0")) {
            display = digit;
        } else {
            display = display + digit;
        }
        return state();
    }

    public State appendDot() {
        if (isError) {
            display = "0.";
            isError = false;
            return state();
        }
        if (resultJustShown) {
            display = "0.";
            resultJustShown = false;
            return state();
        }
        // After an operator was pressed the display is empty — start with "0."
        if (display.isEmpty()) {
            display = "0.";
            return state();
        }
        // Prevent double dot
        if (!display.contains(".")) {
            display = display + ".";
        }
        return state();
    }

    public State pressOperator(String op) {
        if (display.isEmpty() || isError) return state();

        // Chain operator — calculate intermediate result first
        if (operatorPending && !display.isEmpty()) {
            State intermediate = pressEquals();
            if (intermediate.isError) return intermediate;
        }

        try {
            firstNumber = Double.parseDouble(display);
        } catch (NumberFormatException e) {
            return errorState();
        }

        operator = op;
        operatorPending = true;
        resultJustShown = false;
        history = formatNumber(firstNumber) + " " + displaySymbol(op);
        display = "";

        return state();
    }

    public State pressEquals() {
        if (operator.isEmpty() || display.isEmpty() || isError) return state();

        double second;
        try {
            second = Double.parseDouble(display);
        } catch (NumberFormatException e) {
            return errorState();
        }

        double result;
        switch (operator) {
            case "+": result = firstNumber + second; break;
            case "-": result = firstNumber - second; break;
            case "*": result = firstNumber * second; break;
            case "/":
                if (second == 0) {
                    history = formatNumber(firstNumber) + " ÷ 0";
                    display = "Error";
                    isError = true;
                    operator = "";
                    operatorPending = false;
                    resultJustShown = false;
                    return state();
                }
                result = firstNumber / second;
                break;
            default:
                return errorState();
        }

        history = formatNumber(firstNumber) + " " + displaySymbol(operator)
                + " " + formatNumber(second) + " =";
        display = formatNumber(result);
        firstNumber = result;
        operator = "";
        operatorPending = false;
        resultJustShown = true;

        return state();
    }

    public State pressDelete() {
        if (isError) return clear();
        if (display.length() > 1) {
            display = display.substring(0, display.length() - 1);
        } else if (!display.equals("0")) {
            display = "0";
        }
        return state();
    }

    public State clear() {
        display = "0";
        history = "";
        firstNumber = 0;
        operator = "";
        operatorPending = false;
        resultJustShown = false;
        isError = false;
        return state();
    }

    // ==================== Helpers ====================

    private State state() {
        return new State(display, history, isError);
    }

    private State errorState() {
        display = "Error";
        isError = true;
        operator = "";
        operatorPending = false;
        return state();
    }

    /**
     * Formats a double for display — strips trailing ".0" for whole numbers.
     */
    public static String formatNumber(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    /**
     * Maps internal operator symbol to display symbol.
     */
    public static String displaySymbol(String op) {
        switch (op) {
            case "/": return "÷";
            case "*": return "×";
            case "-": return "−";
            default:  return op;
        }
    }

    // Package-private getters for white-box testing
    String getDisplay() { return display; }
    String getOperator() { return operator; }
    double getFirstNumber() { return firstNumber; }
}
