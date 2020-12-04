package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Button
import androidx.constraintlayout.widget.Group
import kotlin.math.pow
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private var numbers = ArrayList<Double>()
    private var operationsList = ArrayList<String>()
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
    private val plus:String="+"
    private val minus:String="-"
    private val dot:String="."
    private val percent:String="%"
    private val mul: String="x"
    private val div: String="÷"
    private var sum: Boolean=false
    private var value: Double=0.0

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

        percentBtn.setOnClickListener {

            if(lastNumeric&& !lastDot || plusMinus || sum){
                value /=100
                lastPercent=true
                lastDot=false
                lastNumeric=false
                numbers.clear()
                operationsList.clear()
                outputTextView.append(percent)
                sum=false
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
            }
            else if(lastNumeric&& !lastDot || lastPercent){
                value*=-1
                outputTextView.text="-(" + outputTextView.text.toString()+")"
                plusMinus=true
                lastNumeric = false
                lastDot = false
                lastPercent=false
                colDot=0
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
            }
            else if(lastDot && !plusMinus && !lastPercent){
                value+= (it as Button).text.toString().toDouble() * ten.pow(-1 * colDot)
                colDot++
                outputTextView.append(it.text)
                lastNumeric = true
            }
            else if(lastNumeric && !plusMinus && !lastPercent){
                value = value * 10 + (it as Button).text.toString().toDouble()
                outputTextView.append(it.text)
                lastNumeric = true
            }
            else if(!lastNumeric && !lastDot && !lastPercent && !plusMinus){
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
            operationsList.add((it as Button).text.toString())
            lastNumeric = false
            lastDot = false
            lastPercent=false
            plusMinus=false
            colDot=0
            value=0.0
            sum=false
        })

        commaButton.setOnClickListener {
            if(!lastDot && lastNumeric){
                outputTextView.append(dot)
                lastNumeric = false
                lastDot = true
                colDot=1
            }
        }
        ACButton.setOnClickListener {
            lastDot=false
            lastNumeric=false
            lastPercent=false
            useOtherVal=false
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

    private fun muvAndDiv(operator:String){
        if (operator==mul && !useOtherVal){
            valOne*=valTwo
            valTwo=0.0
        }
        else if(operator==div && !useOtherVal){

            valOne/=valTwo
            valTwo=0.0
        }
        else if(operator==div && useOtherVal){
            valThree/=valTwo
            valTwo=0.0

        }
        else if(operator==mul && useOtherVal){
            valThree*=valTwo
            valTwo=0.0
        }
    }

    private fun  sumAndMinus(operator:String){
        positionSum()
        if (operator==plus){
            valOne+=valTwo
            valTwo=0.0
        }
        else if(operator==minus){
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
            if(operationsList[i]==mul || operationsList[i]==div){
                muvAndDiv(operationsList[i])
            }
            else if(operationsList[i+1]!=mul && operationsList[i+1]!=div && (operationsList[i]==plus || operationsList[i]==minus)){
                sumAndMinus(operationsList[i])
            }
            else if((operationsList[i+1]==mul || operationsList[i+1]==div) && operationsList[i]==plus){
                valThree=valTwo
                useOtherVal=true
                plusNew=true
            }
            else if((operationsList[i+1]==mul || operationsList[i+1]==div) && operationsList[i]==minus){
                valThree=valTwo
                useOtherVal=true
                minusNew=true
            }

        }
        if(operationsList[operationsList.size-1]==plus || operationsList[operationsList.size-1]==minus){
            valTwo=numbers[operationsList.size]
            sumAndMinus(operationsList[operationsList.size-1])
        }

        else if(operationsList[operationsList.size-1]!=mul || operationsList[operationsList.size-1]!=div){
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