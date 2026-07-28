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

    private double num1 = 0;
    private String operator = "";
    private boolean startNew = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        String[][] labels = {
                {"%", "CE", "C", "⌫"},
                {"1/x", "x²", "√x", "÷"},
                {"7", "8", "9", "×"},
                {"4", "5", "6", "−"},
                {"1", "2", "3", "+"},
                {"±", "0", ".", "="}
        };

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(1);
        pane.setVgap(1);

        VBox displayHere = new VBox();

        displayHere.setAlignment(Pos.CENTER_RIGHT);
        displayHere.setPrefHeight(80);
        displayHere.setPrefWidth(163);

        displayHere.setStyle("-fx-background-color: #f4f4f4; -fx-padding:5; -fx-border-color: #f4f4f4;");

        Label  History  = new Label("");
        History.setFont(Font.font("Segoe UI", 11));
        History.setTextFill(Color.GRAY);

        Label resultDisplay = new Label("0");
        resultDisplay.setFont(Font.font("Segoe UI", 28));

        displayHere.getChildren().addAll(History, resultDisplay);
        pane.add(displayHere, 0,0, 4, 1);

        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                String text = labels[i][j];
                Button button = new Button(text);
                button.setPrefSize(40, 40);
                button.setFont(Font.font("Segoe UI", 10));
                button.setStyle("-fx-background-color: #FFD1DC;");
                if(labels[i][j].equals("="))
                    button.setStyle("-fx-background-color: #C2185B; -fx-text-fill: white;");

                button.setOnAction(e -> {
                    if (text.matches("[0-9]") || text.equals(".")) {
                        if (startNew){

                            resultDisplay.setText(text);
                            startNew = false;
                        } else{
                            resultDisplay.setText(resultDisplay.getText() + text);

                        }
                    }

                    else if (text.equals("+") || text.equals("−") || text.equals("×") || text.equals("÷")){
                        num1 = Double.parseDouble(resultDisplay.getText());
                        operator = text;

                        History.setText(formatResult(num1) + " " + operator);

                        startNew = true;
                    }
                    else if (text.equals("=")){
                        if (operator.isEmpty()) return;

                        double num2 = Double.parseDouble(resultDisplay.getText());

                        double result = 0;

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

                        History.setText(formatResult(num1) + " " +operator + " " + formatResult(num2) +  " =");
                        resultDisplay.setText(formatResult(result) );

                        operator = "";
                        startNew = true;
                    }
                    else if (text.equals("CE")){
                        resultDisplay.setText("0");
                        startNew = true;
                    }
                    else if (text.equals("C")){
                        num1 = 0;
                        operator = "";
                        History.setText("");
                        resultDisplay.setText("0");
                        startNew = true;
                    }
                    else if(text.equals("⌫")){
                        String current = resultDisplay.getText();
                        if (!current.isEmpty() && !current.equals("0")){
                            String sub  = current.substring(0, current.length() - 1);
                            resultDisplay.setText(sub.isEmpty() ? "0" : sub);


                        }
                    }
                    else if (text.equals("%") || text.equals("x²") || text.equals("√x") || text.equals("1/x") || text.equals("±")){
                        double val =  Double.parseDouble(resultDisplay.getText());
                        double result2 =0;

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

                        if (!text.equals("±")){
                            if (text.equals("x²")) {
                                History.setText(formatResult(val) + "² =");
                            } else if (text.equals("√x")) {
                                History.setText("√" + formatResult(val) + " =");
                            } else if (text.equals("1/x")) {
                                History.setText("1/" + formatResult(val) + " =");
                            } else if (text.equals("%")) {
                                History.setText(formatResult(val) + "% =");
                            }
                        }

                        startNew = true;
                    }
                });
                pane.add(button, j, i + 1);
            }
        }

        Scene scene = new Scene(pane);
        primaryStage.setTitle("Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String formatResult(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }
}
