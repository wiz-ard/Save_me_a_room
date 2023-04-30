package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.admin_club_confirmation.*
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
        val adminEmail = intent.getStringExtra("adminemail").toString()
        val college = intent.getStringExtra("college").toString()

        val infoSplit = requestInfo.split(",")

        val email = infoSplit[0]
        val clubName = infoSplit[1].substringAfter(" ")

        tvClubEmail.text = "Email: " + email
        tvClubName.text = "Club Name: " + clubName

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val btnYesClub = findViewById<Button>(R.id.btnYesClub)
        val btnNoClub = findViewById<Button>(R.id.btnNoClub)

        btnYesClub.setOnClickListener {
            val intent = Intent(this, adminStatusConfirm::class.java)
            intent.putExtra("request info", requestInfo)
            intent.putExtra("adminemail",adminEmail)
            intent.putExtra("flag","1")
            intent.putExtra("type", "club")
            intent.putExtra("college", college)
            startActivity(intent)
            finish()
        }
        btnNoClub.setOnClickListener {
            val intent = Intent(this, adminStatusConfirm::class.java)
            intent.putExtra("request info", requestInfo)
            intent.putExtra("adminemail",adminEmail)
            intent.putExtra("flag","0")
            intent.putExtra("type", "club")
            intent.putExtra("college", college)
            startActivity(intent)
            finish()
        }
        btnCancel.setOnClickListener{
            finish()
        }
    }
}