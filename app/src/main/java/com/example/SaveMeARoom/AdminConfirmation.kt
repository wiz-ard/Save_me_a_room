package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        if(resSplit.size == 9){
            if(resSplit[7].substringAfter(" ").equals("Club Request") || resSplit[8].substringAfter(" ").equals("Club Request")){
                club = "1"
            }
            if(resSplit[7].substringAfter(" ").equals("Update Request")){
                update = "1"
            }
        }
        else if(resSplit.size >= 8){
            if(resSplit[7].substringAfter(" ").equals("Club Request")){
                club = "1"
            }
            else if(resSplit[7].substringAfter(" ").equals("Update Request")){
                update = "1"
            }
        }
        val modifiedTime = date + " " + (time.substringBefore("-").toInt() + 12).toString() + ":00:00"

        //get reservation ID of reservation
        var query = "/search?query=SELECT%20Reservation_Id%20FROM%20reservations%20WHERE%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27%20AND%20Reserver_Email=%27" + email + "%27"

        var url = URL(ip.plus(query))

        var text = url.readText()

        var resId = text.substringAfter(":").substringAfter('"').substringBefore('"')

        var newtime = "null"
        var newdate = "null"
        if(update.equals("1")){
            //check if its an update
            query = "/search?query=SELECT%20New_Start_Time,New_End_Time%20FROM%20updates%20WHERE%20Reservation_Id=%27" + resId + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            var times = text.split(",")
            var newStart = times[0].substringAfter(":").substringAfter('"').substringBefore('"')
            var newEnd = times[1].substringAfter(":").substringAfter('"').substringBefore('"')
            newdate = newStart.substringBefore(" ")
            newtime = ((newStart.substringAfter(" ").substringBefore(":").toInt())-12).toString() + "-" + ((newEnd.substringAfter(" ").substringBefore(":").toInt())-12).toString() + "pm"
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

        tvBuildingNameConfirm.text = buildingName
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

        if(newdate.equals("null")){
            tvDateConfirm.text = date
            tvTimeConfirm.text = time
        }else{
            val intent = Intent(this, AdminUpdateConfirmation::class.java)
            intent.putExtra("building",buildingName)
            intent.putExtra("time",time)
            intent.putExtra("date",date)
            intent.putExtra("newtime",newtime)
            intent.putExtra("newdate",newdate)
            intent.putExtra("occupancy",occupancy)
            intent.putExtra("room",room)
            intent.putExtra("email",email)
            intent.putExtra("college",college)
            intent.putExtra("adminemail",adminEmail)
            intent.putExtra("club", club)
            startActivity(intent)
            finish()
        }

        btnConfirm.setOnClickListener {
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
        }

        btnDeny.setOnClickListener {
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
}