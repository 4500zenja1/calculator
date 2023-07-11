package com.example.calculatorapp;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import static com.example.calculatorapp.CalculatorApp.*;
import static com.example.calculatorapp.Constants.*;
import static com.example.calculatorapp.Logic.*;
import static com.example.calculatorapp.Makers.*;

public class UIBuilder {
    public static VBox createLayout(TextField inputScreen, TextField resultScreen, TilePane buttons) {
        final VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add(LAYOUT_STYLE_CLASS);
        layout.getChildren().setAll(inputScreen, resultScreen, buttons);
        handleAccelerators(layout);
        inputScreen.prefWidthProperty().bind(buttons.widthProperty());
        resultScreen.prefWidthProperty().bind(buttons.widthProperty());
        return layout;
    }

    public static void handleAccelerators(VBox layout) {
        layout.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();
            if (keyCode == KeyCode.ENTER) {
                saveExpression();
            } else if (keyCode == KeyCode.BACK_SPACE) {
                removeChar();
            } else if (keyCode == KeyCode.DIGIT5 && keyEvent.isShiftDown()) {
                percent();
            } else {
                String keyText = keyEvent.getText();
                if (keyEvent.isShiftDown()) {
                    keyText = keyCode == KeyCode.DIGIT9 ? "(" : keyCode == KeyCode.DIGIT0 ? ")" : "";
                }
                Button activated = accelerators.get(keyText);
                if (activated != null) {
                    activated.fire();
                }
            }
        });
    }

    public static TextField createInputScreen() {
        final TextField screen = new TextField();
        screen.getStyleClass().add(SCREEN_STYLE_CLASS);
        screen.setAlignment(Pos.BASELINE_RIGHT);
        screen.setEditable(false);
        screen.textProperty().bind(Bindings.format("%s", stackExpression));
        return screen;
    }

    public static TextField createResultScreen() {
        final TextField screen = new TextField();
        screen.getStyleClass().add(SCREEN_STYLE_CLASS);
        screen.setAlignment(Pos.BASELINE_RIGHT);
        screen.setEditable(false);
        screen.textProperty().bind(Bindings.format("%s", expression));
        return screen;
    }

    public static TilePane createButtons() {
        final TilePane buttons = new TilePane();
        buttons.setVgap(7);
        buttons.setHgap(7);
        buttons.setPrefColumns(template[0].length);
        for (String[] row : template) {
            for (String s : row) {
                buttons.getChildren().add(createButton(s));
            }
        }
        return buttons;
    }

    public static Button createButton(String s) {
        Button button = makeStandardButton(s);

        if (CLEAR.equals(s)) {
            makeClearButton(button);
        } else if (EQUALS.equals(s)) {
            makeEqualsButton(button);
        } else if (REMOVE.equals(s)) {
            makeRemoveChar(button);
        } else if (PERCENT.equals(s)) {
            makePercentButton(button);
        } else if (SQUARE_ROOT.equals(s)) {
            makeSquareRootButton(button);
        } else if (INVERSE.equals(s)) {
            makeInverseButton(button);
        } else {
            button.setOnAction(actionEvent -> {
                stackExpression.set(stackExpression.get() + s);
                try {
                    evaluateExpression();
                } catch (IllegalArgumentException e) {
                    expression.set("");
                }
            });
        }

        return button;
    }
}
