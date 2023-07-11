package com.example.calculatorapp;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;


public class CalculatorApp extends Application {

    // Constants
    private static final String CLEAR = "C";
    private static final String EQUALS = "=";
    private static final String PERCENT = "%";
    private static final String REMOVE = "←";
    private static final String INVERSE = "⅟";
    private static final String SQUARE_ROOT = "√";
    private static final String CSS_PATH = "/stylesheet.css";
    private static final String APP_NAME = "Calculator App";
    private static final String LAYOUT_STYLE_CLASS = "layout";
    private static final String SCREEN_STYLE_CLASS = "screen";
    private static final String OPERAND_STYLE_CLASS = "operand";
    private static final String CLEAR_BUTTON_ID = "clear";
    private static final String EQUALS_BUTTON_ID = "equal";
    private final String[][] template = {
            {"%", "⅟", "√", "←"},
            {"C", "(", ")", "/"},
            {"7", "8", "9", "*"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"±", "0", ",", "="}
    };

    // Helping constructs
    private final Map<String, Button> accelerators = new HashMap<>();
    private final DoubleEvaluator evaluator = new DoubleEvaluator();
    private final StringProperty expression = new SimpleStringProperty("");
    private final StringProperty stackExpression = new SimpleStringProperty("");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        final TextField inputScreen = createInputScreen();
        final TextField resultScreen = createResultScreen();
        final TilePane buttons = createButtons();

        VBox layout = createLayout(inputScreen, resultScreen, buttons);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add(CSS_PATH);

        primaryStage.setTitle(APP_NAME);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLayout(TextField inputScreen, TextField resultScreen, TilePane buttons) {
        final VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add(LAYOUT_STYLE_CLASS);
        layout.getChildren().setAll(inputScreen, resultScreen, buttons);
        handleAccelerators(layout);
        inputScreen.prefWidthProperty().bind(buttons.widthProperty());
        resultScreen.prefWidthProperty().bind(buttons.widthProperty());
        return layout;
    }

    private void handleAccelerators(VBox layout) {
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

    private TextField createInputScreen() {
        final TextField screen = new TextField();
        screen.getStyleClass().add(SCREEN_STYLE_CLASS);
        screen.setAlignment(Pos.BASELINE_RIGHT);
        screen.setEditable(false);
        screen.textProperty().bind(Bindings.format("%s", stackExpression));
        return screen;
    }

    private TextField createResultScreen() {
        final TextField screen = new TextField();
        screen.getStyleClass().add(SCREEN_STYLE_CLASS);
        screen.setAlignment(Pos.BASELINE_RIGHT);
        screen.setEditable(false);
        screen.textProperty().bind(Bindings.format("%s", expression));
        return screen;
    }

    private TilePane createButtons() {
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

    private Button createButton(String s) {
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

    private Button makeStandardButton(String s) {
        Button button = new Button(s);
        if (!s.matches("[0-9,±]")) {
            button.getStyleClass().add(OPERAND_STYLE_CLASS);
        }
        accelerators.put(s, button);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return button;
    }

    private void makeClearButton(Button button) {
        button.setId(CLEAR_BUTTON_ID);
        button.setOnAction(actionEvent -> {
            stackExpression.set("");
            expression.set("");
        });
    }

    private void makeEqualsButton(Button button) {
        button.setId(EQUALS_BUTTON_ID);
        button.setOnAction(actionEvent -> saveExpression());
    }

    private void makeRemoveChar(Button button) {
        button.setOnAction(actionEvent -> removeChar());
    }

    private void makePercentButton(Button button) {
        button.setOnAction(actionEvent -> percent());
    }

    private void makeSquareRootButton(Button button) {
        button.setOnAction(actionEvent -> squareRoot());
    }

    private void makeInverseButton(Button button) {
        button.setOnAction(actionEvent -> inverse());
    }

    private void evaluateExpression() {
        try {
            double result = evaluator.evaluate(stackExpression.get());
            expression.set(new BigDecimal(result)
                            .setScale(13, RoundingMode.HALF_UP)
                            .stripTrailingZeros()
                            .toPlainString());
        } catch (IllegalArgumentException e) {
            expression.set("");
        }
    }

    private void saveExpression() {
        if (expression.get().equals("")) {
            showError("The expression can't be executed because it has errors in it");
        } else {
            stackExpression.set(expression.get());
            expression.set("");
        }
    }

    private void removeChar() {
        try {
            String stackValue = stackExpression.getValue();
            stackExpression.set(stackExpression.get().substring(0, stackValue.length() - 1));
            evaluateExpression();
        } catch (StringIndexOutOfBoundsException ignored) {
            showError("There are no characters in the expression!");
        }
    }

    private void percent() {
        stackExpression.set("(" + stackExpression.get() + ")/100");
        evaluateExpression();
    }

    private void squareRoot() {
        stackExpression.set("(" + stackExpression.get() + ")^0.5");
        evaluateExpression();
    }

    private void inverse() {
        if (stackExpression.get().equals("")) {
            showError("You can't inverse the empty expression");
        } else {
            stackExpression.set("1/(" + stackExpression.get() + ")");
            evaluateExpression();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}