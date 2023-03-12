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

class TimeOccupancySelection : AppCompatActivity() {

    private lateinit var adaptor : timeRecycleAdaptor
    private lateinit var timeRecycleView : RecyclerView
    private lateinit var timeList : ArrayList<timeData>
    private lateinit var timeText : Array<String>
    private lateinit var occupancy : String
    private lateinit var time : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_occupancy_selection)

        //sets occupancy to default 2-10 people
        occupancy = "0"

        val date = intent.getStringExtra("date")
        val buildingName = intent.getStringExtra("building name")
        val username = intent.getStringExtra("username")

        val topBuildingTitle : TextView = findViewById(R.id.tvTimeTitle)
        topBuildingTitle.text = buildingName+" reservation"

        getTimeRange()

        //gets occupancy text selected by user and stores it in occupancy as string
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            occupancy = radio.text.toString()
            if(occupancy.equals("2 - 10")){
                occupancy = "0"
            }else if(occupancy.equals("11 - 29")){
                occupancy = "1"
            }else if(occupancy.equals("30 - 49")){
                occupancy = "2"
            }else{
                occupancy = "3"
            }
        })


        timeRecycleView = findViewById(R.id.rvTime)
        timeRecycleView.layoutManager = LinearLayoutManager(this)
        timeRecycleView.setHasFixedSize(true)
        adaptor = timeRecycleAdaptor(timeList){

            //place to put click action of the recycle view item
            val intent = Intent(this, RoomSelection::class.java)
            time = it.component1()
            intent.putExtra("building name", buildingName)
            intent.putExtra("occupancy", occupancy)
            intent.putExtra("time", time)
            intent.putExtra("date", date)
            intent.putExtra("username", username)
            startActivity(intent)
            finish()

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