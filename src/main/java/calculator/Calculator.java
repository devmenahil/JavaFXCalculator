package calculator;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class Calculator extends Application {

    // Stores the first operand entered before an operator is pressed
    private double num1 = 0;

    // Tracks which operator (+, −, ×, ÷) was selected last
    private String operator = "";

    // Tracks whether the next digit typed should start a new number
    // (true after pressing an operator, clearing, or getting a result)
    private boolean startNew = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // 2D array defining the calculator's button layout, row by row
        String[][] labels = {
                {"%", "CE", "C", "⌫"},
                {"1/x", "x²", "√x", "÷"},
                {"7", "8", "9", "×"},
                {"4", "5", "6", "−"},
                {"1", "2", "3", "+"},
                {"±", "0", ".", "="}
        };

        // Grid layout that holds the display and all buttons
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(1);
        pane.setVgap(1);
        pane.setStyle("-fx-background-color: #EAF2FB;");

        // Container for the history label and the main result display
        VBox displayHere = new VBox();

        displayHere.setAlignment(Pos.CENTER_RIGHT);
        displayHere.setPrefHeight(80);
        displayHere.setPrefWidth(163);

        displayHere.setStyle("-fx-background-color: #FFFFFF; -fx-padding:5; -fx-border-color: #FFFFFF;");

        // Small gray label showing the expression history (e.g. "5 +")
        Label history = new Label("");
        history.setFont(Font.font("Segoe UI", 11));
        history.setTextFill(Color.web("#6B7A99"));

        // Main display showing the current number or result
        Label resultDisplay = new Label("0");
        resultDisplay.setFont(Font.font("Segoe UI", 28));
        resultDisplay.setTextFill(Color.web("#1B3A6B"));

        displayHere.getChildren().addAll(history, resultDisplay);
        pane.add(displayHere, 0,0, 4, 1);

        // Loop through the labels array to create and place each button
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                String text = labels[i][j];
                Button button = new Button(text);
                button.setPrefSize(40, 40);
                button.setFont(Font.font("Segoe UI", 10));

                // Digits get a soft light-blue style
                button.setStyle("-fx-background-color: #D6E6F9; -fx-text-fill: #1B3A6B;");

                // Operators (÷, ×, −, +) get a medium blue to stand out from digits
                if (text.equals("÷") || text.equals("×") || text.equals("−") || text.equals("+"))
                    button.setStyle("-fx-background-color: #A9CCF0; -fx-text-fill: #1B3A6B;");

                // Utility functions (%, CE, C, ⌫, 1/x, x², √x, ±) get a muted slate-blue shade
                if (text.equals("%") || text.equals("CE") || text.equals("C") || text.equals("⌫")
                        || text.equals("1/x") || text.equals("x²") || text.equals("√x") || text.equals("±"))
                    button.setStyle("-fx-background-color: #C3D9F0; -fx-text-fill: #2E4E7E;");

                // Give the "=" button a distinct highlighted style
                if(labels[i][j].equals("="))
                    button.setStyle("-fx-background-color: #1B5FBF; -fx-text-fill: white;");

                // Defines what happens when this button is clicked
                button.setOnAction(e -> {
                    // Digit or decimal point pressed: append to display, or start a new number
                    if (text.matches("[0-9]") || text.equals(".")) {
                        if (startNew){

                            resultDisplay.setText(text);
                            startNew = false;
                        } else{
                            resultDisplay.setText(resultDisplay.getText() + text);

                        }
                    }

                    // Operator pressed: store the first operand and the chosen operator
                    else if (text.equals("+") || text.equals("−") || text.equals("×") || text.equals("÷")){
                        num1 = Double.parseDouble(resultDisplay.getText());
                        operator = text;

                        history.setText(formatResult(num1) + " " + operator);

                        startNew = true;
                    }
                    // "=" pressed: perform the calculation using the stored operator
                    else if (text.equals("=")){
                        if (operator.isEmpty()) return;

                        double num2 = Double.parseDouble(resultDisplay.getText());

                        double result = 0;

                        // Apply the operation matching the stored operator
                        switch(operator){
                            case "+":
                                result = num1 + num2;
                                break;
                            case "−":
                                result = num1 - num2;
                                break;
                            case "×":
                                result = num1 * num2;
                                break;
                            case "÷":
                                if(num2 != 0) {
                                    result = num1 / num2;
                                }
                                else
                                    result = 0.0/0.0;

                                break;
                        }

                        history.setText(formatResult(num1) + " " +operator + " " + formatResult(num2) +  " =");
                        resultDisplay.setText(formatResult(result) );

                        operator = "";
                        startNew = true;
                    }
                    // "CE" pressed: clear only the current entry, keep stored operator/history
                    else if (text.equals("CE")){
                        resultDisplay.setText("0");
                        startNew = true;
                    }
                    // "C" pressed: full reset of the calculator state
                    else if (text.equals("C")){
                        num1 = 0;
                        operator = "";
                        history.setText("");
                        resultDisplay.setText("0");
                        startNew = true;
                    }
                    // Backspace pressed: remove the last character of the current entry
                    else if(text.equals("⌫")){
                        String current = resultDisplay.getText();
                        if (!current.isEmpty() && !current.equals("0")){
                            String sub  = current.substring(0, current.length() - 1);
                            resultDisplay.setText(sub.isEmpty() ? "0" : sub);


                        }
                    }
                    // Single-operand functions: %, x², √x, 1/x, ± (sign flip)
                    else if (text.equals("%") || text.equals("x²") || text.equals("√x") || text.equals("1/x") || text.equals("±")){
                        double val =  Double.parseDouble(resultDisplay.getText());
                        double result2 =0;

                        // Compute the result based on which function was pressed
                        if (text.equals("%")) {
                            result2 = val /100;
                        }else if (text.equals("x²")) {
                            result2 = val*val;
                        }else if(text.equals("√x")) {
                            result2 = Math.sqrt(val);
                        } else if (text.equals("1/x")) {
                            if(val == 0){
                                result2 = 0.0/0.0;
                            } else
                                result2 = 1/val;

                        }
                        else if (text.equals("±")) {
                            result2 =  val * -1;
                        }

                        resultDisplay.setText(formatResult(result2));

                        // Update the history label to show what operation was applied
                        // (skipped for ± since it doesn't represent a "completed" operation)
                        if (!text.equals("±")){
                            if (text.equals("x²")) {
                                history.setText(formatResult(val) + "² =");
                            } else if (text.equals("√x")) {
                                history.setText("√" + formatResult(val) + " =");
                            } else if (text.equals("1/x")) {
                                history.setText("1/" + formatResult(val) + " =");
                            } else if (text.equals("%")) {
                                history.setText(formatResult(val) + "% =");
                            }
                        }

                        startNew = true;
                    }
                });
                pane.add(button, j, i + 1);
            }
        }

        // Assemble the scene and show the window
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Formats a double for display: shows whole numbers without a decimal point
    private String formatResult(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }
}