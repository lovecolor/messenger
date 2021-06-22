package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        txtInput = findViewById<View>(R.id.txt_input) as TextView
//        txtResult = findViewById<View>(R.id.txt_result) as TextView
//        btn0 = findViewById<View>(R.id.btn_0) as Button
//        btn1 = findViewById<View>(R.id.btn_1) as Button
//        btn2 = findViewById<View>(R.id.btn_2) as Button
//        btn3 = findViewById<View>(R.id.btn_3) as Button
//        btn4 = findViewById<View>(R.id.btn_4) as Button
//        btn5 = findViewById<View>(R.id.btn_5) as Button
//        btn6 = findViewById<View>(R.id.btn_6) as Button
//        btn7 = findViewById<View>(R.id.btn_7) as Button
//        btn8 = findViewById<View>(R.id.btn_8) as Button
//        btn9 = findViewById<View>(R.id.btn_9) as Button
//        btnPlus= findViewById<View>(R.id.btn_plus) as Button
//        btnSub = findViewById<View>(R.id.btn_sub) as Button
//        btnDiv = findViewById<View>(R.id.btn_div) as Button
//        btnMul = findViewById<View>(R.id.btn_mul) as Button

        btn_plus.setOnClickListener {
            setInput(btn_plus)
        }
        btn_div.setOnClickListener {
            setInput(btn_div)
        }
        btn_mul.setOnClickListener {
            setInput(btn_mul)
        }
        btn_sub.setOnClickListener {
            setInput(btn_sub)
        }
        btn_0.setOnClickListener {
            setInput(btn_0)
        }
        btn_1.setOnClickListener {
            setInput(btn_1)
        }
        btn_2.setOnClickListener {
            setInput(btn_2)
        }
        btn_3.setOnClickListener {
            setInput(btn_3)
        }
        btn_4.setOnClickListener {
            setInput(btn_4)
        }
        btn_5.setOnClickListener {
            setInput(btn_5)
        }
        btn_6.setOnClickListener {
            setInput(btn_6)
        }
        btn_7.setOnClickListener {
            setInput(btn_7)
        }
        btn_8.setOnClickListener {
            setInput(btn_8)
        }
        btn_9.setOnClickListener {
            setInput(btn_9)
        }
        btn_MC.setOnClickListener {
            txt_input.setText("")
        }






    }

    fun setInput(btn: Button) {
        txt_input.setText(txt_input.getText().toString() + btn.getText().toString())

    }
}