package dev.feriixu.calculator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;

import static dev.feriixu.calculator.MainCalcData.MathOperator;

public class MainController extends AppCompatActivity implements View.OnClickListener {
    MainCalcData data;
    MainView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.data = new MainCalcData(this);
        this.view = new MainView(this);
    }

    @Override
    public void onClick(View v) {
        Log.println(Log.DEBUG, "MainActivity", "onClick fired");
        Log.d("onClick", "Button: " + getResources().getResourceName(v.getId()));
        switch (v.getId()) {
            // Commands
            case R.id.buttonEquals:
                BigDecimal result = data.Calculate();
                view.SetResult(result.toString());
                break;
            case R.id.buttonDot:
                data.AppendDot();
                break;
            case R.id.buttonNegate:
                data.NegateNumber();
                break;
            case R.id.buttonClear:
                data.ClearInput();
                break;
            case R.id.buttonBack:
                data.Back();
                break;

            // Digits
            case R.id.buttonNum0:
                data.AppendDigit(0);
                break;
            case R.id.buttonNum1:
                data.AppendDigit(1);
                break;
            case R.id.buttonNum2:
                data.AppendDigit(2);
                break;
            case R.id.buttonNum3:
                data.AppendDigit(3);
                break;
            case R.id.buttonNum4:
                data.AppendDigit(4);
                break;
            case R.id.buttonNum5:
                data.AppendDigit(5);
                break;
            case R.id.buttonNum6:
                data.AppendDigit(6);
                break;
            case R.id.buttonNum7:
                data.AppendDigit(7);
                break;
            case R.id.buttonNum8:
                data.AppendDigit(8);
                break;
            case R.id.buttonNum9:
                data.AppendDigit(9);
                break;

            // Operators
            case R.id.buttonAdd:
                data.AppendOperator(MathOperator.ADD);
                break;
            case R.id.buttonSubtract:
                data.AppendOperator(MathOperator.SUBTRACT);
                break;
            case R.id.buttonDivide:
                data.AppendOperator(MathOperator.DIVIDE);
                break;
            case R.id.buttonMultiply:
                data.AppendOperator(MathOperator.MULTIPLY);
                break;
            case R.id.buttonPow:
                data.AppendOperator(MathOperator.POW);
                break;
        }

        // Update View
        view.SetInput(data.GetInputString());
        BigDecimal result = data.Calculate();
        if (result != null) {
            view.SetResult(result.toString());
        } else {
            view.SetResult("0");
        }
    }
}