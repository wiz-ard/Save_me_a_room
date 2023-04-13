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
import java.time.LocalDate
import java.time.LocalTime

class cancelConfirmation : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cancel_confirmation)
        //pulls reservation information
        val resId = intent.getStringExtra("resid")
        val email = intent.getStringExtra("email")

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"

        val btnYesCancel = findViewById<Button>(R.id.btnYesCancel)
        val btnNoCancel = findViewById<Button>(R.id.btnNoCancel)

        btnYesCancel.setOnClickListener {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

            StrictMode.setThreadPolicy(policy)

            //gets updating value of reservation
            val query = "/search?query=SELECT%20Updating%20FROM%20reservations%20WHERE%20Reservation_Id=%27" + resId + "%27"

            val url = URL(ip.plus(query))

            val text = url.readText()

            val updating = text.substringAfter(":").substringAfter("\"").substringBefore("\"")

            if(updating.equals("1")){
                val ip = "http://3.132.20.107:3000"
                //log cancel


                //deletes reservation from updates database
                var query = "/search?query=DELETE%20FROM%20updates%20WHERE%20Reservation_Id=%27" + resId + "%27"

                var url = URL(ip.plus(query))

                var text = url.readText()
                //set updating = 0
                query = "/search?query=UPDATE%20reservations%20SET%20Updating='0'%20WHERE%20Reservation_Id=%27" + resId + "%27"

                url = URL(ip.plus(query))

                text = url.readText()

                Toast.makeText(this, "Update request cancelled.", Toast.LENGTH_SHORT).show()

                //log cancel
                val curTime = LocalTime.now()
                val curDate = LocalDate.now()
                val logTime = curTime.toString() + " " + curDate.toString()

                //getting reservation info
                query = "/search?query=SELECT%20Building_Name,%20Room_Number,%20College,%20Start_Date_Time,%20End_Date_Time%20FROM%20Capstone.reservations%20WHERE%20Reservation_Id=%27" + resId + "%27"

                url = URL(ip.plus(query))

                text = url.readText()

                val resInfo = text.split(",")

                val buildingName = resInfo[0].substringAfter(":").substringAfter("\"").substringBefore("\"")

                val room = resInfo[1].substringAfter(":").substringAfter("\"").substringBefore("\"")

                val college = resInfo[2].substringAfter(":").substringAfter("\"").substringBefore("\"")

                val initialDate = resInfo[3].substringAfter(":").substringAfter("\"").substringBefore(" ")

                val initialStart = resInfo[3].substringAfter(":").substringAfter("\"").substringBefore("\"").substringAfter(" ")

                val initialEnd = resInfo[4].substringAfter(":").substringAfter("\"").substringBefore("\"").substringAfter(" ")

                val resTime = initialDate + " " + initialStart + " - " + initialDate + " " + initialEnd

                //inserting into logs
                query =
                    "/search?query=INSERT%20INTO%20reservationlogs%20VALUES(%27" + resId + "%27,%27" + buildingName + "%27,%27" + room + "%27,%27" + resTime + "%27,%27" + email + "%27,%27" + college + "%27,%27" + logTime + "%27,%27" + email + "%27,%27False%27,%27False%27,%27True%27,%27False%27,%27False%27,%27False%27,%27False%27)"

                url = URL(ip.plus(query))

                text = url.readText()

                finish()
            }else{
                val ip = "http://3.132.20.107:3000"
                //log cancel
                val curTime = LocalTime.now()
                val curDate = LocalDate.now()
                val logTime = curTime.toString() + " " + curDate.toString()

                //getting reservation info
                var query = "/search?query=SELECT%20Building_Name,%20Room_Number,%20College,%20Start_Date_Time,%20End_Date_Time%20FROM%20Capstone.reservations%20WHERE%20Reservation_Id=%27" + resId + "%27"

                var url = URL(ip.plus(query))

                var text = url.readText()

                val resInfo = text.split(",")

                val buildingName = resInfo[0].substringAfter(":").substringAfter("\"").substringBefore("\"")

                val room = resInfo[1].substringAfter(":").substringAfter("\"").substringBefore("\"")

                val college = resInfo[2].substringAfter(":").substringAfter("\"").substringBefore("\"")

                val initialDate = resInfo[3].substringAfter(":").substringAfter("\"").substringBefore(" ")

                val initialStart = resInfo[3].substringAfter(":").substringAfter("\"").substringBefore("\"").substringAfter(" ")

                val initialEnd = resInfo[4].substringAfter(":").substringAfter("\"").substringBefore("\"").substringAfter(" ")

                val resTime = initialDate + " " + initialStart + " - " + initialDate + " " + initialEnd

                //inserting into logs
                query =
                "/search?query=INSERT%20INTO%20reservationlogs%20VALUES(%27" + resId + "%27,%27" + buildingName + "%27,%27" + room + "%27,%27" + resTime + "%27,%27" + email + "%27,%27" + college + "%27,%27" + logTime + "%27,%27" + email + "%27,%27False%27,%27False%27,%27True%27,%27False%27,%27False%27,%27False%27,%27False%27)"

                url = URL(ip.plus(query))

                text = url.readText()
                //deletes reservation from reservation database
                query = "/search?query=DELETE%20FROM%20reservations%20WHERE%20Reservation_Id=%27" + resId + "%27"

                url = URL(ip.plus(query))

                text = url.readText()
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
        }
        btnNoCancel.setOnClickListener {
            // set viewing to false
            val ip = "http://3.132.20.107:3000"

            var query = "UPDATE%20reservations%20SET%20Viewing=0%20WHERE%20Reservation_Id=" + resId

            var url = URL(ip.plus(query))

            url.readText()
            finish()
        }
    }
}