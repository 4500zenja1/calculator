package com.example.calculatorapp;

import javafx.scene.control.Button;

import static com.example.calculatorapp.CalculatorApp.*;
import static com.example.calculatorapp.Constants.*;
import static com.example.calculatorapp.Logic.*;

public class Makers {
    public static Button makeStandardButton(String s) {
        Button button = new Button(s);
        if (!s.matches("[0-9,±]")) {
            button.getStyleClass().add(OPERAND_STYLE_CLASS);
        }
        accelerators.put(s, button);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return button;
    }

    public static void makeClearButton(Button button) {
        button.setId(CLEAR_BUTTON_ID);
        button.setOnAction(actionEvent -> {
            stackExpression.set("");
            expression.set("");
        });
    }

    public static void makeEqualsButton(Button button) {
        button.setId(EQUALS_BUTTON_ID);
        button.setOnAction(actionEvent -> saveExpression());
    }

    public static void makeRemoveCharButton(Button button) {
        button.setOnAction(actionEvent -> removeChar());
    }

    public static void makePercentButton(Button button) {
        button.setOnAction(actionEvent -> percent());
    }

    public static void makeSquareRootButton(Button button) {
        button.setOnAction(actionEvent -> squareRoot());
    }

    public static void makeInverseButton(Button button) {
        button.setOnAction(actionEvent -> inverse());
    }

    public static void makeSignChangeButton(Button button) {
        button.setOnAction(actionEvent -> signChange());
    }
}
