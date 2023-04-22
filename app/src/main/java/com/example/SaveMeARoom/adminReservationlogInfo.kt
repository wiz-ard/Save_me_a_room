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

class adminReservationlogInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_reservationlog_info)
        //getting values from previous page
        var action = intent.getStringExtra("action")
        var displayId = intent.getStringExtra("displayId")
        var logInfo = intent.getStringExtra("log info").toString()
        var logSplit = logInfo.split(",")

        //setting variables for different textviews on page
        val tvIdLog = findViewById<TextView>(R.id.tvIdLog)
        val tvBldgLog = findViewById<TextView>(R.id.tvBldgLog)
        val tvResRoomLog = findViewById<TextView>(R.id.tvResRoomLog)
        val tvResTimeLog = findViewById<TextView>(R.id.tvResTimeLog)
        val tvResEmailLog = findViewById<TextView>(R.id.tvResEmailLog)
        val tvResTimeActionLog = findViewById<TextView>(R.id.tvResTimeActionLog)
        val tvResEmailActionLog = findViewById<TextView>(R.id.tvResEmailActionLog)

        val btnBack = findViewById<Button>(R.id.btnResBackLog)

        tvIdLog.text = logSplit[0]
        tvBldgLog.text = logSplit[1]
        tvResRoomLog.text = logSplit[2]
        tvResTimeLog.text = logSplit[3]
        tvResEmailLog.text = logSplit[4]
        tvResTimeActionLog.text = logSplit[5]
        tvResEmailActionLog.text = logSplit[6]
        btnBack.setOnClickListener {
            finish()
        }
    }
}