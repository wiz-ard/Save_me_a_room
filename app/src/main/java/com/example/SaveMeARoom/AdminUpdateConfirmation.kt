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

class AdminUpdateConfirmation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_reservation_update_confirmation)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"
        //getting values from previous page
        val buildingName = intent.getStringExtra("building")
        var time = intent.getStringExtra("time").toString()
        var newtime = intent.getStringExtra("newtime")
        var date = intent.getStringExtra("date")
        var newdate = intent.getStringExtra("newdate")
        var room = intent.getStringExtra("room")
        var occupancy = intent.getStringExtra("occupancy")
        var email = intent.getStringExtra("email")
        val modifiedTime = date + " " + (time.substringAfter(" ").substringBefore("pm").toInt() + 12).toString() + ":00:00"

        //setting variables to the different fillable text fields
        val tvBuildingNameConfirm = findViewById<TextView>(R.id.tvAdminUpdateBuildingNameConfirm)
        val tvOldDateConfirm = findViewById<TextView>(R.id.tvAdminOldDateConfirm)
        val tvNewDateConfirm = findViewById<TextView>(R.id.tvAdminNewDateConfirm)
        val tvOldTimeConfirm = findViewById<TextView>(R.id.tvAdminOldTimeConfirm)
        val tvNewTimeConfirm = findViewById<TextView>(R.id.tvAdminNewTimeConfirm)
        val tvEmailConfirm = findViewById<TextView>(R.id.tvAdminUpdateEmailConfirm)
        val tvOccupancyConfirm = findViewById<TextView>(R.id.tvAdminUpdateOccupancyConfirm)
        val tvRoomConfirm = findViewById<TextView>(R.id.tvAdminUpdateRoomConfirm)

        val btnConfirm = findViewById<Button>(R.id.btnAdminUpdateConfirm)
        val btnDeny = findViewById<Button>(R.id.btnAdminUpdateDeny)
        //putting appropriate text on the screen
        tvBuildingNameConfirm.text = "Building: " + buildingName
        tvOldDateConfirm.text = "Old Date: " + date
        tvNewDateConfirm.text = "New Date: " + newdate
        tvOldTimeConfirm.text = "Old Time: " + time
        tvNewTimeConfirm.text = "New Time: " + newtime
        tvEmailConfirm.text = "Email: " + email
        tvOccupancyConfirm.text = "Occupancy: " + occupancy
        tvRoomConfirm.text = "Room: " + room

        btnConfirm.setOnClickListener {
            //get reservation ID of reservation
            var query = "/search?query=SELECT%20Reservation_Id%20FROM%20reservations%20WHERE%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

            var url = URL(ip.plus(query))

            var text = url.readText()

            var resId = text.substringAfter(":").substringAfter("\"").substringBefore("\"")

            //get updated times
            query = "/search?query=SELECT%20New_Start_Time,New_End_Time%20FROM%20updates%20WHERE%20Reservation_Id=%27" + resId + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            var times = text.split(",")

            var newStart = times[0].substringAfter(":").substringAfter("\"").substringBefore("\"")
            var newEnd = times[1].substringAfter(":").substringAfter("\"").substringBefore("\"")

            //delete from updates table
            query = "/search?query=DELETE%20FROM%20updates%20WHERE%20Reservation_Id=%27" + resId + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            //updating the time values to the new ones and set updating to 0
            query = "/search?query=UPDATE%20reservations%20SET%20Updating='0',Start_Date_Time=%27" + newStart + "%27,End_Date_Time=%27" + newEnd + "%27%20WHERE%20Reservation_Id=%27" + resId + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            Toast.makeText(this, "Update accepted.", Toast.LENGTH_SHORT).show()

            finish()
        }

        btnDeny.setOnClickListener {
            //get reservation ID of reservation
            var query = "/search?query=SELECT%20Reservation_Id%20FROM%20reservations%20WHERE%20Building_Name=%27" + buildingName + "%27%20AND%20Room_Number=%27" + room + "%27%20AND%20Start_Date_Time=%27" + modifiedTime + "%27"

            var url = URL(ip.plus(query))

            var text = url.readText()

            var resId = text.substringAfter(":").substringAfter("\"").substringBefore("\"")

            //deleting from updates table
            query = "/search?query=DELETE%20FROM%20updates%20WHERE%20Reservation_Id=%27" + resId + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            //set updating = 0 on original reservation
            query = "/search?query=UPDATE%20reservations%20SET%20Updating='0'%20WHERE%20Reservation_Id=%27" + resId + "%27"

            url = URL(ip.plus(query))

            text = url.readText()

            Toast.makeText(this, "Update denied.", Toast.LENGTH_SHORT).show()

            finish()
        }
    }
}