package com.example.superapp.feature.calculator.presentation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.superapp.core.base.BaseActivity;
import com.example.superapp.feature.calculator.R;

/**
 * Calculator Mini-App.
 *
 * Implements standard operations (+, -, *, /), decimal point,
 * clear (C), delete (backspace), and chained operations.
 * Handles edge cases: division by zero, double-dot, "Error" state recovery.
 * Uses Neumorphic design styling from :core-ui.
 */
public class CalculatorActivity extends BaseActivity {

    private TextView resultTv, solutionTv;
    private String currentOperator = "";
    private double firstNumber = 0;
    private boolean isOperatorPressed = false;
    private boolean isResultDisplayed = false;

    private Button button_1, button_2, button_3, button_4, button_5,
            button_6, button_7, button_8, button_9, button_0,
            button_add, button_sub, button_mul, button_div,
            button_equal, button_clear, button_dot, button_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupClickListeners();
    }

    private void initViews() {
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
        button_dot = findViewById(R.id.btn_dot);
        button_delete = findViewById(R.id.btn_delete);
    }

    private void setupClickListeners() {
        // Utility buttons
        button_clear.setOnClickListener(v -> clear());
        button_delete.setOnClickListener(v -> deleteLastChar());

        // Decimal point
        button_dot.setOnClickListener(v -> appendDot());

        // Number buttons
        button_0.setOnClickListener(v -> appendNumber("0"));
        button_1.setOnClickListener(v -> appendNumber("1"));
        button_2.setOnClickListener(v -> appendNumber("2"));
        button_3.setOnClickListener(v -> appendNumber("3"));
        button_4.setOnClickListener(v -> appendNumber("4"));
        button_5.setOnClickListener(v -> appendNumber("5"));
        button_6.setOnClickListener(v -> appendNumber("6"));
        button_7.setOnClickListener(v -> appendNumber("7"));
        button_8.setOnClickListener(v -> appendNumber("8"));
        button_9.setOnClickListener(v -> appendNumber("9"));

        // Operator buttons
        button_add.setOnClickListener(v -> operatorButtonEvent("+"));
        button_sub.setOnClickListener(v -> operatorButtonEvent("-"));
        button_mul.setOnClickListener(v -> operatorButtonEvent("*"));
        button_div.setOnClickListener(v -> operatorButtonEvent("/"));

        // Equals
        button_equal.setOnClickListener(v -> equalButtonEvent());
    }

    // ==================== NUMBER & DOT INPUT ====================

    @SuppressLint("SetTextI18n")
    private void appendNumber(String number) {
        // If error is displayed or result was just shown, start fresh input
        if (isErrorState() || isResultDisplayed) {
            resultTv.setText(number);
            isResultDisplayed = false;
            return;
        }

        String currentText = resultTv.getText().toString();
        if (currentText.equals("0")) {
            resultTv.setText(number);
        } else {
            resultTv.setText(currentText + number);
        }
    }

    @SuppressLint("SetTextI18n")
    private void appendDot() {
        if (isErrorState()) {
            resultTv.setText("0.");
            return;
        }

        if (isResultDisplayed) {
            resultTv.setText("0.");
            isResultDisplayed = false;
            return;
        }

        String currentText = resultTv.getText().toString();

        // Prevent double dot: "3.14." is invalid
        if (currentText.contains(".")) {
            return;
        }

        resultTv.setText(currentText + ".");
    }

    // ==================== OPERATOR ====================

    private void operatorButtonEvent(String operator) {
        String currentText = resultTv.getText().toString();
        if (currentText.isEmpty() || isErrorState()) {
            return;
        }

        // If an operator was already pressed, chain-calculate first
        if (isOperatorPressed && !currentText.isEmpty()) {
            equalButtonEvent();
            // After chained calculation, the result is now in resultTv
            currentText = resultTv.getText().toString();
            if (isErrorState()) return;
        }

        try {
            firstNumber = Double.parseDouble(currentText);
        } catch (NumberFormatException e) {
            return;
        }

        currentOperator = operator;
        isOperatorPressed = true;
        isResultDisplayed = false;

        // Show expression in history
        solutionTv.setText(formatNumber(firstNumber) + " " + getDisplayOperator(operator));

        // Clear result for next input
        resultTv.setText("");
    }

    // ==================== EQUALS ====================

    private void equalButtonEvent() {
        String currentText = resultTv.getText().toString();
        if (currentOperator.isEmpty() || currentText.isEmpty() || isErrorState()) {
            return;
        }

        double secondNumber;
        try {
            secondNumber = Double.parseDouble(currentText);
        } catch (NumberFormatException e) {
            return;
        }

        double result = 0;

        switch (currentOperator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                if (secondNumber == 0) {
                    // Division by zero → show error gracefully
                    solutionTv.setText(formatNumber(firstNumber) + " ÷ 0");
                    resultTv.setText(getString(R.string.error));
                    Toast.makeText(this, R.string.divide_by_zero, Toast.LENGTH_SHORT).show();
                    currentOperator = "";
                    isOperatorPressed = false;
                    isResultDisplayed = false;
                    return;
                }
                result = firstNumber / secondNumber;
                break;
        }

        // Show full expression in history
        solutionTv.setText(formatNumber(firstNumber) + " "
                + getDisplayOperator(currentOperator) + " "
                + formatNumber(secondNumber) + " =");

        // Display result
        resultTv.setText(formatNumber(result));

        // Prepare for chaining: store result as firstNumber
        firstNumber = result;
        currentOperator = "";
        isOperatorPressed = false;
        isResultDisplayed = true;
    }

    // ==================== CLEAR & DELETE ====================

    private void clear() {
        solutionTv.setText("");
        resultTv.setText("0");
        firstNumber = 0;
        currentOperator = "";
        isOperatorPressed = false;
        isResultDisplayed = false;
    }

    private void deleteLastChar() {
        if (isErrorState()) {
            clear();
            return;
        }
        String currentText = resultTv.getText().toString();
        if (currentText.length() > 1) {
            resultTv.setText(currentText.substring(0, currentText.length() - 1));
        } else if (currentText.length() == 1 && !currentText.equals("0")) {
            resultTv.setText("0");
        }
    }

    // ==================== HELPERS ====================

    /**
     * Check if the display currently shows an error state.
     */
    private boolean isErrorState() {
        String text = resultTv.getText().toString();
        return text.equals(getString(R.string.error)) || text.equalsIgnoreCase("Error");
    }

    /**
     * Format a number for display: remove trailing ".0" for whole numbers.
     */
    private String formatNumber(double number) {
        if (number == (long) number) {
            return String.valueOf((long) number);
        }
        return String.valueOf(number);
    }

    /**
     * Return the display symbol for an operator (e.g. "/" → "÷", "*" → "×").
     */
    private String getDisplayOperator(String operator) {
        switch (operator) {
            case "/": return "÷";
            case "*": return "×";
            case "-": return "−";
            default:  return operator;
        }
    }
}
