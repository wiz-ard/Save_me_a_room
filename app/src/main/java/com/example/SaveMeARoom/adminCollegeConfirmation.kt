package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

        val ip = "http://3.132.20.107:3000"

        val btnYesCancel = findViewById<Button>(R.id.btnYesCancel)
        val btnNoCancel = findViewById<Button>(R.id.btnNoCancel)

        btnYesCancel.setOnClickListener {
            val curTime = LocalTime.now()
            val curDate = LocalDate.now()
            val logTime = curTime.toString().substringBefore(".") + " " + curDate.toString()

            var query = "/search?query=UPDATE%20users%20SET%20College=%27" + newCollege + "%27%20WHERE%20Email=%27" + email + "%27"
            var url = URL(ip.plus(query))
            url.readText()

            query = "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"
            url = URL(ip.plus(query))
            url.readText()

            Toast.makeText(this, "College change request accepted.", Toast.LENGTH_SHORT).show()

            //update status logs
            query = "/search?query=INSERT%20INTO%20statuslogs%20VALUES(%27" + email + "%27,%27" + college + "%27,%27" + adminEmail + "%27,%27" + logTime + "%27,%270%27,%270%27,%271%27,%270%27)"
            url = URL(ip.plus(query))
            url.readText()

            finish()

        }
        btnNoCancel.setOnClickListener {
            val curTime = LocalTime.now()
            val curDate = LocalDate.now()
            val logTime = curTime.toString().substringBefore(".") + " " + curDate.toString()

            var query = "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"
            var url = URL(ip.plus(query))
            url.readText()

            //update status logs
            query =
                "/search?query=INSERT%20INTO%20statuslogs%20VALUES(%27" + email + "%27,%27" + college + "%27,%27" + adminEmail + "%27,%27" + logTime + "%27,%270%27,%270%27,%270%27,%271%27)"
            url = URL(ip.plus(query))
            url.readText()

            Toast.makeText(this, "College change request denied.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}