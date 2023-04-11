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
        //getting values from previous page
        val buildingName = intent.getStringExtra("building name")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        var occupancy = intent.getStringExtra("occupancy")
        val room = intent.getStringExtra("room")
        val username = intent.getStringExtra("username")

        //setting variables for different textvies on page
        val tvBuildingNameConfirm = findViewById<TextView>(R.id.tvBuildingNameConfirm)
        val tvDateConfirm = findViewById<TextView>(R.id.tvDateConfirm)
        val tvTimeConfirm = findViewById<TextView>(R.id.tvTimeConfirm)
        val tvOccupancyConfirm = findViewById<TextView>(R.id.tvOccupancyConfirm)
        val tvRoomConfirm = findViewById<TextView>(R.id.tvRoomConfirm)

        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        val ip = "http://3.132.20.107:3000"
        //querying for user information to access their email and the reservation number
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

        var email = infoList[2]
        var resNum = infoList[7].toInt()

        tvBuildingNameConfirm.text = buildingName
        tvDateConfirm.text = date
        tvTimeConfirm.text = time
        //formatting occupancy
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
            query = "/search?query=SELECT%20Club_Leader%20FROM%20users%20WHERE%20email=%27" + email + "%27"
            url = URL(ip.plus(query))
            text = url.readText()
            var club = text.substringAfter(":").substringAfter('"').substringBefore('"').toInt()
            // checks if you're a club leader
            if(club == 0){
                //checks if you have reservations available
                if(resNum < 3) {
                    val buildingName = intent.getStringExtra("building name")
                    val date = intent.getStringExtra("date")
                    val time = intent.getStringExtra("time")
                    val college = intent.getStringExtra("college")
                    var start = ""
                    var end = ""
                    //formatting the time
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
                    //gets reservation ID based on given information
                    query = "/search?query=SELECT%20Reservation_Id%20FROM%20reservations%20ORDER%20BY%20Reservation_Id%20DESC%20LIMIT%201"

                    url = URL(ip.plus(query))

                    text = url.readText()
                    //default reservation ID
                    var resId = 1
                    //if there are reservations, make resID +1 from highest resID in database
                    if(text.length == 2){

                    }else{
                        resId = text.substringAfter(":").substringAfter("\"").substringBefore("\"").toInt()
                        resId += 1
                    }
                    //inserts reservation into database
                    query ="/search?query=INSERT%20INTO%20reservations%20VALUES(%27" + buildingName + "%27," + room + ",%27" + email + "%27," + occupancy + ",%27" + date + "%20" + start + "%27,%27" + date + "%20" + end + "%27, 1, %27"+college+"%27,%27" + resId + "%27,0,%20%27" + club + "%27,0)"

                    url = URL(ip.plus(query))

                    text = url.readText()

                    Toast.makeText(this, "Reservation successful!", Toast.LENGTH_SHORT).show()

                    resNum += 1
                    //updates user's number of reservations
                    query =
                        "/search?query=UPDATE%20users%20SET%20Number_of_Reservations=%27"+resNum+"%27%20WHERE%20Username=%27"+username+"%27%20AND%20Email=%27"+email+"%27"

                    url = URL(ip.plus(query))

                    text = url.readText()

                    finish()
                } else {
                    Toast.makeText(this, "You have reached the maximum number of reservations.", Toast.LENGTH_SHORT).show()
                }
            } else {
                //checks if you have reservations available
                if(resNum < 6) {
                    val buildingName = intent.getStringExtra("building name")
                    val date = intent.getStringExtra("date")
                    val time = intent.getStringExtra("time")
                    val college = intent.getStringExtra("college")
                    var start = ""
                    var end = ""
                    //formatting the time
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
                    //gets reservation ID based on given information
                    query = "/search?query=SELECT%20Reservation_Id%20FROM%20reservations%20ORDER%20BY%20Reservation_Id%20DESC%20LIMIT%201"

                    url = URL(ip.plus(query))

                    text = url.readText()
                    //default reservation ID
                    var resId = 1
                    //if there are reservations, make resID +1 from highest resID in database
                    if(text.length == 2){

                    }else{
                        resId = text.substringAfter(":").substringAfter("\"").substringBefore("\"").toInt()
                        resId += 1
                    }
                    //inserts reservation into database
                    query ="/search?query=INSERT%20INTO%20reservations%20VALUES(%27" + buildingName + "%27," + room + ",%27" + email + "%27," + occupancy + ",%27" + date + "%20" + start + "%27,%27" + date + "%20" + end + "%27, 1, %27"+college+"%27,%27" + resId + "%27,0,%20%27" + club + "%27,0)"

                    url = URL(ip.plus(query))

                    text = url.readText()

                    Toast.makeText(this, "Reservation successful!", Toast.LENGTH_SHORT).show()

                    resNum += 1
                    //updates user's number of reservations
                    query =
                        "/search?query=UPDATE%20users%20SET%20Number_of_Reservations=%27"+resNum+"%27%20WHERE%20Username=%27"+username+"%27%20AND%20Email=%27"+email+"%27"

                    url = URL(ip.plus(query))

                    text = url.readText()

                    finish()
                } else {
                    Toast.makeText(this, "You have reached the maximum number of reservations.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //restarts reservation process
        btnCancel.setOnClickListener {
            finish()
        }
    }
}