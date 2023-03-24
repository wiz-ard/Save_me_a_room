package com.example.SaveMeARoom


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.time_occupancy_selection.*

class UpdateTimeSelection : AppCompatActivity() {

    private lateinit var adaptor : UpdateTimeAdapter
    private lateinit var timeRecycleView : RecyclerView
    private lateinit var timeList : ArrayList<timeData>
    private lateinit var timeText : Array<String>
    private lateinit var occupancy : String
    private lateinit var time : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_time_selection)

        val date = intent.getStringExtra("date")
        val buildingName = intent.getStringExtra("building name")
        val occupancy = intent.getStringExtra("occupancy")
        val room = intent.getStringExtra("room")
        val resId = intent.getStringExtra("resId")

        val topBuildingTitle : TextView = findViewById(R.id.tvUpdateTimeTitle)
        topBuildingTitle.text = buildingName +" reservation"

        getTimeRange()

        timeRecycleView = findViewById(R.id.rvUpdateTime)
        timeRecycleView.layoutManager = LinearLayoutManager(this)
        timeRecycleView.setHasFixedSize(true)
        adaptor = UpdateTimeAdapter(timeList){

            //place to put click action of the recycle view item
            val intent = Intent(this, UpdateConfirmation::class.java)
            time = it.component1()
            val timeText = time.toString()
            intent.putExtra("building name", buildingName)
            intent.putExtra("occupancy", occupancy)
            intent.putExtra("time", time)
            intent.putExtra("date", date)
            intent.putExtra("room", room)
            intent.putExtra("resId",resId)
            startActivity(intent)
            finish()

        }
        timeRecycleView.adapter = adaptor
    }


    private fun getTimeRange(){
        val date = intent.getStringExtra("date")
        val oldDate = intent.getStringExtra("olddate")
        val oldTime = intent.getStringExtra("oldtime").toString()
        timeText = arrayOf(
            "5:00pm - 7:00pm",
            "7:00pm - 9:00pm",
            "9:00pm - 11:00pm")

        timeList = arrayListOf()

        if(date.equals(oldDate)){
            for (i in timeText.indices){
                val timeRange = timeData(timeText[i])
                val timeString = timeRange.toString().substringAfter("=").substringBefore(")")
                if(!(timeString[0].equals(oldTime[0]))){
                    timeList.add(timeRange)
                }
            }
        }else{
            for (i in timeText.indices){
                val timeRange = timeData(timeText[i])
                timeList.add(timeRange)
            }
        }
    }
}