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

class BookConfirmation : AppCompatActivity() {
    private lateinit var infoList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_confirmation)

        val buildingName = intent.getStringExtra("building name")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        var occupancy = intent.getStringExtra("occupancy")
        val room = intent.getStringExtra("room")
        val username = intent.getStringExtra("username")

        val tvBuildingNameConfirm = findViewById<TextView>(R.id.tvBuildingNameConfirm)
        val tvDateConfirm = findViewById<TextView>(R.id.tvDateConfirm)
        val tvTimeConfirm = findViewById<TextView>(R.id.tvTimeConfirm)
        val tvOccupancyConfirm = findViewById<TextView>(R.id.tvOccupancyConfirm)
        val tvRoomConfirm = findViewById<TextView>(R.id.tvRoomConfirm)

        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        val ip = "http://3.132.20.107:3000"

        var query = "/search?query=SELECT%20*%20FROM%20users%20WHERE%20Username=%27" + username + "%27"

        var url = URL(ip.plus(query))

        var text = url.readText()

        infoList = arrayListOf()

        val userInfo = text.split(",")
        for (i in userInfo.indices){
            val info = userData(userInfo[i].substringAfter(":").substringAfter('"').substringBefore('"')).toString()
            val info2 = info.substringAfter("=").substringBefore(")")
            infoList.add(info2)
        }

        var email = infoList[2].toString()
        var resNum = infoList[7].toString().toInt()

        tvBuildingNameConfirm.text = buildingName
        tvDateConfirm.text = date
        tvTimeConfirm.text = time
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

        btnConfirm.setOnClickListener {
            if(resNum < 3) {
                val buildingName = intent.getStringExtra("building name")
                val date = intent.getStringExtra("date")
                val time = intent.getStringExtra("time")
                val college = intent.getStringExtra("college")
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
                val occupancy = intent.getStringExtra("occupancy")
                val room = intent.getStringExtra("room")
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

                StrictMode.setThreadPolicy(policy)

                query =
                    "/search?query=INSERT%20INTO%20reservations%20VALUES(%27" + buildingName + "%27," + room + ",%27" + email + "%27," + occupancy + ",%27" + date + "%20" + start + "%27,%27" + date + "%20" + end + "%27, 1, %27"+college+"%27, 1)"

                url = URL(ip.plus(query))

                text = url.readText()

                Toast.makeText(this, "Reservation successful!", Toast.LENGTH_SHORT).show()

                resNum += 1

                query =
                    "/search?query=UPDATE%20users%20SET%20Number_of_Reservations=%27"+resNum+"%27%20WHERE%20Username=%27"+username+"%27%20AND%20Email=%27"+email+"%27"

                url = URL(ip.plus(query))

                text = url.readText()

                finish()
            } else {
                Toast.makeText(this, "You have reached the maximum number of reservations.", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}