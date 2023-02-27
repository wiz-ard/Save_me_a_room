package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.book_confirmation.*
import java.net.URL

class myReservationConfirmation : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_reservation_confirmation)



        val resInfo = intent.getStringExtra("reservation info")
        val email = intent.getStringExtra("email")

        val splitRes = resInfo.toString().split(",")

        val buildingName = splitRes[0]
        var date = splitRes[3]
        val time = splitRes[2]
        val modifiedTime = date + " " + (time.substringAfter(" ").substringBefore("pm").toInt()+12).toString()+ ":00:00"
        var occupancy = ""
        val room = splitRes[1].substringAfter(" ")

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"

        val query = "/search?query=SELECT%20Occupancy_Range%20FROM%20reservations%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

        val url = URL(ip.plus(query))

        val text = url.readText()

        val querysplit = text.split(",")

        occupancy = querysplit[0].substringAfter(":").substringAfter("\"").substringBefore("\"")
        if(occupancy.equals("0")){
            occupancy = "2-10"
        }else if(occupancy.equals("1")){
            occupancy = "11-29"
        }else if(occupancy.equals("2")){
            occupancy = "30-49"
        }else{
            occupancy = "50+"
        }

        val tvMyBuildingName = findViewById<TextView>(R.id.tvMyBuildingNameConfirm)
        val tvMyDate = findViewById<TextView>(R.id.tvMyDateConfirm)
        val tvMyTime = findViewById<TextView>(R.id.tvMyTimeConfirm)
        val tvMyOccupancy = findViewById<TextView>(R.id.tvMyOccupancyConfirm)
        val tvMyRoom = findViewById<TextView>(R.id.tvMyRoomConfirm)

        val btnMyCancel = findViewById<Button>(R.id.btnMyCancel)

        tvMyBuildingName.text = buildingName
        tvMyDate.text = date
        tvMyTime.text = time

        tvMyOccupancy.text = "Occupancy: " + occupancy
        tvMyRoom.text = "Room: " + room

        btnMyCancel.setOnClickListener {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

            StrictMode.setThreadPolicy(policy)

            val ip = "http://3.132.20.107:3000"

            val query = "/search?query=DELETE%20FROM%20reservations%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

            val url = URL(ip.plus(query))

            val text = url.readText()

            finish()
        }

    }
}