package com.example.SaveMeARoom

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //admin admin username password test
        loginbtn.setOnClickListener {
            val username = username.text.toString()
            val password = password.text.toString()

            val nextPage = Intent(this, home::class.java)

            if (username == "admin" && password == "admin") {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                startActivity(nextPage)
                finish()

            } else {
                Toast.makeText(this, "Incorrect Login", Toast.LENGTH_SHORT).show()
            }
        }
    }
}