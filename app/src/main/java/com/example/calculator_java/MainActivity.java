package com.example.calculator_java;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultText;
    TextView inputText;
    boolean newOperation = true;
    boolean decimal = false;
    boolean neg = false;
    String number = "0";
    int zeroCount = 0;
    Double val;
    double result;
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

        number = savedInstanceState.getString("number");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putString("number", number);
    }

    //onClick function
    //parameters is the view
    //doesn't return anything
    //is called on all numerical buttons and operations (does not include equal sign)
    //shows each of the numbers and operations that is clicked on the calculator screen as input or output
    @Override
    public void onClick(View view) {
        //clears the initial 0 on the on start of the screen
        //checks for if the click is a new operation
        if(newOperation){
            resultText.setText("0");
            zeroCount++;
        }
        else{
            number = resultText.getText().toString();
        }

        newOperation = false;

        try {
            switch(view.getId()){
                case R.id.btn0:
                    clearZero(view);
                    number += "0";
                    break;
                case R.id.btn1:
                    clearZero(view);
                    number += "1";
                    break;
                case R.id.btn2:
                    clearZero(view);
                    number += "2";
                    break;
                case R.id.btn3:
                    clearZero(view);
                    number += "3";
                    break;
                case R.id.btn4:
                    clearZero(view);
                    number += "4";
                    break;
                case R.id.btn5:
                    clearZero(view);
                    number += "5";
                    break;
                case R.id.btn6:
                    clearZero(view);
                    number += "6";
                    break;
                case R.id.btn7:
                    clearZero(view);
                    number += "7";
                    break;
                case R.id.btn8:
                    clearZero(view);
                    number += "8";
                    break;
                case R.id.btn9:
                    clearZero(view);
                    number += "9";
                    break;
                case R.id.btnNegative:
                    negate(view);
                    break;
                case R.id.btnDecimal:
                    setDecimal(view);
                    break;
                case R.id.btnClear:
                    number = "0";
                    newOperation = true;
                    neg = false;
                    zeroCount = 0;
                    break;
                case R.id.btnPlus:
                    number += "+";
                    decimal = false;
                    break;
                case R.id.btnMinus:
                    number += "-";
                    decimal = false;
                    break;
                case R.id.btnMulti:
                    number += "*";
                    decimal = false;
                    break;
                case R.id.btnDivide:
                    number += "/";
                    decimal = false;
                    break;
                case R.id.btnBack:
                    backSpace(view);
                    break;
            }
            resultText.setText(number);
        }catch (Exception e){
            resultText.setText(error);
            e.printStackTrace();
        }

    }
    public void equalsClick(View view) {
        try {
            String newVal = resultText.getText().toString();
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            result = (double) engine.eval(newVal);

            number = trailingValues(Double.toString(result));

            //check for negative
            if(neg = false){
                number = resultText.getText().toString();
            }
            if(Double.parseDouble(number) < 0) neg = true;

            if(number.contains(".")) decimal = true;

            newOperation = true;
        }catch (Exception e){
            resultText.setText(error);
            e.printStackTrace();
        }

    }
    //backspace method
    public void backSpace(View view){
        if(number.equals("")){
            number = "";
        }
        else{
            if(number.charAt(number.length() -1) == '.'){
                decimal = false;
            }
            number = (String) number.subSequence(0,number.length()-1);
        }
    }
    public void setDecimal(View view){
        if(!decimal){
            if(number.equals("") || number.contains("00")){
                number = "0.";
            } else {
                number += ".";
            }
            decimal = true;
        }
    }
    public void negate(View view){
        if(!neg){
            number = "-"+number;
            val = Double.valueOf(number);
            neg = true;

        }
        else{
            val = Double.valueOf(number);
            number = String.valueOf(Math.abs(val));
            number = trailingValues(number);
            neg = false;
        }
    }
    public String trailingValues(String value){
        //trailing values
        BigDecimal bd = new BigDecimal(value);
        MathContext m = new MathContext(4);
        BigDecimal f = bd.round(m);


        if(value.contains(".0") && value.endsWith("0")){
            double finalResult = Double.parseDouble(value);
            int finalInt = (int) finalResult;
            resultText.setText(String.valueOf(finalInt));
            return String.valueOf(finalInt);
        }
        else {
            resultText.setText(f.toString());
            return f.toString();
        }
    }
    public void clearZero(View view){
        if(number.equals("0") || number.equals("-0")){
            number = "";
        }
    }
}