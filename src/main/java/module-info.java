module com.example.calculatorapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javaluator;


    opens com.example.calculatorapp to javafx.fxml;
    exports com.example.calculatorapp;
}