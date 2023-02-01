package com.example.SaveMeARoom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Homefragment: Fragment() {
    
    private lateinit var adaptor : homeRecycleAdaptor
    private lateinit var recycleView : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var buildingList = mutableListOf<buildingname>(
            buildingname("IESB"),
            buildingname("NETH"),
            buildingname("BOGH"),
            buildingname("COBB")
        )


        val layoutManager = LinearLayoutManager(context)
        recycleView = view.findViewById(R.id.rvReservation)
        recycleView.layoutManager = layoutManager
        adaptor = homeRecycleAdaptor(buildingList)
        recycleView.adapter = adaptor
    }

}