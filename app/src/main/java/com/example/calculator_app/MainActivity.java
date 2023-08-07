package com.example.calculator_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText displayTextView;
    private StringBuilder currentInput;
    private double firstOperand;
    private char operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayTextView = findViewById(R.id.displayTextView);
        currentInput = new StringBuilder();
        operator = '\0';

        setNumberButtonListeners();
        setOperatorButtonListeners();
        setEqualButtonListener();

        Button clearButton = findViewById(R.id.buttonClear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCalculator();
            }
        });
        
        Button decimalButton = findViewById(R.id.buttonDecimal);
        decimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentInput.toString().contains(".")) {
                    // Add decimal point only if not already present
                    currentInput.append(".");
                    updateDisplay();
                }
            }
        });
    }

    private void setNumberButtonListeners() {
        int[] numberButtonIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9
        };

        for (int numberButtonId : numberButtonIds) {
            Button numberButton = findViewById(numberButtonId);
            numberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String buttonText = ((Button) v).getText().toString();
                    currentInput.append(buttonText);
                    updateDisplay();
                }
            });
        }
    }

    private void setOperatorButtonListeners() {
        int[] operatorButtonIds = {
                R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide
        };

        for (int operatorButtonId : operatorButtonIds) {
            Button operatorButton = findViewById(operatorButtonId);
            operatorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentInput.length() > 0) {
                        firstOperand = Double.parseDouble(currentInput.toString());
                        operator = ((Button) v).getText().toString().charAt(0);
                        currentInput.setLength(0); // Clear the input
                        updateDisplay();
                    }
                }
            });
        }
    }
    
    

    private void setEqualButtonListener() {
        Button equalButton = findViewById(R.id.buttonEqual);
        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator != '\0' && currentInput.length() > 0) {
                    double secondOperand = Double.parseDouble(currentInput.toString());
                    double result = performOperation(firstOperand, secondOperand, operator);
                    if (Double.isFinite(result)) {
                        currentInput.setLength(0);
                        currentInput.append(formatResult(result));
                        operator = '\0';
                        updateDisplay();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid operation", Toast.LENGTH_SHORT).show();
                        clearCalculator();
                    }
                }
            }
        });
    }

    private double performOperation(double operand1, double operand2, char operator) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 != 0) {
                    return operand1 / operand2;
                } else {
                    return Double.NaN; // Division by zero
                }
            default:
                return 0;
        }
    }

    private void updateDisplay() {
        displayTextView.setText(currentInput.toString());
    }

    private String formatResult(double result) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##########");
        return decimalFormat.format(result);
    }

    private void clearCalculator() {
        currentInput.setLength(0);
        operator = '\0';
        updateDisplay();
    }
}