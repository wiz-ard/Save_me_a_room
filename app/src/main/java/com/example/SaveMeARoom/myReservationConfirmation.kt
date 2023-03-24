package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.book_confirmation.*
import kotlinx.android.synthetic.main.my_reservation_confirmation.*
import java.net.URL

class myReservationConfirmation : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_reservation_confirmation)
        //pulls reservation information
        val resInfo = intent.getStringExtra("reservation info")
        val email = intent.getStringExtra("email")
        //splits reservation info and assigns it to specific variables
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
        //gets occupancy range
        var query = "/search?query=SELECT%20Occupancy_Range%20FROM%20reservations%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

        var url = URL(ip.plus(query))

        var text = url.readText()

        val querysplit = text.split(",")

        occupancy = querysplit[0].substringAfter(":").substringAfter("\"").substringBefore("\"")
        //formats occupancy
        if(occupancy.equals("0")){
            occupancy = "2-10"
        }else if(occupancy.equals("1")){
            occupancy = "11-29"
        }else if(occupancy.equals("2")){
            occupancy = "30-49"
        }else{
            occupancy = "50+"
        }
        //gets reservation ID of reservation
        query = "/search?query=SELECT%20Reservation_Id%20FROM%20reservations%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

        url = URL(ip.plus(query))

        text = url.readText()

        val resId = text.substringAfter(":").substringAfter("\"").substringBefore("\"")
        //sets variables for TextViews
        val tvMyBuildingName = findViewById<TextView>(R.id.tvMyBuildingNameConfirm)
        val tvMyDate = findViewById<TextView>(R.id.tvMyDateConfirm)
        val tvMyTime = findViewById<TextView>(R.id.tvMyTimeConfirm)
        val tvMyOccupancy = findViewById<TextView>(R.id.tvMyOccupancyConfirm)
        val tvMyRoom = findViewById<TextView>(R.id.tvMyRoomConfirm)

        val btnMyCancel = findViewById<Button>(R.id.btnMyCancel)
        //sets text for TextViews
        tvMyBuildingName.text = buildingName
        tvMyDate.text = date
        tvMyTime.text = time

        tvMyOccupancy.text = "Occupancy: " + occupancy
        tvMyRoom.text = "Room: " + room

        btnMyCancel.setOnClickListener {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

            StrictMode.setThreadPolicy(policy)

            val ip = "http://3.132.20.107:3000"
            //deletes reservation from database
            var query = "/search?query=DELETE%20FROM%20reservations%20WHERE%20Reservation_Id=%27" + resId + "%27"

            var url = URL(ip.plus(query))

            var text = url.readText()
            //gets users number of reservations
            query = "/search?query=SELECT%20Number_of_Reservations%20FROM%20users%20WHERE%20Email=%27" + email + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            var numRes = text.substringAfter(":").substringAfter("\"").substringBefore("\"").toInt()

            numRes -= 1
            //updates number of reservations to the decremented value as the reservation was deleted
            query = "/search?query=UPDATE%20users%20SET%20Number_of_Reservations=" + numRes + "%20WHERE%20Email=%27" + email + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            Toast.makeText(this, "Reservation cancelled.", Toast.LENGTH_SHORT).show()

            finish()
        }
        btnMyUpdate.setOnClickListener {
            //gets updating value of reservation
            query = "/search?query=SELECT%20Updating%20FROM%20reservations%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            val updating = text.substringAfter(":").substringAfter("\"").substringBefore("\"")
            //gets pending value of reservation
            query = "/search?query=SELECT%20Pending%20FROM%20reservations%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            val pending = text.substringAfter(":").substringBefore("}")
            //if pending and updating are both 0, then send to UpdateDateSelectionPage
            if(updating.equals("0") && pending.equals("0")){
                val intent = Intent(this, UpdateDateSelection::class.java)
                intent.putExtra("building name", buildingName)
                intent.putExtra("occupancy", occupancy)
                intent.putExtra("room", room)
                intent.putExtra("email",email)
                intent.putExtra("oldtime",time)
                intent.putExtra("olddate",date)
                intent.putExtra("resId",resId)
                startActivity(intent)
                finish()
            //if either is 1, alert user they can't update
            }else{
                Toast.makeText(this, "Reservation is either pending or already being updated.", Toast.LENGTH_SHORT).show()

            }
        }
    }
}