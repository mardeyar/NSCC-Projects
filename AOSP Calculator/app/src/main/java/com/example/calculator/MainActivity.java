/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import com.google.android.material.button.MaterialButton;

// TODO: Optimize app for larger display compatibility
// TODO: Change button colors to something more visually appealing

/**
* MainActivity acts as the main interface for the calculator app
* It will handle all the user interactions and mathematics operations
*/
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultView, solutionView;
    LinearLayout buttonContainer;

    /**
     * Entry point to initialize the MainActivity and set up UI components
     * @param savedInstanceState Restores the activities previous state after UI orientation or
     * any config changes
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultView = findViewById(R.id.result_view);
        solutionView = findViewById(R.id.solution_view);
        buttonContainer = findViewById(R.id.buttons_container);

        chooseTheme();
        assignButtons();
    }

    /**
     * Assigns all the buttons on the calculator to the ones created in activity_main.xml
     */
    void assignButtons() {
        assignButtonID(R.id.btnClear);
        assignButtonID(R.id.btnDelete);
        assignButtonID(R.id.btnDecimal);
        assignButtonID(R.id.btnDivide);
        assignButtonID(R.id.btnMultiply);
        assignButtonID(R.id.btnMinus);
        assignButtonID(R.id.btnAdd);
        assignButtonID(R.id.btnEqual);
        assignButtonID(R.id.btn0);
        assignButtonID(R.id.btn1);
        assignButtonID(R.id.btn2);
        assignButtonID(R.id.btn3);
        assignButtonID(R.id.btn4);
        assignButtonID(R.id.btn5);
        assignButtonID(R.id.btn6);
        assignButtonID(R.id.btn7);
        assignButtonID(R.id.btn8);
        assignButtonID(R.id.btn9);
    }

    /**
     * Assigns a specific ID to each MaterialButton and sets an OnClickListener event
     * @param id The MaterialButton's specific ID #
     */
    void assignButtonID(int id) {
        MaterialButton button = findViewById(id);
        button.setOnClickListener(this);
    }

    /**
     * Get the value of current display theme, dark or light, then set the textViews and container
     * based on which system theme the user has set
     */
    void chooseTheme() {
        int displayTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        // TODO: getColor is deprecated; implement ContextCompat instead
        if (displayTheme == Configuration.UI_MODE_NIGHT_YES) {
            solutionView.setTextColor(getResources().getColor(R.color.dark_view_text));
            resultView.setTextColor(getResources().getColor(R.color.dark_view_text));
            buttonContainer.setBackgroundColor(getResources().getColor(R.color.container_dark));
        } else {
            solutionView.setTextColor(getResources().getColor(R.color.light_view_text));
            resultView.setTextColor(getResources().getColor(R.color.light_view_text));
            buttonContainer.setBackgroundColor(getResources().getColor(R.color.container_light));
        }
    }

    /**
     * Click event handler for all calculator buttons, performing actions based on the button
     * @param view The buttons clicked view
     * @return If conditions will set textview based on button. Ex: clear sets to 0
     */
    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String calculation = solutionView.getText().toString();

        if (buttonText.equals("Clear")) {
            solutionView.setText("");
            resultView.setText("0");
            return;
        }

        if (buttonText.equals("=")) {
            solutionView.setText(resultView.getText());
            return;
        }

        if (buttonText.equals("⌫")) {
            if (!calculation.isEmpty()) {
                calculation = calculation.substring(0, calculation.length() - 1);
                // This helps avoid "Undefined" message in combination with data.isEmpty in calculationResult
                solutionView.setText(calculation);
            } else {
                return;
            }
        } else {
            calculation = calculation + buttonText;
        }

        solutionView.setText(calculation);
        String result = calculationResult(calculation);

        // TODO: Change this to replace "NaN" with "Err"
        if (!result.equals("Error")) {
            resultView.setText(result);
        }
    }

    /**
     * Calculates the result of the operation provided by the user from button clicks
     * @param data The mathematical operation to be evaluated
     * @return The calculation result, or, "Error" if an exception is to occur ex. 0 / 0
     */
    String calculationResult(String data) {
        try {
            if (data.isEmpty()) {
                return "0"; // Will only allow numbers to display on screen in textview
            }

            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initSafeStandardObjects();
            String result = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();

            if (result.endsWith(".0")) {
                result = result.replace(".0", "");
            }
            return result;
        } catch (Exception e) {
            return "Error";
        }
    }
}