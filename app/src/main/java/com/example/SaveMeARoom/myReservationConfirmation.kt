package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        val college = intent.getStringExtra("college")
        //splits reservation info and assigns it to specific variables
        val splitRes = resInfo.toString().split(",")
        val buildingName = splitRes[0]
        var date = splitRes[3].substringBefore(" ")
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
        val btnMyBack = findViewById<FloatingActionButton>(R.id.btnBackMRC)
        //sets text for TextViews
        tvMyBuildingName.text = buildingName
        tvMyDate.text = date
        tvMyTime.text = time

        tvMyOccupancy.text = "Occupancy: " + occupancy
        tvMyRoom.text = "Room: " + room

        btnMyCancel.setOnClickListener {
            val intent = Intent(this, cancelConfirmation::class.java)
            // make sure that the reservation isn't being currently viewed
            query = "/search?query=SELECT%20Viewing%20FROM%20reservations%20WHERE%20Reservation_Id=" + resId

            url = URL(ip.plus(query))

            val view = url.readText().substringAfter(':').substringAfter('"').substringBefore('"').toInt()

            //if pending and updating are both 0, then send to UpdateDateSelectionPage
            if(view == 0) {
                // set the request as in progress
                query =
                    "/search?query=UPDATE%20reservations%20SET%20Viewing=1%20WHERE%20Reservation_Id=" + resId

                url = URL(ip.plus(query))

                url.readText()

                intent.putExtra("resid", resId)
                intent.putExtra("email", email)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Reservation is currently being viewed, please try again later.", Toast.LENGTH_SHORT).show()
            }
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

            val pending = text.substringAfter(":").substringAfter('"').substringBefore('"')

            // make sure that the reservation isn't being currently viewed
            query = "/search?query=SELECT%20Viewing%20FROM%20reservations%20WHERE%20Reservation_Id=" + resId

            url = URL(ip.plus(query))

            val view = url.readText().substringAfter(':').substringAfter('"').substringBefore('"').toInt()

            //if pending and updating are both 0, then send to UpdateDateSelectionPage
            if(view == 0){
                // set the request as in progress
                query = "/search?query=UPDATE%20reservations%20SET%20Viewing=1%20WHERE%20Reservation_Id=" + resId

                url = URL(ip.plus(query))

                url.readText()

                // go to the next page
                val intent = Intent(this, UpdateDateSelection::class.java)
                intent.putExtra("building name", buildingName)
                intent.putExtra("occupancy", occupancy)
                intent.putExtra("room", room)
                intent.putExtra("college", college)
                intent.putExtra("email",email)
                intent.putExtra("oldtime",time)
                intent.putExtra("olddate",date)
                intent.putExtra("resId",resId)
                intent.putExtra("pending",pending)
                intent.putExtra("updating",updating)
                startActivity(intent)
                finish()
            }
            else{
                // inform the user they can't change the request
                Toast.makeText(this, "Reservation is currently being viewed, please try again later.", Toast.LENGTH_SHORT).show()
            }
        }

        btnMyBack.setOnClickListener{
            finish()
        }
    }
}