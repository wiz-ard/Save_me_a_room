package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.create_account.*
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime

class adminClubConfirmation : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_club_confirmation)
        val tvClubEmail = findViewById<TextView>(R.id.tvClubEmailText)
        val tvClubName = findViewById<TextView>(R.id.tvClubNameText)
        //pulls reservation information
        val requestInfo = intent.getStringExtra("request info").toString()

        val infoSplit = requestInfo.split(",")

        val email = infoSplit[0]
        val clubName = infoSplit[1].substringAfter(" ")

        tvClubEmail.text = "Email: " + email
        tvClubName.text = "Club Name: " + clubName

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"

        val btnYesCancel = findViewById<Button>(R.id.btnYesCancel)
        val btnNoCancel = findViewById<Button>(R.id.btnNoCancel)

        btnYesCancel.setOnClickListener {

            var query =
                "/search?query=UPDATE%20users%20SET%20Club_Leader=%271%27%20WHERE%20Email=%27" + email + "%27"
            var url = URL(ip.plus(query))
            url.readText()

            query =
                "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20Club_Leader_Request=%271%27"
            url = URL(ip.plus(query))
            url.readText()

            Toast.makeText(this, "Club request accepted.", Toast.LENGTH_SHORT).show()

            finish()
        }
        btnNoCancel.setOnClickListener {

            val query =
                "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20Club_Leader_Request=%271%27"
            val url = URL(ip.plus(query))
            url.readText()
            Toast.makeText(this, "Club request denied.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}