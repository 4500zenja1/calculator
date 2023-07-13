package com.example.calculatorapp;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.example.calculatorapp.CalculatorApp.*;
import static com.example.calculatorapp.ErrorHandler.showError;

public class Logic {
    public static void evaluateExpression() {
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

    public static void saveExpression() {
        if (expression.get().equals("")) {
            showError("The expression can't be executed because it has errors in it");
        } else {
            stackExpression.set(expression.get());
        }
    }

    public static void removeChar() {
        try {
            String stackValue = stackExpression.getValue();
            stackExpression.set(stackExpression.get().substring(0, stackValue.length() - 1));
            evaluateExpression();
        } catch (StringIndexOutOfBoundsException ignored) {
            showError("There are no characters in the expression!");
        }
    }

    public static void percent() {
        stackExpression.set("(" + stackExpression.get() + ")/100");
        evaluateExpression();
    }

    public static void squareRoot() {
        stackExpression.set("(" + stackExpression.get() + ")^0.5");
        evaluateExpression();
    }

    public static void inverse() {
        if (stackExpression.get().equals("")) {
            showError("You can't inverse the empty expression");
        } else {
            stackExpression.set("1/(" + stackExpression.get() + ")");
            evaluateExpression();
        }
    }

    public static void signChange() {
        stackExpression.set("-(" + stackExpression.get() + ")");
        evaluateExpression();
    }
}
