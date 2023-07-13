package com.example.calculatorapp;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

import static com.example.calculatorapp.Constants.APP_NAME;
import static com.example.calculatorapp.Constants.CSS_PATH;
import static com.example.calculatorapp.UIBuilder.*;


public class CalculatorApp extends Application {

    // Helping constructs
    public static final Map<String, Button> accelerators = new HashMap<>();
    public static final DoubleEvaluator evaluator = new DoubleEvaluator();
    public static final StringProperty expression = new SimpleStringProperty("");
    public static final StringProperty stackExpression = new SimpleStringProperty("");
    
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
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}