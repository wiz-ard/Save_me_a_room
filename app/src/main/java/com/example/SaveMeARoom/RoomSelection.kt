package com.example.SaveMeARoom


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_book_selection)

        val buildingHandle = intent.getStringExtra("building name")
        val topBuildingTitle : TextView = findViewById(R.id.tvRoomTitle)
        topBuildingTitle.text = buildingHandle+" reservation"

        getRooms()

        roomRecycleView = findViewById(R.id.rvRoom)
        roomRecycleView.layoutManager = LinearLayoutManager(this)
        roomRecycleView.setHasFixedSize(true)
        adaptor = roomRecycleAdaptor(roomList){

            //place to put click action
            Toast.makeText(this,it.component1(), Toast.LENGTH_SHORT).show()

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