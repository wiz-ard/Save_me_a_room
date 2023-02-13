package com.example.SaveMeARoom


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reservation_date_selection)

        //taking in the passed building name data from home fragment
        val buildingHandle = intent.getStringExtra("building name")
        val topBuildingTitle : TextView = findViewById(R.id.tvTitle)
        topBuildingTitle.text = buildingHandle+" reservation"

        getDate()

        dateRecycleView = findViewById(R.id.rvDate)
        dateRecycleView.layoutManager = LinearLayoutManager(this)
        dateRecycleView.setHasFixedSize(true)
        adaptor = dateRecycleAdaptor(dateList){

            //place to put click action
            Toast.makeText(this,it.component1(),Toast.LENGTH_SHORT).show()

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