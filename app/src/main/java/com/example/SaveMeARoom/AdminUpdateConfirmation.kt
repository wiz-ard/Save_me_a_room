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
        val adminEmail = intent.getStringExtra("adminemail")
        val resInfo = intent.getStringExtra("res info")
        val club = intent.getStringExtra("club")

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
            val intent = Intent(this, adminResUpdateConfirm::class.java)

            val modifiedTime = (time.substringBefore("-").toInt() + 12).toString() + ":00:00"

            val query = "/search?query=SELECT%20Viewing%20FROM%20reservations%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Start_Date_Time=%27" + date + " " + modifiedTime + "%27"

            val url = URL(ip.plus(query))

            val view = url.readText().substringAfter(":").substringAfter('"').substringBefore('"')

            if(view == "0"){
                val query = "/search?query=UPDATE%20reservations%20SET%20Viewing=1%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Start_Date_Time=%27" + date + " " + modifiedTime + "%27"

                val url = URL(ip.plus(query))

                url.readText()

                intent.putExtra("res info", resInfo)
                intent.putExtra("adminemail",adminEmail)
                intent.putExtra("flag","1")
                intent.putExtra("club", club)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Another user is viewing this reservation at this time.", Toast.LENGTH_SHORT).show()
            }
        }

        btnDeny.setOnClickListener {
            val intent = Intent(this, adminResUpdateConfirm::class.java)

            val modifiedTime = (time.substringBefore("-").toInt() + 12).toString() + ":00:00"

            val query = "/search?query=SELECT%20Viewing%20FROM%20reservations%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Start_Date_Time=%27" + date + " " + modifiedTime + "%27"

            val url = URL(ip.plus(query))

            val view = url.readText().substringAfter(":").substringAfter('"').substringBefore('"')

            if(view == "0"){
                val query = "/search?query=UPDATE%20reservations%20SET%20Viewing=1%20WHERE%20Reserver_Email=%27" + email + "%27%20AND%20Start_Date_Time=%27" + date + " " + modifiedTime + "%27"

                val url = URL(ip.plus(query))

                url.readText()

                intent.putExtra("res info", resInfo)
                intent.putExtra("adminemail",adminEmail)
                intent.putExtra("flag","0")
                intent.putExtra("club", club)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Another user is viewing this reservation at this time.", Toast.LENGTH_SHORT).show()
            }
        }
        btnCancel.setOnClickListener{
            finish()
        }
    }
}