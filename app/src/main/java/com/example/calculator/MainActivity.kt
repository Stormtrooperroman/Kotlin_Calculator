package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Button
import androidx.constraintlayout.widget.Group
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private var numbers = ArrayList<Double>()
    private var operationsList = ArrayList<Operation>()
    private var ten: Double=10.0
    private var lastNumeric: Boolean = false
    private var plusMinus: Boolean = false
    private var lastDot: Boolean = false
    private var lastPercent:Boolean=false
    private var valOne: Double=0.0
    private var valTwo: Double=0.0
    private var valThree: Double=0.0
    private var colDot: Int=0
    private var useOtherVal:Boolean=false
    private var plusNew:Boolean=false
    private var minusNew: Boolean=false
    private var sum: Boolean=false
    private var value: Double=0.0
    private var lastRandom:Boolean=false

    enum class Operation(val oper: String){
        Plus("+"),
        Minus("-"),
        Mul("x"),
        Div("÷"),
        Dot("."),
        Percent("%")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val digits: Group = findViewById(R.id.digits)
        equalButton.setOnClickListener{
            numbers.add(value)
            if(operationsList.size!=0) {
                findAns()
            }
            else{
                output(numbers[0])
            }

            sum=true
        }
        if (randButton != null) {
            randButton.setOnClickListener {
                if(sum){
                    lastDot=false
                    lastNumeric=false
                    lastPercent=false
                    numbers.clear()
                    operationsList.clear()
                    outputTextView.text=""
                    value=((0..100000).random()).toDouble()
                    outputTextView.append(value.toInt().toString())
                    sum=false
                    lastRandom=true
                }
                else if(!lastNumeric && !lastDot && !lastRandom){
                    value=((0..100000).random()).toDouble()
                    outputTextView.append(value.toInt().toString())
                    lastRandom=true
                }
            }
        }


        percentBtn.setOnClickListener {

            if(lastNumeric&& !lastDot || plusMinus || sum || lastRandom){
                value /=100
                lastPercent=true
                lastDot=false
                lastNumeric=false
                numbers.clear()
                operationsList.clear()
                outputTextView.append(Operation.Percent.oper)
                sum=false
                lastRandom=false
            }
        }

        plusMinusBtn.setOnClickListener {
            if(sum){
                lastDot=false
                lastNumeric=false
                lastPercent=false
                numbers.clear()
                operationsList.clear()
                sum=false
                value*=-1
                outputTextView.text="-(" + outputTextView.text.toString()+")"
                plusMinus=true
                lastNumeric = false
                lastDot = false
                lastPercent=false
                colDot=0
                lastRandom=false
            }
            else if(lastNumeric&& !lastDot || lastPercent || lastRandom){
                value*=-1
                outputTextView.text="-(" + outputTextView.text.toString()+")"
                plusMinus=true
                lastNumeric = false
                lastDot = false
                lastPercent=false
                colDot=0
                lastRandom=false
            }

        }

        digits.setAllOnClickListener(View.OnClickListener {
            if(sum){
                lastDot=false
                lastNumeric=false
                lastPercent=false
                numbers.clear()
                operationsList.clear()
                outputTextView.text=""
                value=(it as Button).text.toString().toDouble()
                outputTextView.append(it.text)
                lastNumeric = true
                sum=false
                lastRandom=false
            }
            else if(lastDot && !plusMinus && !lastPercent && !lastRandom){
                value+= (it as Button).text.toString().toDouble() * ten.pow(-1 * colDot)
                colDot++
                outputTextView.append(it.text)
                lastNumeric = true
            }
            else if(lastNumeric && !plusMinus && !lastPercent && !lastRandom){
                value = value * 10 + (it as Button).text.toString().toDouble()
                outputTextView.append(it.text)
                lastNumeric = true
            }
            else if(!lastNumeric && !lastDot && !lastPercent && !plusMinus && !lastRandom){
                value=(it as Button).text.toString().toDouble()
                outputTextView.append(it.text)
                lastNumeric = true
            }

        })
        operations.setAllOnClickListener(View.OnClickListener {
            if(sum){
                sum=false
                numbers.clear()
                operationsList.clear()
            }
            numbers.add(value)
            outputTextView.text=""

            if((it as Button).text.toString()==Operation.Mul.oper){
                operationsList.add(Operation.Mul)
            }
            else if((it as Button).text.toString()==Operation.Div.oper){
                operationsList.add(Operation.Div)
            }
            else if((it as Button).text.toString()==Operation.Plus.oper){
                operationsList.add(Operation.Plus)
            }
            else if((it as Button).text.toString()==Operation.Minus.oper){
                operationsList.add(Operation.Minus)
            }
            lastNumeric = false
            lastDot = false
            lastPercent=false
            plusMinus=false
            colDot=0
            value=0.0
            lastRandom=false
            sum=false
        })

        commaButton.setOnClickListener {
            if(!lastDot && lastNumeric){
                outputTextView.append(Operation.Dot.oper)
                lastNumeric = false
                lastDot = true
                colDot=1
            }
        }
        acButton.setOnClickListener {
            lastDot=false
            lastNumeric=false
            lastPercent=false
            useOtherVal=false
            lastRandom=false
            plusNew=false
            minusNew=false
            plusMinus=false
            valOne=0.0
            valTwo=0.0
            valThree=0.0
            value=0.0
            numbers.clear()
            operationsList.clear()
            outputTextView.text=""
        }
    }

    private fun muvAndDiv(operator:Operation){
        if (operator==Operation.Mul && !useOtherVal){
            valOne*=valTwo
            valTwo=0.0
        }
        else if(operator==Operation.Div && !useOtherVal){

            valOne/=valTwo
            valTwo=0.0
        }
        else if(operator==Operation.Div && useOtherVal){
            valThree/=valTwo
            valTwo=0.0

        }
        else if(operator==Operation.Mul && useOtherVal){
            valThree*=valTwo
            valTwo=0.0
        }
    }

    private fun  sumAndMinus(operator:Operation){
        positionSum()
        if (operator==Operation.Plus){
            valOne+=valTwo
            valTwo=0.0
        }
        else if(operator==Operation.Minus){
            valOne-=valTwo
            valTwo=0.0
        }
    }
    private fun positionSum(){
        if(plusNew){
            valOne+=valThree
            valThree=0.0
            useOtherVal=false
            plusNew=false
        }
        else if(minusNew){
            valOne-=valThree
            valThree=0.0
            useOtherVal=false
            minusNew=false
        }
    }
    private fun findAns(){
        valOne=numbers[0]

        for (i in 0..operationsList.size-2) {
            valTwo=numbers[i+1]
            if(operationsList[i]==Operation.Mul || operationsList[i]==Operation.Div ){
                muvAndDiv(operationsList[i])
            }
            else if(operationsList[i+1]!=Operation.Mul && operationsList[i+1]!=Operation.Div && (operationsList[i]==Operation.Plus || operationsList[i]==Operation.Minus)){
                sumAndMinus(operationsList[i])
            }
            else if((operationsList[i+1]==Operation.Mul  || operationsList[i+1]==Operation.Div ) && operationsList[i]==Operation.Plus ){
                valThree=valTwo
                useOtherVal=true
                plusNew=true
            }
            else if((operationsList[i+1]==Operation.Mul  || operationsList[i+1]==Operation.Div ) && operationsList[i]==Operation.Minus){
                valThree=valTwo
                useOtherVal=true
                minusNew=true
            }

        }
        if(operationsList[operationsList.size-1]==Operation.Plus  || operationsList[operationsList.size-1]==Operation.Minus ){
            valTwo=numbers[operationsList.size]
            sumAndMinus(operationsList[operationsList.size-1])
        }

        else if(operationsList[operationsList.size-1]!=Operation.Mul  || operationsList[operationsList.size-1]!=Operation.Div ){
            valTwo=numbers[operationsList.size]
            muvAndDiv(operationsList[operationsList.size-1])
        }
        positionSum()


        output(valOne)
    }

    private fun output(ans:Double){
        val longAns=ans.toLong()
        value=ans
        if(ans==longAns.toDouble()){
            outputTextView.text=longAns.toString()
        }
        else{

            outputTextView.text=ans.toString()
        }
    }

    private fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }


}