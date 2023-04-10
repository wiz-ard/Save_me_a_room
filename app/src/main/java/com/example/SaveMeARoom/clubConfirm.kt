package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.book_confirmation.*
import kotlinx.android.synthetic.main.my_reservation_confirmation.*
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime

class clubConfirm : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.club_confirm)

        val username = intent.getStringExtra("username")

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val btnYesCancel = findViewById<Button>(R.id.btnYesClub)
        val btnNoCancel = findViewById<Button>(R.id.btnNoClub)

        btnYesCancel.setOnClickListener {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

            StrictMode.setThreadPolicy(policy)

            val ip = "http://3.132.20.107:3000"

            val query = "/search?query=INSERT%20INTO%20clubrequests%20VALUES(%27" + username + "%27)"

            val url = URL(ip.plus(query))

            val text = url.readText()

            Toast.makeText(this, "Club request sent.", Toast.LENGTH_SHORT).show()

            finish()

        }
        btnNoCancel.setOnClickListener {
            finish()
        }
    }
}