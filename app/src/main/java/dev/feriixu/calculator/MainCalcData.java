package dev.feriixu.calculator;

import android.app.Activity;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import dev.feriixu.calculator.Math.BigDecimalMath;

public class MainCalcData {
    private ArrayList<Object> inputs = new ArrayList<>();
    private Activity activity;
    private boolean decimalActive = false;
    private int curDecimalPlaces = 0;

    public MainCalcData(Activity activity) {
        this.activity = activity;
    }

    private Object GetLastInput() {
        Object last = null; // Null so we have 'a' value
        if (this.inputs.size() > 0) { // If the inputs list has elements
            last = this.inputs.get(this.inputs.size() - 1); // Get the last element
        }

        return last;
    }

    public void AppendDot() {
        Log.println(Log.DEBUG, "CalcData", "AppendDot entered");
        Object last = GetLastInput();

        if (last instanceof BigDecimal && !decimalActive) {
            decimalActive = true;
            curDecimalPlaces++;
        }
    }

    public void AppendDigit(int digit) {
        Log.d("CalcData", "AppendDigit entered: " + digit);
        Object last = GetLastInput();

        if (last instanceof BigDecimal) { // If the last input is a number
            Log.d("AppendDigit", "Last input is a number");
            BigDecimal lastNum = (BigDecimal) last;
            // Multiply by 10 if we are not inputting decimal points, then add the digit or the negative digit
            // Increase decimals if we input a decimal
            if (!decimalActive) {
                Log.d("AppendDigit", "Decimals are not active");
                lastNum = lastNum.multiply(BigDecimal.TEN).add((lastNum.compareTo(BigDecimal.ZERO) >= 0) ? BigDecimal.valueOf(digit) : BigDecimal.valueOf(-digit));
            } else {
                Log.d("AppendDigit", "Decimals are active");
                lastNum = lastNum.add((lastNum.compareTo(BigDecimal.ZERO) >= 0) ? BigDecimal.valueOf(digit).movePointLeft(curDecimalPlaces++) : BigDecimal.valueOf(-digit).movePointLeft(curDecimalPlaces++));
            }

            this.inputs.set(this.inputs.size() - 1, lastNum); // Overwrite the last element
            Log.d("AppendDigit", "Replaced last number with new one");
        } else { // If the last element is an operator
            this.inputs.add(BigDecimal.valueOf(digit)); // Add the number to the end of the list
            Log.d("AppendDigit", "Appended digit");
        }

        Log.println(Log.DEBUG, "AppendDigit", "Input length is now: " + inputs.size());
    }

    public void AppendOperator(MathOperator o) {
        Log.d("CalcData", "AppendOperator entered");
        Object last = GetLastInput();
        decimalActive = false;
        curDecimalPlaces = 0;
        if (last instanceof MathOperator) { // If the last input is an operator
            this.inputs.set(this.inputs.size() - 1, o); // Overwrite the last operator
            Log.d("AppendOperator", "Replaced last operator");
        } else if (last instanceof BigDecimal) { // If its a number
            this.inputs.add(o); // Just add the operator to the list
            Log.d("AppendOperator", "Appended operator to inputs");
        }
    }

    public void NegateNumber() {
        Log.println(Log.DEBUG, "CalcData", "NegateNumber entered");
        Object last = GetLastInput();

        if (last instanceof BigDecimal) { // If the last element is a number
            this.inputs.set(this.inputs.size() - 1, ((BigDecimal) last).negate()); // Negate the last element and overwrite
            Log.d("NegateNumber", "Negated last number");
        } else {
            Log.d("NegateNumber", "Last input was NaN");
        }
    }

    public String GetInputString() {
        Log.println(Log.DEBUG, "CalcData", "GetInputString entered");
        Log.println(Log.DEBUG, "GetInputString", "Input length is now: " + inputs.size());
        // Build the string
        try {
            StringBuilder s = new StringBuilder();
            for (Object o : this.inputs) {
                if (o instanceof BigDecimal) {
                    Log.d("GetInputString", "Number found");
                    BigDecimal num = (BigDecimal) o;
                    if (num.compareTo(BigDecimal.valueOf(num.longValue())) != 0) {
                        s.append(num.toString());
                        Log.d("GetInputString", "Appended with decimal point");
                    } else {
                        s.append(num.longValue());
                        Log.d("GetInputString", "Appended without decimal point");
                    }

                } else if (o instanceof MathOperator) {
                    Log.d("GetInputString", "MathOperator found: " + ((MathOperator) o).name());
                    switch ((MathOperator) o) {
                        case ADD:
                            s.append(activity.getString(R.string.buttonAddText));
                            break;
                        case SUBTRACT:
                            s.append(activity.getString(R.string.buttonSubtractText));
                            break;
                        case DIVIDE:
                            s.append(activity.getString(R.string.buttonDivideText));
                            break;
                        case MULTIPLY:
                            s.append(activity.getString(R.string.buttonMultiplyText));
                            break;
                        case POW:
                            s.append(activity.getString(R.string.buttonPowText));
                    }
                }
            }
            Log.d("GetInputString", s.toString());
            return s.toString();
        } catch (Exception ex) {
            Log.e("GetInputString", ex.toString());
            return "Internal Error";
        }


    }

    public void ClearInput() {
        this.inputs.clear();
        decimalActive = false;
        curDecimalPlaces = 0;
        Log.d("CalcData", "Cleared inputs, reset decimalActive and curDecimalPlaces");
    }

    public BigDecimal Calculate() {
        ArrayList<Object> inputCopy = (ArrayList<Object>) this.inputs.clone();

        Log.d("CalcData", "Calculate entered");
        // If there are no inputs, return zero
        if (inputCopy.isEmpty()) {
            Log.d("Calculate", "Input list is empty");
            return BigDecimal.ZERO;
        }

        // If there are less than 3 inputs, don't do anything because there is nothing to calculate
        if (inputCopy.size() < 3) {
            Log.d("Calculate", "There is nothing to calculate");
            return (BigDecimal) inputCopy.get(0);
        }

        if (inputCopy.get(inputCopy.size() - 1) instanceof MathOperator) {
            Log.d("Calculate", "Removed last unnecessary operator");
            inputCopy.remove(inputCopy.size() - 1);
        }

        Log.d("Calculate", "Input amount: " + inputCopy.size());
        Log.d("Calculate", "Multiplying and Dividing...");
        // Pow
        for (int i = 1; i < inputCopy.size() - 1; i++) {
            Log.d("Calculate", "Index: " + i);
            Object current = inputCopy.get(i);
            if (current instanceof MathOperator) {
                BigDecimal result = null;
                BigDecimal previous = (BigDecimal) inputCopy.get(i - 1);
                BigDecimal next = (BigDecimal) inputCopy.get(i + 1);
                if (current == MathOperator.POW) {
                    result = BigDecimalMath.pow(previous, next);
                    inputCopy.set(i, result);
                    inputCopy.remove(i + 1);
                    inputCopy.remove(i - 1);
                    i -= 2;
                }
            }
        }
        // Multiply and Divide
        for (int i = 1; i < inputCopy.size() - 1; i++) {
            Log.d("Calculate", "Index: " + i);
            Object current = inputCopy.get(i);
            if (current instanceof MathOperator) {
                BigDecimal result = null;
                BigDecimal previous = (BigDecimal) inputCopy.get(i - 1);
                BigDecimal next = (BigDecimal) inputCopy.get(i + 1);
                switch ((MathOperator) current) {
                    case MULTIPLY:
                        result = previous.multiply(next);
                        inputCopy.set(i, result);
                        inputCopy.remove(i + 1);
                        inputCopy.remove(i - 1);
                        i -= 2;
                        break;
                    case DIVIDE:
                        result = previous.divide(next, 32, RoundingMode.HALF_DOWN);
                        inputCopy.set(i, result);
                        inputCopy.remove(i + 1);
                        inputCopy.remove(i - 1);
                        i -= 2;
                        break;
                }
            }
        }

        // Add and subtract
        for (int i = 1; i < inputCopy.size() - 1; i++) {
            Object current = inputCopy.get(i);
            if (current instanceof MathOperator) {
                BigDecimal result = null;
                BigDecimal previous = (BigDecimal) inputCopy.get(i - 1);
                BigDecimal next = (BigDecimal) inputCopy.get(i + 1);
                switch ((MathOperator) current) {
                    case ADD:
                        result = previous.add(next);
                        inputCopy.set(i, result);
                        inputCopy.remove(i + 1);
                        inputCopy.remove(i - 1);
                        i -= 2;
                        break;
                    case SUBTRACT:
                        result = previous.subtract(next);
                        inputCopy.set(i, result);
                        inputCopy.remove(i + 1);
                        inputCopy.remove(i - 1);
                        i -= 2;
                        break;
                }
            }
        }
        BigDecimal output = (BigDecimal) inputCopy.get(0);
        Log.d("Calculate", "Result: " + output.toString());
        return output;
    }

    public void Back() {
        Log.d("CalcData", "Back entered");

        Object last = GetLastInput();
        if (last instanceof MathOperator) {
            Log.d("Back", "Last input is a MathOperator. Removing it...");
            this.inputs.remove(last);
        } else {
            Log.d("Back", "Last input is a number");
            BigDecimal num = (BigDecimal) last;

            if (decimalActive) {
                Log.d("Back", "Decimals are active. Removing decimal place...");
                // Get the last digit and subtract it
                // Also subtract 1 from the curDecimalPlaces
                BigDecimal divisor = BigDecimal.ONE.movePointLeft(--this.curDecimalPlaces - 1);
                BigDecimal[] lastDigit = num.divideAndRemainder(divisor);
                BigDecimal remainder = lastDigit[1];
                inputs.set(this.inputs.size() - 1, num.subtract(remainder));

                // Set decimalActive to false, if we removed all decimal places
                if (this.curDecimalPlaces == 1) {
                    this.curDecimalPlaces = 0;
                    this.decimalActive = false;
                }
            } else {
                Log.d("Back", "Decimals are not active. Removing last digit...");
                // Divide by 10 and discard the decimal places
                inputs.set(this.inputs.size() - 1, num.divideToIntegralValue(BigDecimal.TEN));
            }
        }
    }

    enum MathOperator {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        POW
    }
}
