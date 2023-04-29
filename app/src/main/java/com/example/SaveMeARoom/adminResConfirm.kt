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

class adminResConfirm : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_res_confirm)

        val ip = "http://3.132.20.107:3000"
        val resInfo = intent.getStringExtra("res info")
        val resSplit = resInfo.toString().split(",")
        val buildingName = resSplit[0]
        val room = resSplit[1].substringAfter(" ")
        val email = resSplit[2].substringAfter(' ')
        var time = resSplit[4].substringAfter(" ")
        var date = resSplit[5].substringAfter(" ")
        var college = resSplit[6].substringAfter(" ")
        var club = "0"
        val modifiedTime = date + " " + (time.substringBefore("-").toInt() + 12).toString() + ":00:00"
        val flag = intent.getStringExtra("flag")

        var tvResConfirmText = findViewById<TextView>(R.id.tvResConfirmText)
        val btnYesResConfirm = findViewById<Button>(R.id.btnYesResConfirm)
        val btnNoResConfirm = findViewById<Button>(R.id.btnNoResConfirm)

        if(flag == "1"){
            tvResConfirmText.text = "Are you sure you want to accept this reservation?"
        }else{
            tvResConfirmText.text = "Are you sure you want to deny this reservation?"
        }

        btnYesResConfirm.setOnClickListener {
            if(flag == "1"){
                //get reservation ID of reservation
                var query = "/search?query=SELECT%20Reservation_Id%20FROM%20reservations%20WHERE%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27%20AND%20Club_Request=" + club

                var url = URL(ip.plus(query))

                var text = url.readText()

                var resId = text.substringAfter(":").substringAfter('"').substringBefore('"')

                // make sure that the reservation isn't being currently viewed
                query = "/search?query=SELECT%20Viewing%20FROM%20reservations%20WHERE%20Reservation_Id=" + resId

                url = URL(ip.plus(query))

                val view = url.readText().substringAfter(':').substringAfter('"').substringBefore('"').toInt()

                if(view == 0){
                    query = "/search?query=UPDATE%20reservations%20SET%20Pending=0%20WHERE%20Reservation_Id="+resId

                    url = URL(ip.plus(query))

                    text = url.readText()

                    if(club.equals("1")){
                        // see if there are any reservations at the same from non club leaders
                        query = "/search?query=SELECT%20Reservation_Id,Reserver_Email%20FROM%20reservations%20WHERE%20Club_Request=0%20AND%20Start_Date_Time=%27" + modifiedTime + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27"

                        url = URL(ip.plus(query))

                        text = url.readText()

                        // if there are any (should only be 1 at most), get rid of them
                        if(!(text.equals("[]"))){
                            // grab the resid and resemail of the reservation
                            var id = text.substringAfter(':').substringAfter('"').substringBefore('"')
                            var resEmail = text.substringAfter(',').substringAfter(':').substringAfter('"').substringBefore('"')

                            // delete the reservation
                            query = "/search?query=DELETE%20FROM%20reservations%20WHERE%20Reservation_Id=%27" + id + "%27"

                            url = URL(ip.plus(query))

                            text = url.readText()

                            // decrement the user's reservation count
                            query = "/search?query=SELECT%20Number_of_Reservations%20FROM%20users%20WHERE%20Email=%27" + resEmail + "%27"

                            url = URL(ip.plus(query))

                            text = url.readText()

                            var resnum = text.substringAfter(':').substringAfter('"').substringBefore('"').toInt()

                            resnum -= 1

                            query = "/search?query=UPDATE%20users%20SET%20Number_of_Reservations=" + resnum + "%20WHERE%20Email=%27" + resEmail + "%27"

                            url = URL(ip.plus(query))

                            text = url.readText()
                        }
                    }
                    else{
                        // see if there are any reservations at the same from non club leaders
                        query = "/search?query=SELECT%20Reservation_Id,Reserver_Email%20FROM%20reservations%20WHERE%20Club_Request=1%20AND%20Start_Date_Time=%27" + modifiedTime + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27"

                        url = URL(ip.plus(query))

                        text = url.readText()

                        // if there are any (should only be 1 at most), get rid of them
                        if(!(text.equals("[]"))){
                            // grab the resid and resemail of the reservation
                            var id = text.substringAfter(':').substringAfter('"').substringBefore('"')
                            var resEmail = text.substringAfter(',').substringAfter(':').substringAfter('"').substringBefore('"')

                            // delete the reservation
                            query = "/search?query=DELETE%20FROM%20reservations%20WHERE%20Reservation_Id=%27" + id + "%27"

                            url = URL(ip.plus(query))

                            text = url.readText()

                            // decrement the user's reservation count
                            query = "/search?query=SELECT%20Number_of_Reservations%20FROM%20users%20WHERE%20Email=%27" + resEmail + "%27"

                            url = URL(ip.plus(query))

                            text = url.readText()

                            var resnum = text.substringAfter(':').substringAfter('"').substringBefore('"').toInt()

                            resnum -= 1

                            query = "/search?query=UPDATE%20users%20SET%20Number_of_Reservations=" + resnum + "%20WHERE%20Email=%27" + resEmail + "%27"

                            url = URL(ip.plus(query))

                            text = url.readText()
                        }
                    }
                    //log accept
                    val curTime = LocalTime.now()
                    val curDate = LocalDate.now()
                    val logTime = curTime.toString() + " " + curDate.toString()

                    //getting initial reservation time
                    query =
                        "/search?query=SELECT%20Start_Date_Time,End_Date_Time%20FROM%20reservations%20WHERE%20Reservation_Id=%27" + resId + "%27"

                    url = URL(ip.plus(query))

                    text = url.readText()

                    val times = text.split(",")

                    val initialDate = times[0].substringAfter(":").substringAfter("\"").substringBefore(" ")

                    val initialStart = times[0].substringAfter(":").substringAfter("\"").substringBefore("\"").substringAfter(" ")

                    val initialEnd = times[1].substringAfter(":").substringAfter("\"").substringBefore("\"").substringAfter(" ")

                    val resTime = initialDate + " " + initialStart + " - " + initialDate + " " + initialEnd

                    val adminEmail = intent.getStringExtra("email")

                    //inserting into logs
                    query =
                        "/search?query=INSERT%20INTO%20reservationlogs%20VALUES(%27" + resId + "%27,%27" + buildingName + "%27,%27" + room + "%27,%27" + resTime + "%27,%27" + email + "%27,%27" + college + "%27,%27" + logTime + "%27,%27" + adminEmail + "%27,%27False%27,%27False%27,%27False%27,%27True%27,%27False%27,%27False%27,%27False%27)"

                    url = URL(ip.plus(query))

                    text = url.readText()

                    Toast.makeText(this, "Reservation accepted.", Toast.LENGTH_SHORT).show()

                    finish()
                }
                else{
                    Toast.makeText(this, "Reservation is currently being viewed, please try again later.", Toast.LENGTH_SHORT).show()
                }
            }else{
                //get reservation ID of reservation
                var query = "/search?query=SELECT%20Reservation_Id%20FROM%20reservations%20WHERE%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

                var url = URL(ip.plus(query))

                var text = url.readText()

                var resId = text.substringAfter(":").substringAfter("\"").substringBefore("\"")

                // make sure that the reservation isn't being currently viewed
                query = "/search?query=SELECT%20Viewing%20FROM%20reservations%20WHERE%20Reservation_Id=" + resId

                url = URL(ip.plus(query))

                val view = url.readText().substringAfter(':').substringAfter('"').substringBefore('"').toInt()

                if(view == 0){
                    //log deny
                    val curTime = LocalTime.now()
                    val curDate = LocalDate.now()
                    val logTime = curTime.toString() + " " + curDate.toString()

                    //getting initial reservation time
                    query =
                        "/search?query=SELECT%20Start_Date_Time,End_Date_Time%20FROM%20reservations%20WHERE%20Reservation_Id=%27" + resId + "%27"

                    url = URL(ip.plus(query))

                    text = url.readText()

                    val times = text.split(",")

                    val initialDate = times[0].substringAfter(":").substringAfter("\"").substringBefore(" ")

                    val initialStart = times[0].substringAfter(":").substringAfter("\"").substringBefore("\"").substringAfter(" ")

                    val initialEnd = times[1].substringAfter(":").substringAfter("\"").substringBefore("\"").substringAfter(" ")

                    val resTime = initialDate + " " + initialStart + " - " + initialDate + " " + initialEnd

                    val adminEmail = intent.getStringExtra("email")

                    //inserting into logs
                    query =
                        "/search?query=INSERT%20INTO%20reservationlogs%20VALUES(%27" + resId + "%27,%27" + buildingName + "%27,%27" + room + "%27,%27" + resTime + "%27,%27" + email + "%27,%27" + college + "%27,%27" + logTime + "%27,%27" + adminEmail + "%27,%27False%27,%27False%27,%27False%27,%27False%27,%27True%27,%27False%27,%27False%27)"

                    url = URL(ip.plus(query))

                    text = url.readText()
                    //deleting reservation from reservations table
                    query = "/search?query=DELETE%20FROM%20reservations%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

                    url = URL(ip.plus(query))

                    text = url.readText()

                    //getting accurate number of reservations for the user
                    query = "/search?query=SELECT%20Number_of_Reservations%20FROM%20users%20WHERE%20Email=%27" + email + "%27"

                    url = URL(ip.plus(query))

                    text = url.readText()

                    var numRes = text.substringAfter(":").substringAfter("\"").substringBefore("\"").toInt()

                    numRes -= 1

                    //subtracting 1 from number of reservations
                    query = "/search?query=UPDATE%20users%20SET%20Number_of_Reservations=" + numRes + "%20WHERE%20Email=%27" + email + "%27"

                    url = URL(ip.plus(query))

                    text = url.readText()

                    Toast.makeText(this, "Reservation denied.", Toast.LENGTH_SHORT).show()

                    finish()
                }
                else{
                    Toast.makeText(this, "Reservation is currently being viewed, please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnNoResConfirm.setOnClickListener {
            finish()
        }
    }
}