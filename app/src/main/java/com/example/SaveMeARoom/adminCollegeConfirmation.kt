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
            var query = "/search?query=SELECT%20Viewing%20FROM%20statusrequests%20WHERE%20College_Request=%271%27%20AND%20Email=%27" + email + "%27"
            var url = URL(ip.plus(query))
            val viewing = url.readText().substringAfter(":").substringAfter('"').substringBefore('"')

            if(viewing == "1"){
                Toast.makeText(this, "Request is being viewed by another admin.", Toast.LENGTH_SHORT).show()
            }
            else{
                query = "/search?query=UPDATE%20users%20SET%20College=%27" + newCollege + "%27%20WHERE%20Email=%27" + email + "%27"
                url = URL(ip.plus(query))
                url.readText()

                query = "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"
                url = URL(ip.plus(query))
                url.readText()

                Toast.makeText(this, "College change request accepted.", Toast.LENGTH_SHORT).show()

                finish()
            }

        }
        btnNoCancel.setOnClickListener {
            var query = "/search?query=SELECT%20Viewing%20FROM%20statusrequests%20WHERE%20College_Request=%271%27%20AND%20Email=%27" + email + "%27"
            var url = URL(ip.plus(query))
            val viewing = url.readText().substringAfter(":").substringAfter('"').substringBefore('"')

            if(viewing == "1"){
                Toast.makeText(this, "Request is being viewed by another admin.", Toast.LENGTH_SHORT).show()
            }
            else{
                val query = "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"
                val url = URL(ip.plus(query))
                url.readText()
                Toast.makeText(this, "College change request denied.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}