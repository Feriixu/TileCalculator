package dev.feriixu.calculator;

import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;

import java.math.BigDecimal;

public class MainView {
    Activity activity;
    View.OnClickListener clickListener;

    Button btnAdd;
    Button btnSub;
    Button btnDiv;
    Button btnMul;
    Button btnDot;
    Button btnClr;
    Button btnBck;
    Button btnNeg;
    Button btnEqu;
    Button btnPow;
    Button[] numberButtons = new Button[10];

    TextView textResult;
    TextView textInputs;

    public MainView(Activity activity) {
        this.activity = activity;
        this.clickListener = (View.OnClickListener) activity;
        textResult = this.activity.findViewById(R.id.textResult);
        textInputs = this.activity.findViewById(R.id.textInputs);

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textResult, 2, 50, 1, TypedValue.COMPLEX_UNIT_DIP);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textInputs, 2, 50, 1, TypedValue.COMPLEX_UNIT_DIP);

        btnEqu = this.activity.findViewById(R.id.buttonEquals);
        btnEqu.setOnClickListener(this.clickListener);
        btnAdd = this.activity.findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(this.clickListener);
        btnSub = this.activity.findViewById(R.id.buttonSubtract);
        btnSub.setOnClickListener(this.clickListener);
        btnDiv = this.activity.findViewById(R.id.buttonDivide);
        btnDiv.setOnClickListener(this.clickListener);
        btnMul = this.activity.findViewById(R.id.buttonMultiply);
        btnMul.setOnClickListener(this.clickListener);
        btnClr = this.activity.findViewById(R.id.buttonClear);
        btnClr.setOnClickListener(this.clickListener);
        btnBck = this.activity.findViewById(R.id.buttonBack);
        btnBck.setOnClickListener(this.clickListener);
        btnNeg = this.activity.findViewById(R.id.buttonNegate);
        btnNeg.setOnClickListener(this.clickListener);
        btnDot = this.activity.findViewById(R.id.buttonDot);
        btnDot.setOnClickListener(this.clickListener);
        btnPow = this.activity.findViewById(R.id.buttonPow);
        btnPow.setOnClickListener(this.clickListener);

        for (int i = 0; i <= 9; i++) {
            numberButtons[i] = this.activity.findViewById(this.activity.getResources().getIdentifier("buttonNum" + i, "id", this.activity.getPackageName()));
            numberButtons[i].setOnClickListener(this.clickListener);
        }
    }

    public void SetResult(String text) {
        textResult.setText(text);
    }

    public void SetInput(String text) {
        textInputs.setText(text);
    }
}
