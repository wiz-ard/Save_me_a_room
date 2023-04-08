package com.example.SaveMeARoom


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class adminUserLogs : AppCompatActivity() {
    private lateinit var adaptor : adminLogRecycleViewAdaptor
    private lateinit var userLogRecycleView : RecyclerView
    private lateinit var userLogList : ArrayList<adminLogData>
    private lateinit var userLogText : ArrayList<String>
    private lateinit var log : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reservation_date_selection)

        //taking in the passed building name data from home fragment
        val buildingName = intent.getStringExtra("building name")
        val username = intent.getStringExtra("username")
        val college = intent.getStringExtra("college")

        val topBuildingTitle : TextView = findViewById(R.id.tvDateTitle)
        topBuildingTitle.text = buildingName+" reservation"

        getDate()

        userLogRecycleView = findViewById(R.id.rvDate)
        userLogRecycleView.layoutManager = LinearLayoutManager(this)
        userLogRecycleView.setHasFixedSize(true)
        adaptor = adminLogRecycleViewAdaptor(userLogList){

            //place to put click action
            val intent = Intent(this, TimeOccupancySelection::class.java)
            log = it.component1()
            intent.putExtra("building name", buildingName)
            intent.putExtra("date", log)
            intent.putExtra("username", username)
            intent.putExtra("college", college)
            startActivity(intent)
            finish()
        }
        userLogRecycleView.adapter = adaptor
    }

    private fun getDate(){
        userLogList = arrayListOf()

        userLogText = arrayListOf()

        val startTime = LocalDate.now()

        val scheduleDays = 14

        userLogText.add(startTime.toString())

        for (i: Int in 1..scheduleDays ){
            userLogText.add(startTime.plusDays(i.toLong()).toString())
        }

        for(i in userLogText.indices){
            val resLog = adminLogData(userLogText[i])
            userLogList.add(resLog)
        }
    }
}