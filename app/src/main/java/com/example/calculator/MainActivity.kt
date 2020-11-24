package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Button
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    var ten: Double=10.0
    var lastNumeric: Boolean = false
    var stateError: Boolean = false
    var lastDot: Boolean = false
    var lastPercent:Boolean=false
    var plus_minus: Boolean=false
    var val1: Double=0.0;
    var val2: Double=0.0;
    var val3: Double=0.0;
    var colDot: Int=0;
    var useVal1:Boolean=false
    var useVal2:Boolean=false
    var useVal3:Boolean=false
    var plus:Boolean=false
    var minus: Boolean=false
    var mul: Boolean=false
    var div: Boolean=false
    var sum: Boolean=false
    var len:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        equal.setOnClickListener{

            if((mul || div) && (plus || minus)){
                if(div){
                    val2/=val3
                    val3=0.0
                    useVal3=false
                    div=false
                    if(plus){
                        val1+=val2
                        useVal2=false
                        val2=0.0
                        plus=false
                    }
                    else if(minus){
                        val1-=val2
                        useVal2=false
                        val2=0.0
                        minus=false
                    }
                }
                if(mul){
                    val2*=val3
                    val3=0.0
                    useVal3=false
                    mul=false
                    if(plus){
                        val1+=val2
                        useVal2=false
                        val2=0.0
                        plus=false
                    }
                    else if(minus){
                        val1-=val2
                        useVal2=false
                        val2=0.0
                        minus=false
                    }
                }
            }
            else if(mul || div){
                if(div) {
                    val1 /= val2
                    val2 = 0.0
                    useVal2 = false
                    div=false
                }
                if(mul) {
                    val1 *= val2
                    val2 = 0.0
                    useVal2 = false
                    mul=false
                }
            }
            else if(plus || minus){
                if(plus){
                    val1+=val2
                    useVal2=false
                    val2=0.0
                    plus=false
                }
                else if(minus){
                    val1-=val2
                    useVal2=false
                    val2=0.0
                    minus=false
                }


            }
            output.text = val1.toString()
            sum=true
        }
    }

    fun onDigit(view: View) {
        if (stateError || sum) {
            output.text=""
            val1 = (view as Button).text.toString().toDouble()
            sum=false
            stateError = false
            useVal1=true
            output.append((view as Button).text)
        }
        else if(!lastPercent && !plus_minus){
            if (output.text.toString().length+1<13){
                if (!lastNumeric && !useVal1) {
                    val1 = (view as Button).text.toString().toDouble()
                    useVal1 = true
                } else if (lastNumeric && !useVal2 && !lastDot) {

                    val1 = val1 * 10 + (view as Button).text.toString().toDouble()
                    useVal1 = true

                } else if (!lastNumeric && !useVal2 && lastDot) {
                    val1 += (view as Button).text.toString().toDouble() * ten.pow(-1 * colDot)
                    colDot++
                    useVal1 = true
                } else if (!lastDot && ((plus || minus) && !(div || mul)) || (!(plus || minus) && (div || mul))) {
                    val2 = val2 * 10 + (view as Button).text.toString().toDouble()
                    useVal2 = true
                } else if (lastDot && !lastNumeric && ((plus || minus) && !(div || mul)) || (!(plus || minus) && (div || mul))) {
                    val2 += (view as Button).text.toString().toDouble() * ten.pow(-1 * colDot)
                    colDot++
                    useVal2 = true
                } else if (!lastDot && ((plus || minus) && (div || mul))) {
                    val3 = val3 * 10 + (view as Button).text.toString().toDouble()
                    useVal3 = true
                } else if (lastDot && !lastNumeric && ((plus || minus) && (div || mul))) {
                    val3 += (view as Button).text.toString().toDouble() * ten.pow(-1 * colDot)
                    colDot++
                    useVal3 = true
                }

                output.append((view as Button).text)
            }


        }

        lastNumeric = true
        lastPercent=false
    }

    fun onOperator(view: View) {
        if ((lastNumeric || plus_minus) && !stateError &&(output.text.toString().length+1<12)) {
            if(mul || div){
                if (mul && !useVal3){
                    val1*=val2
                    val2=0.0
                    useVal2=false
                    mul=false
                }
                else if(div && !useVal3){

                    val1/=val2
                    val2=0.0
                    useVal2=false
                    div=false
                }
                else if(div && useVal3){
                    val2/=val3
                    val3=0.0
                    useVal3=false
                    div=false
                }
                else if(mul && useVal3){
                    val2/=val3
                    val3=0.0
                    useVal3=false
                    mul=false
                }
            }
            if("x"==(view as Button).text.toString()){
                mul=true
                output.append("⋅")
                plus_minus=false

            }
            else if("÷"==(view as Button).text.toString()){
                output.append("/")
                div=true
                plus_minus=false
            }
            if(!mul && !div){
                if (plus){

                    val1+=val2
                    val2=0.0
                    useVal2=false
                    plus=false
                }
                else if(minus){
                    val1-=val2
                    val2=0.0
                    useVal2=false
                    minus=false
                }
            }
            if("+"==(view as Button).text.toString()){
                plus=true
                output.append((view as Button).text)
                plus_minus=false
            }
            else if("-"==(view as Button).text.toString()){
                minus=true
                output.append((view as Button).text)
                plus_minus=false
            }
            else if("+/-"==(view as Button).text.toString()){
                val1*=-1
                output.text="-(" + output.text.toString()+")"
                plus_minus=true
            }

            sum=false
            lastNumeric = false
            lastDot = false
            lastPercent=false
            colDot=0
        }
    }
    fun onClear(view: View){
        output.text=""
        lastNumeric = false
        lastDot = false
        stateError=false
        val1=0.0
        val2=0.0
        val3=0.0
        useVal2=false
        useVal1=false
        useVal3=false
        plus=false
        minus=false
        div=false
        mul=false
        lastPercent=false
        plus_minus=false
        sum=false
        colDot=0
    }

    fun addDot(view: View){
        if (lastNumeric && !stateError && !lastDot && !sum) {
            output.append(".")
            lastNumeric = false
            lastDot = true
            colDot=1
        }

    }
    fun percent(view: View){
        if(lastNumeric){
            if(!useVal2 && !lastDot){
                val1 /=100
            }
            else if(!lastDot && ((plus || minus) && !(div || mul)) || (!(plus || minus) && (div || mul))){
                val2 /=100

            }
            else if(!lastDot && ((plus || minus) && (div || mul))){

                val3 /=100

            }
            lastPercent=true
            output.append("%")

        }
    }


}