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

class ReservationDateSelection : AppCompatActivity() {

    private lateinit var adaptor : dateRecycleAdaptor
    private lateinit var dateRecycleView : RecyclerView
    private lateinit var dateList : ArrayList<dateData>
    private lateinit var dateText : ArrayList<String>
    private lateinit var date : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reservation_date_selection)

        //taking in the passed building name data from home fragment
        val buildingName = intent.getStringExtra("building name")
        val email = intent.getStringExtra("email")
        val resNum = intent.getStringExtra("reserveNum")
        val username = intent.getStringExtra("username")


        val topBuildingTitle : TextView = findViewById(R.id.tvDateTitle)
        topBuildingTitle.text = buildingName+" reservation"

        getDate()

        dateRecycleView = findViewById(R.id.rvDate)
        dateRecycleView.layoutManager = LinearLayoutManager(this)
        dateRecycleView.setHasFixedSize(true)
        adaptor = dateRecycleAdaptor(dateList){

            //place to put click action
            val intent = Intent(this, TimeOccupancySelection::class.java)
            date = it.component1()
            //Toast.makeText(this,date,Toast.LENGTH_SHORT).show()
            intent.putExtra("building name", buildingName)
            intent.putExtra("date", date)
            intent.putExtra("email", email)
            intent.putExtra("reserveNum", resNum)
            intent.putExtra("username", username)
            startActivity(intent)
            finish()
        }
        dateRecycleView.adapter = adaptor
    }

    private fun getDate(){
        dateList = arrayListOf()

        dateText = arrayListOf()

        val startTime = LocalDate.now()

        val scheduleDays = 14

        dateText.add(startTime.toString())

        for (i: Int in 1..scheduleDays ){
            dateText.add(startTime.plusDays(i.toLong()).toString())
        }

        for(i in dateText.indices){
            val date = dateData(dateText[i])
            dateList.add(date)
        }

    }
}