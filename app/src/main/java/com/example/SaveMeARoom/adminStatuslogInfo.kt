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

class adminStatuslogInfo : AppCompatActivity() {
    private lateinit var infoList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_statuslog_info)
        //getting values from previous page
        var logInfo = intent.getStringExtra("log info").toString()
        var logSplit = logInfo.split(",")

        //setting variables for different textviews on page
        val tvUserEmailLog = findViewById<TextView>(R.id.tvUserEmailLog)
        val tvActionEmailLog = findViewById<TextView>(R.id.tvActionEmailLog)
        val tvActionTimeLog = findViewById<TextView>(R.id.tvActionTimeLog)
        val tvStatusActionLog = findViewById<TextView>(R.id.tvStatusActionLog)

        val btnBack = findViewById<Button>(R.id.btnBackLog)

        tvUserEmailLog.text = logSplit[0]
        tvActionEmailLog.text = logSplit[1]
        tvActionTimeLog.text = logSplit[2]
        tvStatusActionLog.text = logSplit[3]

        btnBack.setOnClickListener {
            finish()
        }
    }
}