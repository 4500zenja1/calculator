package com.example.calculatorapp;

public class Constants {
    public static final String CLEAR = "C";
    public static final String EQUALS = "=";
    public static final String PERCENT = "%";
    public static final String REMOVE = "←";
    public static final String INVERSE = "⅟";
    public static final String SQUARE_ROOT = "√";
    public static final String SIGN_CHANGE = "±";
    public static final String CSS_PATH = "/stylesheet.css";
    public static final String APP_NAME = "Calculator App";
    public static final String LAYOUT_STYLE_CLASS = "layout";
    public static final String SCREEN_STYLE_CLASS = "screen";
    public static final String OPERAND_STYLE_CLASS = "operand";
    public static final String CLEAR_BUTTON_ID = "clear";
    public static final String EQUALS_BUTTON_ID = "equal";
    public static final String[][] template = {
            {"%", "⅟", "√", "←"},
            {"C", "^", ")", "/"},
            {"7", "8", "9", "*"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"±", "0", ",", "="}
    };
}
