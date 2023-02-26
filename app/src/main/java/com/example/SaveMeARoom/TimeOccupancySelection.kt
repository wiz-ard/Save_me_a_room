package com.example.SaveMeARoom


import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TimeOccupancySelection : AppCompatActivity() {

    private lateinit var adaptor : timeRecycleAdaptor
    private lateinit var timeRecycleView : RecyclerView
    private lateinit var timeList : ArrayList<timeData>
    private lateinit var timeText : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_occupancy_selection)

        val buildingHandle = intent.getStringExtra("building name")
        val topBuildingTitle : TextView = findViewById(R.id.tvTimeTitle)
        topBuildingTitle.text = buildingHandle+" reservation"

        getTimeRange()

        timeRecycleView = findViewById(R.id.rvTime)
        timeRecycleView.layoutManager = LinearLayoutManager(this)
        timeRecycleView.setHasFixedSize(true)
        adaptor = timeRecycleAdaptor(timeList){

            //place to put click action
            Toast.makeText(this,it.component1(), Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RoomSelection::class.java)
            intent.putExtra("building name", buildingHandle)
            startActivity(intent)

        }
        timeRecycleView.adapter = adaptor
    }


    private fun getTimeRange(){
        timeText = arrayOf(
            "5:00pm - 7:00pm",
            "7:00pm - 9:00pm",
            "9:00pm - 11:00pm")

        timeList = arrayListOf()

        for (i in timeText.indices){
            val timeRange = timeData(timeText[i])
            timeList.add(timeRange)
        }

    }
}