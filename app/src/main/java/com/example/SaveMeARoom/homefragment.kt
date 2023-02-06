package com.example.SaveMeARoom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Homefragment: Fragment() {
    
    private lateinit var adaptor : homeRecycleAdaptor
    private lateinit var recycleView : RecyclerView
    private lateinit var buildingList : ArrayList<buildingname>

    lateinit var buildingName : Array<String>

    // needed to display and fill in the data when its created
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.homefragment, container, false)
    }

    // fills in the data
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        recycleView = view.findViewById(R.id.rvReservation)
        recycleView.layoutManager = layoutManager
        recycleView.setHasFixedSize(true)
        adaptor = homeRecycleAdaptor(buildingList){

            //Place to put actions for when recycle view is clicked
            Toast.makeText(activity,it.component1(),Toast.LENGTH_SHORT).show()
        }
        recycleView.adapter = adaptor
    }

    //initializes building name data to be displayed
    private fun dataInitialize(){

        buildingList = arrayListOf()

        buildingName = arrayOf(
            "IESB",
            "NETH",
            "COBB",
            "BOGH",
            "GTM",
            "CTH",
            "a",
            "b",
            "c",
            "d",
            "e"
        )

        for (i in buildingName.indices){

            val building = buildingname(buildingName[i])
            buildingList.add(building)
        }

    }

}