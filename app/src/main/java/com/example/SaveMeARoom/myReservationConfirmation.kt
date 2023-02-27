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

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"

        //val query = "/search?query=SELECT%20DISTINCT%20Building_Name%20FROM%20locations%20WHERE%20Associated_College=%27" + college + "%27%20OR%20Associated_College=%27General%27%20ORDER%20BY%20Building_Name"

        //val url = URL(ip.plus(query))

        //val text = url.readText()

        val buildingName = null
        val date = null
        val time = null
        val occupancy = null
        val room = null

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


            finish()
        }

    }
}