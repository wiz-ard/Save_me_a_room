package com.example.SaveMeARoom


import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.URL

class RoomSelection : AppCompatActivity() {

    private lateinit var adaptor : roomRecycleAdaptor
    private lateinit var roomRecycleView : RecyclerView
    private lateinit var roomList : ArrayList<roomData>
    private lateinit var room : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_book_selection)

        val buildingName = intent.getStringExtra("building name")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val email = intent.getStringExtra("email")


        var start = ""
        if(time.equals("5:00pm - 7:00pm")){
            start = "17:00:00"
        }else if(time.equals("7:00pm - 9:00pm")){
            start = "19:00:00"
        }else{
            start = "21:00:00"
        }
        val occupancy = intent.getStringExtra("occupancy")

        val topBuildingTitle : TextView = findViewById(R.id.tvRoomTitle)
        topBuildingTitle.text = buildingName+" reservation"

        getRooms(buildingName.toString(),date.toString(),time.toString(),occupancy.toString(),start)

        roomRecycleView = findViewById(R.id.rvRoom)
        roomRecycleView.layoutManager = LinearLayoutManager(this)
        roomRecycleView.setHasFixedSize(true)
        adaptor = roomRecycleAdaptor(roomList){

            //place to put click action
            //Toast.makeText(this,it.component1(), Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BookConfirmation::class.java)
            room = it.component1()
            intent.putExtra("building name", buildingName)
            intent.putExtra("occupancy", occupancy)
            intent.putExtra("time", time)
            intent.putExtra("date", date)
            intent.putExtra("room", room)
            intent.putExtra("email", email)
            startActivity(intent)
            finish()


        }
        roomRecycleView.adapter = adaptor
    }


    private fun getRooms(building:String,date:String,time:String,occupancy:String, start:String){
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        val ip = "http://3.132.20.107:3000"

        val query = "/search?query=SELECT%20DISTINCT%20locations.Room_Number%20FROM%20Capstone.locations%20WHERE%20locations.Occupancy_Range%20=%20" + occupancy +"%20AND%20locations.Building_Name%20=%20%27" + building +"%27%20AND%20(locations.Room_Number%20Not%20IN%20(SELECT%20Room_Number%20FROM%20Capstone.reservations%20WHERE%20Start_Date_Time%20=%20%27" + date + "%20" + start + "%27%20OR%20End_Date_time%20LIKE%20%27" + date + "%2011:59:00%27))ORDER%20BY%20Room_Number"

        val url = URL(ip.plus(query))

        val text = url.readText()

        if(text.equals("[]")){
            Toast.makeText(this, "No rooms available in this building at this time :(", Toast.LENGTH_SHORT).show()
            finish()
        }

        val rooms = text.split(",")

        roomList = arrayListOf()

        for (i in rooms.indices){
            val roomData = roomData(rooms[i].substringAfter(":").substringBefore('}'))
            roomList.add(roomData)
        }

    }
}