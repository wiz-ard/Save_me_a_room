package com.example.SaveMeARoom


import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RoomSelection : AppCompatActivity() {

    private lateinit var adaptor : roomRecycleAdaptor
    private lateinit var roomRecycleView : RecyclerView
    private lateinit var roomList : ArrayList<roomData>
    private lateinit var roomText : Array<String>
    private lateinit var room : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_book_selection)

        val buildingName = intent.getStringExtra("building name")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val occupancy = intent.getStringExtra("occupancy")

        val topBuildingTitle : TextView = findViewById(R.id.tvRoomTitle)
        topBuildingTitle.text = buildingName+" reservation"

        getRooms()

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
            startActivity(intent)
            finish()


        }
        roomRecycleView.adapter = adaptor
    }


    private fun getRooms(){
        roomText = arrayOf(
            "iesb 1",
            "iesb 2",
            "iesb 3"
        )

        roomList = arrayListOf()

        for (i in roomText.indices){
            val roomData = roomData(roomText[i])
            roomList.add(roomData)
        }

    }
}