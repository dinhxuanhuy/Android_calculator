package com.example.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {
    // ==================== UI Components ====================
    TextView resultTv, solutionTv;
    Button button_1, button_2, button_3, button_4, button_5,
            button_6, button_7, button_8, button_9, button_0,
            button_add, button_sub, button_mul, button_div,
            button_equal, button_clear, button_dot, button_delete;

    // ==================== State Variables ====================
    private StringBuilder expression = new StringBuilder();
    private boolean isResultDisplayed = false;

    // ==================== Constants ====================
    private static final float SIZE_LARGE = 83f;
    private static final float SIZE_SMALL = 36f;

    // BigDecimal Calculation Settings
    private static final int CALCULATION_SCALE = 15; // Internal precision for calculations
    private static final int DISPLAY_SCALE = 10; // Max decimal places for display
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupButtonListeners();
        initializeDisplay();
    }

    /**
     * Initialize all UI view references
     */
    private void initializeViews() {
        resultTv = findViewById(R.id.tv_result);
        solutionTv = findViewById(R.id.tv_history);
        button_0 = findViewById(R.id.btn_0);
        button_1 = findViewById(R.id.btn_1);
        button_2 = findViewById(R.id.btn_2);
        button_3 = findViewById(R.id.btn_3);
        button_4 = findViewById(R.id.btn_4);
        button_5 = findViewById(R.id.btn_5);
        button_6 = findViewById(R.id.btn_6);
        button_7 = findViewById(R.id.btn_7);
        button_8 = findViewById(R.id.btn_8);
        button_9 = findViewById(R.id.btn_9);
        button_add = findViewById(R.id.btn_add);
        button_sub = findViewById(R.id.btn_subtract);
        button_mul = findViewById(R.id.btn_multiply);
        button_div = findViewById(R.id.btn_divide);
        button_equal = findViewById(R.id.btn_equals);
        button_clear = findViewById(R.id.btn_clear);
        button_delete = findViewById(R.id.btn_delete);
    }

    /**
     * Setup all button click listeners
     */
    private void setupButtonListeners() {
        button_clear.setOnClickListener(v -> clear());
        button_delete.setOnClickListener(v -> deleteLastChar());

        // Number buttons
        button_0.setOnClickListener(v -> appendToExpression("0"));
        button_1.setOnClickListener(v -> appendToExpression("1"));
        button_2.setOnClickListener(v -> appendToExpression("2"));
        button_3.setOnClickListener(v -> appendToExpression("3"));
        button_4.setOnClickListener(v -> appendToExpression("4"));
        button_5.setOnClickListener(v -> appendToExpression("5"));
        button_6.setOnClickListener(v -> appendToExpression("6"));
        button_7.setOnClickListener(v -> appendToExpression("7"));
        button_8.setOnClickListener(v -> appendToExpression("8"));
        button_9.setOnClickListener(v -> appendToExpression("9"));

        // Operator buttons
        button_add.setOnClickListener(v -> appendToExpression(" + "));
        button_sub.setOnClickListener(v -> appendToExpression(" − "));
        button_mul.setOnClickListener(v -> appendToExpression(" × "));
        button_div.setOnClickListener(v -> appendToExpression(" ÷ "));

        // Equal button
        button_equal.setOnClickListener(v -> calculateResult());
    }

    /**
     * Initialize display state
     */
    private void initializeDisplay() {
        setInputMode();
        solutionTv.setText("");
        resultTv.setText("");
    }

    // ==================== UI Mode Management ====================

    private void setInputMode() {
        solutionTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, SIZE_LARGE);
        solutionTv.setTextColor(0xFFFFFFFF); // White
        resultTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, SIZE_SMALL);
        resultTv.setTextColor(0x80FFFFFF); // Semi-transparent white
    }

    private void setResultMode() {
        solutionTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, SIZE_SMALL);
        solutionTv.setTextColor(0x80FFFFFF); // Semi-transparent white
        resultTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, SIZE_LARGE);
        resultTv.setTextColor(0xFFFFFFFF); // White
    }

    // ==================== Expression Input Handling ====================

    @SuppressLint("SetTextI18n")
    void appendToExpression(String value) {
        if (isResultDisplayed) {
            String lastResult = resultTv.getText().toString();
            if (value.contains("+") || value.contains("−") || value.contains("×") || value.contains("÷")) {
                expression = new StringBuilder(lastResult);
            } else {
                expression = new StringBuilder();
            }
            isResultDisplayed = false;
            setInputMode();
        }

        if (expression.length() == 0 &&
                (value.equals(" + ") || value.equals(" × ") || value.equals(" ÷ "))) {
            return;
        }

        String exprStr = expression.toString();
        if (exprStr.length() > 0) {
            boolean lastIsOperator = exprStr.endsWith(" + ") || exprStr.endsWith(" − ") ||
                    exprStr.endsWith(" × ") || exprStr.endsWith(" ÷ ");
            boolean newIsOperator = value.equals(" + ") || value.equals(" − ") ||
                    value.equals(" × ") || value.equals(" ÷ ");
            if (lastIsOperator && newIsOperator) {
                // Replace last operator with new one
                expression = new StringBuilder(exprStr.substring(0, exprStr.length() - 3));
            }
        }

        expression.append(value);
        updateDisplay();
    }

    @SuppressLint("SetTextI18n")
    private void updateDisplay() {
        solutionTv.setText(expression.toString());
        String result = calculateExpression(expression.toString());
        resultTv.setText(result);
    }

    // ==================== Calculation Engine (BigDecimal) ====================

    /**
     * Main entry point for expression calculation
     * Preserves original UI logic while using BigDecimal internally
     */
    private String calculateExpression(String expr) {
        if (expr.isEmpty())
            return "";

        try {
            // Convert display operators to calculation operators
            String calcExpr = normalizeExpression(expr);

            // Remove trailing operator if present
            calcExpr = removeTrailingOperator(calcExpr);

            if (calcExpr.isEmpty())
                return "";

            // Evaluate using BigDecimal
            BigDecimal result = evaluateExpression(calcExpr);

            // Format result for display
            return formatResult(result);

        } catch (ArithmeticException e) {
            // Handle division by zero or other arithmetic errors
            return "Error";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Normalize expression: convert display operators to calculation operators
     */
    private String normalizeExpression(String expr) {
        return expr.replace(" + ", "+")
                .replace(" − ", "-")
                .replace(" × ", "*")
                .replace(" ÷ ", "/")
                .trim();
    }

    /**
     * Remove trailing operator from expression
     */
    private String removeTrailingOperator(String calcExpr) {
        if (calcExpr.endsWith("+") || calcExpr.endsWith("-") ||
                calcExpr.endsWith("*") || calcExpr.endsWith("/")) {
            return calcExpr.substring(0, calcExpr.length() - 1);
        }
        return calcExpr;
    }

    /**
     * Format BigDecimal result for clean display
     * Uses stripTrailingZeros() to show "5" instead of "5.0000"
     */
    private String formatResult(BigDecimal result) {
        // Round to display scale
        BigDecimal rounded = result.setScale(DISPLAY_SCALE, ROUNDING_MODE);

        // Strip trailing zeros for clean display
        BigDecimal stripped = rounded.stripTrailingZeros();

        // Use toPlainString() to avoid scientific notation (e.g., "1E+1" -> "10")
        return stripped.toPlainString();
    }

    /**
     * Main expression evaluator - entry point for recursive descent parser
     */
    private BigDecimal evaluateExpression(String expr) {
        return parseAdditionSubtraction(expr);
    }

    /**
     * Parse addition and subtraction (lowest precedence)
     * Scans from right to left to handle left-to-right associativity
     */
    private BigDecimal parseAdditionSubtraction(String expr) {
        int parenDepth = 0;
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')')
                parenDepth++;
            else if (c == '(')
                parenDepth--;
            else if (parenDepth == 0 && (c == '+' || c == '-')) {
                if (i > 0 && !isOperator(expr.charAt(i - 1))) {
                    String left = expr.substring(0, i);
                    String right = expr.substring(i + 1);

                    BigDecimal leftValue = parseAdditionSubtraction(left);
                    BigDecimal rightValue = parseMultiplicationDivision(right);

                    if (c == '+') {
                        return leftValue.add(rightValue);
                    } else {
                        return leftValue.subtract(rightValue);
                    }
                }
            }
        }
        return parseMultiplicationDivision(expr);
    }

    /**
     * Parse multiplication and division (higher precedence)
     * Scans from right to left to handle left-to-right associativity
     */
    private BigDecimal parseMultiplicationDivision(String expr) {
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == '*' || c == '/') {
                String left = expr.substring(0, i);
                String right = expr.substring(i + 1);

                BigDecimal leftValue = parseMultiplicationDivision(left);
                BigDecimal rightValue = parseNumber(right);

                if (c == '*') {
                    return leftValue.multiply(rightValue);
                } else {
                    // Use divide with scale and rounding mode to handle infinite decimals
                    // Example: 1/3 = 0.333... won't crash
                    return leftValue.divide(rightValue, CALCULATION_SCALE, ROUNDING_MODE);
                }
            }
        }
        return parseNumber(expr);
    }

    /**
     * Parse a number string to BigDecimal
     */
    private BigDecimal parseNumber(String expr) {
        return new BigDecimal(expr.trim());
    }

    /**
     * Check if character is an operator
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // ==================== User Actions ====================

    @SuppressLint("SetTextI18n")
    void calculateResult() {
        if (expression.length() == 0)
            return;

        String result = calculateExpression(expression.toString());
        if (result.isEmpty() || result.equals("Error")) {
            resultTv.setText("Error");
            return;
        }

        setResultMode();
        resultTv.setText(result);
        isResultDisplayed = true;
    }

    void clear() {
        expression = new StringBuilder();
        solutionTv.setText("0");
        resultTv.setText("0");
        isResultDisplayed = false;
        setInputMode();
    }

    void deleteLastChar() {
        if (expression.length() == 0)
            return;
        if (isResultDisplayed) {
            isResultDisplayed = false;
            setInputMode();
        }

        String exprStr = expression.toString();

        if (exprStr.endsWith(" + ") || exprStr.endsWith(" − ") ||
                exprStr.endsWith(" × ") || exprStr.endsWith(" ÷ ")) {
            expression = new StringBuilder(exprStr.substring(0, exprStr.length() - 3));
        } else if (exprStr.length() > 0) {
            expression = new StringBuilder(exprStr.substring(0, exprStr.length() - 1));
        }
        updateDisplay();
    }
}