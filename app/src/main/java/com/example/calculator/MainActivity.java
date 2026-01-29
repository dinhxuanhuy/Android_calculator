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

public class MainActivity extends AppCompatActivity {
    TextView resultTv, solutionTv;
    Button button_1, button_2, button_3, button_4, button_5,
            button_6, button_7, button_8, button_9, button_0,
            button_add, button_sub, button_mul, button_div,
            button_equal, button_clear, button_dot, button_delete;

    private StringBuilder expression = new StringBuilder();
    private boolean isResultDisplayed = false;
    private static final float SIZE_LARGE = 83f;
    private static final float SIZE_SMALL = 36f;

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
        button_clear.setOnClickListener(v -> clear());
        button_delete.setOnClickListener(v -> deleteLastChar());
        number_button_event();

        setInputMode();
        solutionTv.setText("");
        resultTv.setText("");
    }

    void number_button_event() {
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
        button_add.setOnClickListener(v -> appendToExpression(" + "));
        button_sub.setOnClickListener(v -> appendToExpression(" − "));
        button_mul.setOnClickListener(v -> appendToExpression(" × "));
        button_div.setOnClickListener(v -> appendToExpression(" ÷ "));
        button_equal.setOnClickListener(v -> calculateResult());
    }

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

    private String calculateExpression(String expr) {
        if (expr.isEmpty())
            return "";

        try {
            String calcExpr = expr.replace(" + ", "+")
                    .replace(" − ", "-")
                    .replace(" × ", "*")
                    .replace(" ÷ ", "/")
                    .trim();

            if (calcExpr.endsWith("+") || calcExpr.endsWith("-") ||
                    calcExpr.endsWith("*") || calcExpr.endsWith("/")) {
                calcExpr = calcExpr.substring(0, calcExpr.length() - 1);
            }

            if (calcExpr.isEmpty())
                return "";

            double result = evaluateExpression(calcExpr);

            if (Double.isInfinite(result) || Double.isNaN(result)) {
                return "Error";
            }
            if (result == (long) result) {
                return String.valueOf((long) result);
            } else {
                return String.valueOf(result);
            }
        } catch (Exception e) {
            return "";
        }
    }

    private double evaluateExpression(String expr) {
        return parseAdditionSubtraction(expr);
    }

    private double parseAdditionSubtraction(String expr) {
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
                    if (c == '+') {
                        return parseAdditionSubtraction(left) + parseMultiplicationDivision(right);
                    } else {
                        return parseAdditionSubtraction(left) - parseMultiplicationDivision(right);
                    }
                }
            }
        }
        return parseMultiplicationDivision(expr);
    }

    private double parseMultiplicationDivision(String expr) {
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == '*' || c == '/') {
                String left = expr.substring(0, i);
                String right = expr.substring(i + 1);
                if (c == '*') {
                    return parseMultiplicationDivision(left) * parseNumber(right);
                } else {
                    return parseMultiplicationDivision(left) / parseNumber(right);
                }
            }
        }
        return parseNumber(expr);
    }

    private double parseNumber(String expr) {
        return Double.parseDouble(expr.trim());
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

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