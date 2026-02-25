package com.example.superapp.feature.calculator.presentation;

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
import com.example.superapp.feature.calculator.logic.CalculatorEngine;

/**
 * Calculator Mini-App.
 *
 * All computation is delegated to {@link CalculatorEngine} (pure Java, no Android deps).
 * This keeps the Activity thin and allows the engine to be unit-tested on the JVM.
 * Uses Neumorphic design styling from :core-ui.
 */
public class CalculatorActivity extends BaseActivity {

    private TextView resultTv, solutionTv;
    private final CalculatorEngine engine = new CalculatorEngine();

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
        button_clear.setOnClickListener(v -> applyState(engine.clear()));
        button_delete.setOnClickListener(v -> applyState(engine.pressDelete()));

        // Decimal point
        button_dot.setOnClickListener(v -> applyState(engine.appendDot()));

        // Number buttons
        button_0.setOnClickListener(v -> applyState(engine.appendDigit("0")));
        button_1.setOnClickListener(v -> applyState(engine.appendDigit("1")));
        button_2.setOnClickListener(v -> applyState(engine.appendDigit("2")));
        button_3.setOnClickListener(v -> applyState(engine.appendDigit("3")));
        button_4.setOnClickListener(v -> applyState(engine.appendDigit("4")));
        button_5.setOnClickListener(v -> applyState(engine.appendDigit("5")));
        button_6.setOnClickListener(v -> applyState(engine.appendDigit("6")));
        button_7.setOnClickListener(v -> applyState(engine.appendDigit("7")));
        button_8.setOnClickListener(v -> applyState(engine.appendDigit("8")));
        button_9.setOnClickListener(v -> applyState(engine.appendDigit("9")));

        // Operator buttons
        button_add.setOnClickListener(v -> applyState(engine.pressOperator("+")));
        button_sub.setOnClickListener(v -> applyState(engine.pressOperator("-")));
        button_mul.setOnClickListener(v -> applyState(engine.pressOperator("*")));
        button_div.setOnClickListener(v -> applyState(engine.pressOperator("/")));

        // Equals
        button_equal.setOnClickListener(v -> {
            CalculatorEngine.State state = engine.pressEquals();
            applyState(state);
            if (state.isError) {
                Toast.makeText(this, R.string.divide_by_zero, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Apply engine state to the UI views.
     *
     * When the engine clears the display (operator just pressed, awaiting next digit),
     * show "0" as a placeholder rather than an empty string.
     */
    private void applyState(CalculatorEngine.State state) {
        solutionTv.setText(state.history);
        String display = state.display.isEmpty() ? "0" : state.display;
        resultTv.setText(display);
    }
}
