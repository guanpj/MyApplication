package com.me.guanpj.myapplication

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 *
 * @author guanpengjie
 * @date 2022/3/16
 */
class TextViewActivity : AppCompatActivity() {
    private lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)

        text = findViewById(R.id.txt)
        text.movementMethod = ScrollingMovementMethod()
        text.setOnClickListener {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
        }
    }
}