package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.time_items.*

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
            TODO("add api calls for adding reservation")
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}