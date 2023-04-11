package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.time_items.*
import java.net.URL

class UpdateConfirmation : AppCompatActivity() {
    private lateinit var infoList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_confirmation)


        val buildingName = intent.getStringExtra("building name")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        var occupancy = intent.getStringExtra("occupancy")
        val room = intent.getStringExtra("room")
        val resId = intent.getStringExtra("resId")
        val pending = intent.getStringExtra("pending")
        val updating = intent.getStringExtra("updating")

        val tvBuildingNameConfirm = findViewById<TextView>(R.id.tvBuildingNameConfirm)
        val tvDateConfirm = findViewById<TextView>(R.id.tvDateConfirm)
        val tvTimeConfirm = findViewById<TextView>(R.id.tvTimeConfirm)
        val tvOccupancyConfirm = findViewById<TextView>(R.id.tvOccupancyConfirm)
        val tvRoomConfirm = findViewById<TextView>(R.id.tvRoomConfirm)

        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        val ip = "http://3.132.20.107:3000"

        infoList = arrayListOf()

        tvBuildingNameConfirm.text = buildingName
        tvDateConfirm.text = date
        tvTimeConfirm.text = time
        tvOccupancyConfirm.text = "Occupancy: " + occupancy
        tvRoomConfirm.text = "Room: " + room

        btnConfirm.setOnClickListener {
            var start = ""
            var end = ""
            if (time.equals("5:00pm - 7:00pm")) {
                start = "17:00:00"
                end = "19:00:00"
            } else if (time.equals("7:00pm - 9:00pm")) {
                start = "19:00:00"
                end = "21:00:00"
            } else {
                start = "21:00:00"
                end = "23:00:00"
            }

            if(pending.equals("0") && updating.equals("0")){
                //insert request to updates table
                var query = "/search?query=INSERT%20INTO%20updates%20VALUES(" + resId + ",%27" + date + "%20" + start + "%27,%27" + date + "%20" + end + "%27)"

                var url = URL(ip.plus(query))

                var text = url.readText()

                //set updating to 1
                query = "/search?query=UPDATE%20reservations%20SET%20Updating=1%20WHERE%20Reservation_Id=" + resId + ""
                url = URL(ip.plus(query))
                text = url.readText()
                Toast.makeText(this, "Update request sent.", Toast.LENGTH_SHORT).show()

                // set viewing to false
                query = "/search?query=UPDATE%20reservations%20SET%20Viewing=0%20WHERE%20Reservation_Id=" + resId

                url = URL(ip.plus(query))

                url.readText()

                finish()
            }else if(pending.equals("1") && updating.equals("0")){
                //modify time of pending request
                var query = "/search?query=UPDATE%20reservations%20SET%20Start_Date_Time=%27" + date + "%20" + start + "%27,End_Date_Time=%27" + date + "%20" + end + "%27%20WHERE%20Reservation_Id=" + resId + ""

                var url = URL(ip.plus(query))

                var text = url.readText()

                Toast.makeText(this, "Pending request updated.", Toast.LENGTH_SHORT).show()

                // set viewing to false
                query = "/search?query=UPDATE%20reservations%20SET%20Viewing=0%20WHERE%20Reservation_Id=" + resId

                url = URL(ip.plus(query))

                url.readText()

                finish()
            }else if(pending.equals("0") && updating.equals("1")){
                //modify update request
                var query = "/search?query=UPDATE%20updates%20SET%20New_Start_Time=%27" + date + "%20" + start + "%27,New_End_Time=%27" + date + "%20" + end + "%27%20WHERE%20Reservation_Id=" + resId + ""

                var url = URL(ip.plus(query))

                var text = url.readText()

                Toast.makeText(this, "Update request changed.", Toast.LENGTH_SHORT).show()

                // set viewing to false
                query = "/search?query=UPDATE%20reservations%20SET%20Viewing=0%20WHERE%20Reservation_Id=" + resId

                url = URL(ip.plus(query))

                url.readText()

                finish()
            }
        }

        btnCancel.setOnClickListener {
            // set viewing to false
            var query = "/search?query=UPDATE%20reservations%20SET%20Viewing=0%20WHERE%20Reservation_Id=" + resId

            var url = URL(ip.plus(query))

            url.readText()
            finish()
        }
    }
}