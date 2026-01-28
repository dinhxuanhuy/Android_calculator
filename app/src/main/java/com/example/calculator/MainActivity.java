package com.example.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    TextView resultTv, solutionTv;
    String currentOperator = "";
    double firstNumber = 0;
    boolean isOperatorPressed = false;
    Button button_1, button_2, button_3, button_4, button_5,
            button_6, button_7, button_8, button_9, button_0,
            button_add, button_sub, button_mul, button_div,
            button_equal, button_clear, button_dot;




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
        button_clear.setOnClickListener(v -> clear());
        number_button_event();

    }

    void number_button_event() {
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
        button_add.setOnClickListener(v -> operator_button_event("+"));
        button_sub.setOnClickListener(v -> operator_button_event("-"));
        button_mul.setOnClickListener(v -> operator_button_event("*"));
        button_div.setOnClickListener(v -> operator_button_event("/"));
        button_equal.setOnClickListener(v -> equal_button_event());
    }
    void operator_button_event(String operator) {
        if (resultTv.getText().toString().isEmpty()) {
            return;
        }

        // If an operator was already pressed, calculate the result first
        if (isOperatorPressed && !resultTv.getText().toString().isEmpty()) {
            equal_button_event();
        }

        // Store the first number
        String currentText = resultTv.getText().toString();
        if (!currentText.isEmpty()) {
            firstNumber = Double.parseDouble(currentText);
        }

        // Store the operator
        currentOperator = operator;
        isOperatorPressed = true;

        // Update the solution TextView to show the expression
        solutionTv.setText(currentText + " " + operator);

        // Clear result for next input
        resultTv.setText("");
    }
    void equal_button_event() {
        if (currentOperator.isEmpty() || resultTv.getText().toString().isEmpty()) {
            return;
        }

        double secondNumber = Double.parseDouble(resultTv.getText().toString());
        double result = 0;

        // Perform calculation based on operator
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
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    resultTv.setText("Error");
                    solutionTv.setText("");
                    currentOperator = "";
                    isOperatorPressed = false;
                    return;
                }
                break;
        }

        // Update the solution TextView with complete expression
        solutionTv.setText(firstNumber + " " + currentOperator + " " + secondNumber + " =");

        // Display result (remove decimal if it's a whole number)
        if (result == (long) result) {
            resultTv.setText(String.valueOf((long) result));
        } else {
            resultTv.setText(String.valueOf(result));
        }

        // Reset state
        firstNumber = result;
        currentOperator = "";
        isOperatorPressed = false;
    }

    @SuppressLint("SetTextI18n")
    void appendNumber(String number) {
        String currentText = resultTv.getText().toString();
        if (currentText.equals("0")) {
            resultTv.setText(number);

        } else {
            resultTv.setText(currentText + number);
        }
    }


    void clear() {
        solutionTv.setText("");
        resultTv.setText("0");
        firstNumber = 0;
        currentOperator = "";
        isOperatorPressed = false;
    }
}