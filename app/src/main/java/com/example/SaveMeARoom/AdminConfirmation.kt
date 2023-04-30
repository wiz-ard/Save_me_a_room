package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.admin_reservation_confirmation.*
import kotlinx.android.synthetic.main.time_items.*
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime

class AdminConfirmation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_reservation_confirmation)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"
        val resInfo = intent.getStringExtra("res info")
        val resSplit = resInfo.toString().split(",")
        val buildingName = resSplit[0]
        val room = resSplit[1].substringAfter(" ")
        val email = resSplit[2].substringAfter(' ')
        var time = resSplit[4].substringAfter(" ")
        var date = resSplit[5].substringAfter(" ")
        val adminEmail = intent.getStringExtra("email")
        var club = "0"
        var update = "0"

        val accepted = intent.getStringExtra("accepted")

        if (resSplit.size == 9) {
            if (resSplit[7].substringAfter(" ")
                    .equals("Club Request") || resSplit[8].substringAfter(" ")
                    .equals("Club Request")
            ) {
                club = "1"
            }
            if (resSplit[7].substringAfter(" ").equals("Update Request")) {
                update = "1"
            }
        } else if (resSplit.size >= 8) {
            if (resSplit[7].substringAfter(" ").equals("Club Request")) {
                club = "1"
            } else if (resSplit[7].substringAfter(" ").equals("Update Request")) {
                update = "1"
            }
        }
        val modifiedTime =
            date + " " + (time.substringBefore("-").toInt() + 12).toString() + ":00:00"

        //get reservation ID of reservation
        var query =
            "/search?query=SELECT%20Reservation_Id%20FROM%20reservations%20WHERE%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27%20AND%20Reserver_Email=%27" + email + "%27"

        var url = URL(ip.plus(query))

        var text = url.readText()

        var resId = text.substringAfter(":").substringAfter('"').substringBefore('"')

        var newtime = "null"
        var newdate = "null"
        if (update.equals("1")) {
            //check if its an update
            query =
                "/search?query=SELECT%20New_Start_Time,New_End_Time%20FROM%20updates%20WHERE%20Reservation_Id=%27" + resId + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            var times = text.split(",")
            var newStart = times[0].substringAfter(":").substringAfter('"').substringBefore('"')
            var newEnd = times[1].substringAfter(":").substringAfter('"').substringBefore('"')
            newdate = newStart.substringBefore(" ")
            newtime = ((newStart.substringAfter(" ").substringBefore(":")
                .toInt()) - 12).toString() + "-" + ((newEnd.substringAfter(" ").substringBefore(":")
                .toInt()) - 12).toString() + "pm"
        }

        var occupancy = resSplit[3]

        var college = resSplit[6]

        val tvBuildingNameConfirm = findViewById<TextView>(R.id.tvAdminBuildingNameConfirm)
        val tvDateConfirm = findViewById<TextView>(R.id.tvAdminDateConfirm)
        val tvTimeConfirm = findViewById<TextView>(R.id.tvAdminTimeConfirm)
        val tvOccupancyConfirm = findViewById<TextView>(R.id.tvAdminOccupancyConfirm)
        val tvRoomConfirm = findViewById<TextView>(R.id.tvAdminRoomConfirm)

        val btnConfirm = findViewById<Button>(R.id.btnAdminConfirm)
        val btnDeny = findViewById<Button>(R.id.btnAdminDeny)

        if (accepted == "1") {
            btnConfirm.isVisible = false
            btnDeny.isVisible = false
        }

        tvBuildingNameConfirm.text = buildingName
        if (occupancy.equals("0")) {
            occupancy = "2-10"
        } else if (occupancy.equals("1")) {
            occupancy = "11-29"
        } else if (occupancy.equals("2")) {
            occupancy = "30-49"
        } else {
            occupancy = "50+"
        }
        tvOccupancyConfirm.text = "Occupancy: " + occupancy
        tvRoomConfirm.text = "Room: " + room

        if (newdate.equals("null")) {
            tvDateConfirm.text = date
            tvTimeConfirm.text = time
        } else {
            val intent = Intent(this, AdminUpdateConfirmation::class.java)
            intent.putExtra("building", buildingName)
            intent.putExtra("time", time)
            intent.putExtra("date", date)
            intent.putExtra("newtime", newtime)
            intent.putExtra("newdate", newdate)
            intent.putExtra("occupancy", occupancy)
            intent.putExtra("room", room)
            intent.putExtra("email", email)
            intent.putExtra("college", college)
            intent.putExtra("adminemail", adminEmail)
            intent.putExtra("club", club)
            intent.putExtra("res info", resInfo)
            startActivity(intent)
            finish()
        }

        query =
            "/search?query=SELECT%20Viewing%20FROM%20reservations%20WHERE%20Reservation_Id=%27" + resId + "%27"
        url = URL(ip.plus(query))
        val view = url.readText().substringAfter(":").substringAfter('"').substringBefore('"')

        btnConfirm.setOnClickListener {
            val intent = Intent(this, adminResConfirm::class.java)
            intent.putExtra("res info", resInfo)
            intent.putExtra("email", adminEmail)
            intent.putExtra("flag", "1")
            intent.putExtra("club", club)
            intent.putExtra("resId", resId)
            startActivity(intent)
            finish()
        }

        btnDeny.setOnClickListener {
            val intent = Intent(this, adminResConfirm::class.java)
            intent.putExtra("res info", resInfo)
            intent.putExtra("email", adminEmail)
            intent.putExtra("flag", "0")
            intent.putExtra("club", club)
            intent.putExtra("resId", resId)
            startActivity(intent)
            finish()
            btnCancel.setOnClickListener {
                finish()
            }
        }
        btnCancel.setOnClickListener{
            finish()
        }
    }
}