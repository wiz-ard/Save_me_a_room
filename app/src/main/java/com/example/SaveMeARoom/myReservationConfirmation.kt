package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.book_confirmation.*

class myReservationConfirmation : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_reservation_confirmation)

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