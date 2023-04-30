package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.admin_college_confirmation.*
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime

class adminCollegeConfirmation : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_college_confirmation)
        //pulls reservation information
        val requestInfo = intent.getStringExtra("request info").toString()
        val adminEmail = intent.getStringExtra("adminemail").toString()
        val college = intent.getStringExtra("college").toString()

        val infoSplit = requestInfo.split(",")

        val email = infoSplit[0]
        val newCollege = infoSplit[1].substringAfter(" ")

        val tvCollegeEmail = findViewById<TextView>(R.id.tvCollegeEmailText)
        val tvOldCollege = findViewById<TextView>(R.id.tvOldCollegeText)
        val tvNewCollege = findViewById<TextView>(R.id.tvNewCollegeText)

        tvCollegeEmail.text = "Email: " + email
        tvOldCollege.text = "Old College: " + college
        tvNewCollege.text = "New College: " + newCollege

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val btnYesCancel = findViewById<Button>(R.id.btnYesCollege)
        val btnNoCancel = findViewById<Button>(R.id.btnNoCollege)

        btnYesCancel.setOnClickListener {
            val intent = Intent(this, adminStatusConfirm::class.java)
            intent.putExtra("request info", requestInfo)
            intent.putExtra("adminemail",adminEmail)
            intent.putExtra("flag","1")
            intent.putExtra("type", "college")
            intent.putExtra("college", college)
            startActivity(intent)
            finish()
        }
        btnNoCancel.setOnClickListener {
            val intent = Intent(this, adminStatusConfirm::class.java)
            intent.putExtra("request info", requestInfo)
            intent.putExtra("adminemail",adminEmail)
            intent.putExtra("flag","0")
            intent.putExtra("type", "college")
            intent.putExtra("college", college)
            startActivity(intent)
            finish()
        }
        btnCancel.setOnClickListener{
            finish()
        }
    }
}