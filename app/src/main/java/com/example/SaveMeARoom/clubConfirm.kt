package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.book_confirmation.*
import kotlinx.android.synthetic.main.club_confirm.*
import kotlinx.android.synthetic.main.create_account.*
import kotlinx.android.synthetic.main.my_reservation_confirmation.*
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime

class clubConfirm : AppCompatActivity() {
    private lateinit var email:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.club_confirm)

        email = intent.getStringExtra("email").toString()
        val college = intent.getStringExtra("college")

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val btnYesCancel = findViewById<Button>(R.id.btnYesClub)
        val btnNoCancel = findViewById<Button>(R.id.btnNoClub)

        btnYesCancel.setOnClickListener {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

            StrictMode.setThreadPolicy(policy)

            val clubName = club.text.toString()

            val ip = "http://3.132.20.107:3000"

            val query = "/search?query=INSERT%20INTO%20statusrequests%20VALUES(%27" + email + "%27, %271%27, %270%27, %27" + clubName + "%27, %27" + college + "%27, %27null%27,0)"

            val url = URL(ip.plus(query))

            url.readText()

            Toast.makeText(this, "Club request sent.", Toast.LENGTH_SHORT).show()

            finish()

        }
        btnNoCancel.setOnClickListener {
            finish()
        }
    }
}