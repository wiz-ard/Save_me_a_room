package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.time_items.*
import java.net.URL

class BookConfirmation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_confirmation)

        val buildingName = intent.getStringExtra("building name")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val occupancy = intent.getStringExtra("occupancy")
        val room = intent.getStringExtra("room")

        val tvBuildingNameConfirm = findViewById<TextView>(R.id.tvBuildingNameConfirm)
        val tvDateConfirm = findViewById<TextView>(R.id.tvDateConfirm)
        val tvTimeConfirm = findViewById<TextView>(R.id.tvTimeConfirm)
        val tvOccupancyConfirm = findViewById<TextView>(R.id.tvOccupancyConfirm)
        val tvRoomConfirm = findViewById<TextView>(R.id.tvRoomConfirm)

        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        tvBuildingNameConfirm.text = buildingName
        tvDateConfirm.text = date
        tvTimeConfirm.text = time
        tvOccupancyConfirm.text = occupancy
        tvRoomConfirm.text = room

        btnConfirm.setOnClickListener {
            val buildingName = intent.getStringExtra("building name")
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
            val room = intent.getStringExtra("room")
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

            StrictMode.setThreadPolicy(policy)

            val ip = "http://3.132.20.107:3000"

            val query = "/search?query=INSERT%20INTO%20reservations%20VALUES(%27" + buildingName + "%27," + room + ",%27email@email.com%27," + occupancy + ",%27" + date + "%20" + start + "%27,%27" + date + "%20" + end + "%27)"

            val url = URL(ip.plus(query))

            val text = url.readText()

            Toast.makeText(this, "Reservation successful!", Toast.LENGTH_SHORT).show()

            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}