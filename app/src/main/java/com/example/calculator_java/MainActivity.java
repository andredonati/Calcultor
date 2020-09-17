package com.example.calculator_java;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //access to the TextView
    TextView resultText;
    
    //booleans to check state of new operation, decimal places, 
    // and if value is negative
    boolean newOperation = true;
    boolean decimal = false;
    boolean neg = false;
    
    //String of the expression 
    String expression = "0";

    //expression in double form for parsing issues
    Double val;
    double result;

    //error message
    String error = "Error";

    @Override
    @RequiresApi(28)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultText = findViewById(R.id.resultVal);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        expression = savedInstanceState.getString("expression");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putString("expression", expression);
    }

    //onClick function
    //parameters is the view
    //doesn't return anything
    //is called on all numerical buttons and operations (does not include equal sign)
    //shows each of the expressions and operations that is clicked on the calculator screen as input or output
    @Override
    public void onClick(View view) {
        //clears the initial 0 on the on start of the screen
        //checks for if the click is a new operation
        if(newOperation){
            resultText.setText("0");
        }
        else{
            expression = resultText.getText().toString();
        }

        newOperation = false;

        //switch statement for each of the buttons being clicked
        //gets each of the buttons values by id
        try {
            switch(view.getId()){
                case R.id.btn0:
                    clearZero();
                    expression += "0";
                    break;
                case R.id.btn1:
                    clearZero();
                    expression += "1";
                    break;
                case R.id.btn2:
                    clearZero();
                    expression += "2";
                    break;
                case R.id.btn3:
                    clearZero();
                    expression += "3";
                    break;
                case R.id.btn4:
                    clearZero();
                    expression += "4";
                    break;
                case R.id.btn5:
                    clearZero();
                    expression += "5";
                    break;
                case R.id.btn6:
                    clearZero();
                    expression += "6";
                    break;
                case R.id.btn7:
                    clearZero();
                    expression += "7";
                    break;
                case R.id.btn8:
                    clearZero();
                    expression += "8";
                    break;
                case R.id.btn9:
                    clearZero();
                    expression += "9";
                    break;
                case R.id.btnNegative:
                    negate();
                    break;
                case R.id.btnDecimal:
                    setDecimal();
                    break;
                case R.id.btnClear:
                    expression = "0";
                    newOperation = true;
                    neg = false;
                    decimal  = false;
                    break;
                case R.id.btnPlus:
                    expression += "+";
                    break;
                case R.id.btnMinus:
                    expression += "-";
                    break;
                case R.id.btnMulti:
                    expression += "*";
                    break;
                case R.id.btnDivide:
                    expression += "/";
                    break;
                case R.id.btnBack:
                    backSpace();
                    break;
            }

            resultText.setText(expression);

        }catch (Exception e){
            resultText.setText(error);
            e.printStackTrace();
        }
    }

    //is called when the user clicks on the equals sign
    //takes in the view
    //sets the final value of the expression to the String
    //uses the ScriptEngineManager class so evaluate the expression
    //that value is then passed into the trailingValues function to set the precision
    //checks to make sure if value is negative and decimal points
    //sets newOperation to true
    public void equalsClick(View view) {
        try {
            String newVal = resultText.getText().toString();
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            result = (double) engine.eval(newVal);

            expression = trailingValues(Double.toString(result));

            //check for negative
            if(neg = false){
                expression = resultText.getText().toString();
            }
            if(Double.parseDouble(expression) < 0) neg = true;

           if(expression.contains(".")) decimal = true;

            newOperation = true;
        }catch (Exception e){
            resultText.setText(error);
            e.printStackTrace();
        }
    }

    //backSpace is the method for deleting values in the expression
    //if the expression is not null then it will make a
    //substring with a length less than it is
    //called when backSpace button is clicked
    public void backSpace(){
        if(expression.equals("")){
            expression = "";
        }
        else{
            if(expression.charAt(expression.length() -1) == '.'){
                decimal = false;
            }
            expression = (String) expression.subSequence(0,expression.length()-1);
        }
    }

    //setsDecimal places the decimal
    //checks for null string
    //sets decimal to true when the decimal place is added
    public void setDecimal(){
        if(!decimal){
            if(expression.equals("")){
                expression = "0.";
            } else {
                expression += ".";
            }
            decimal = true;
        }
        else{
            if(expression.contains("+") || expression.contains("/") || expression.contains("*") || expression.contains("-")){
                expression += ".";
            }
        }
    }

    //negates the value of the value
    //checks to see if value is already negative
    public void negate(){
        //if it is not negative then it will add the negative sign
        // and set negative to true
        if(!neg){
            expression = "-"+expression;
            val = Double.valueOf(expression);
            neg = true;

        }
        //if it is negative then it passes the value
        // through trailingValues to set precision
        else{
            val = Double.valueOf(expression);
            expression = String.valueOf(Math.abs(val));
            expression = trailingValues(expression);
            neg = false;
        }
    }

    //trailingValues passes in the String expression
    //returns a String with the value of expression
    //chops off extra zero's in the answer
    //sets precision of the answer to 4
    //called in different functions
    public String trailingValues(String value){
        //use BigDecimal to round the value off to 4 precision
        BigDecimal bd = new BigDecimal(value);
        MathContext m = new MathContext(4);
        BigDecimal f = bd.round(m);

        //checks to see if there are extra zero's in the expression
        if(value.contains(".0") && value.endsWith("0")){
            double finalResult = Double.parseDouble(value);
            int finalInt = (int) finalResult;
            resultText.setText(String.valueOf(finalInt));
            return String.valueOf(finalInt);
        }
        //if there is no extra zero's then setText and return that value
        else {
            resultText.setText(String.valueOf(f));
            return f.toString();
        }
    }

    //clearZero clears the zero when entering a new number after clearing
    //changes values from "05" to "5"
    //called when each number button is pressed
    public void clearZero(){
        if(expression.equals("0") || expression.equals("-0")){
            expression = "";
        }
    }
}