package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.admin_reservation_confirmation.*
import kotlinx.android.synthetic.main.time_items.*
import java.net.URL

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
        val time = resSplit[2]
        val date = resSplit[3].substringAfter(" ")
        val room = resSplit[1].substringAfter(" ")
        val modifiedTime = date + " " + (time.substringAfter(" ").substringBefore("pm").toInt() + 12).toString() + ":00:00"
        var query = "/search?query=SELECT%20Reserver_Email%20FROM%20reservations%20WHERE%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

        var url = URL(ip.plus(query))

        var text = url.readText()

        var querysplit = text.split(",")

        var email = querysplit[0].substringAfter(":").substringAfter("\"").substringBefore("\"")

        query = "/search?query=SELECT%20Occupancy_Range%20FROM%20reservations%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

        url = URL(ip.plus(query))

        text = url.readText()
        querysplit = text.split(",")

        var occupancy = querysplit[0].substringAfter(":").substringAfter("\"").substringBefore("\"")
        val tvBuildingNameConfirm = findViewById<TextView>(R.id.tvAdminBuildingNameConfirm)
        val tvDateConfirm = findViewById<TextView>(R.id.tvAdminDateConfirm)
        val tvTimeConfirm = findViewById<TextView>(R.id.tvAdminTimeConfirm)
        val tvOccupancyConfirm = findViewById<TextView>(R.id.tvAdminOccupancyConfirm)
        val tvRoomConfirm = findViewById<TextView>(R.id.tvAdminRoomConfirm)

        val btnConfirm = findViewById<Button>(R.id.btnAdminConfirm)
        val btnDeny = findViewById<Button>(R.id.btnAdminDeny)

        tvBuildingNameConfirm.text = buildingName
        tvDateConfirm.text = date
        tvTimeConfirm.text = time
        if(occupancy.equals("0")){
            occupancy = "2-10"
        }else if(occupancy.equals("1")){
            occupancy = "11-29"
        }else if(occupancy.equals("2")){
            occupancy = "30-49"
        }else{
            occupancy = "50+"
        }
        tvOccupancyConfirm.text = "Occupancy: " + occupancy
        tvRoomConfirm.text = "Room: " + room

        btnConfirm.setOnClickListener {
            val date = intent.getStringExtra("date")
            val time = intent.getStringExtra("time")
            var start = ""
            var end = ""
            if(time.equals("5:00pm - 7:00pm")){
                start = "17:00:00"
                end = "19:00:00"
            }else if(time.equals("7:00pm - 9:00pm")){
                start = "19:00:00"
                end = "21:00:00"
            }else{
                start = "21:00:00"
                end = "23:00:00"
            }
            val occupancy = intent.getStringExtra("occupancy")

            val query = "/search?query=UPDATE%20reservations%20SET%20Pending=0%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

            val url = URL(ip.plus(query))

            val text = url.readText()

            Toast.makeText(this, "Reservation accepted.", Toast.LENGTH_SHORT).show()

            finish()
        }

        btnDeny.setOnClickListener {
            val query = "/search?query=DELETE%20FROM%20reservations%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

            val url = URL(ip.plus(query))

            val text = url.readText()

            Toast.makeText(this, "Reservation denied.", Toast.LENGTH_SHORT).show()

            finish()
        }
    }
}