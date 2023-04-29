package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.admin_userlog_info.*
import kotlinx.android.synthetic.main.time_items.*
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime

class adminUserlogInfo : AppCompatActivity() {
    private lateinit var infoList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_userlog_info)
        //getting values from previous page
        var logInfo = intent.getStringExtra("log info").toString()
        var logSplit = logInfo.split(",")

        //setting variables for different textviews on page
        val tvUserLog = findViewById<TextView>(R.id.tvUserLog)
        val tvDateLog = findViewById<TextView>(R.id.tvDateLog)
        val tvLoginLog = findViewById<TextView>(R.id.tvLoginLog)
        val tvLogoutLog = findViewById<TextView>(R.id.tvLogoutLog)
        val tvSuccessfulLog = findViewById<TextView>(R.id.tvSuccessfulLog)

        val btnBack = findViewById<Button>(R.id.btnBackLog)

        tvUserLog.text = "User: " + logSplit[0]
        if(!(logSplit[1].equals(" NULL"))){
            val inTime = logSplit[1].substringAfter(" ").substringBefore(" 2")
            tvDateLog.text = "Date: " + logSplit[1].substringAfter(" ").substringAfter(" ").substringAfter(" ")
            tvLoginLog.text = "Login Time: " + inTime
            tvSuccessfulLog.isInvisible = true
            tvLogoutLog.text = "Successful: " + logSplit[3]
        }else{

        }
        if(!(logSplit[2].equals(" NULL"))){
            val test = logSplit[2]
            val outTime = logSplit[2].substringAfter(" ").substringBefore(" 2")
            tvDateLog.text = "Date: " + logSplit[2].substringAfter(" ").substringAfter(" ").substringAfter(" ")
            tvLoginLog.text = "Logout Time: " + outTime
            tvLogoutLog.isInvisible = true
            tvSuccessfulLog.isInvisible = true
        }else{

        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}