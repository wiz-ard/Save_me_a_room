package com.example.SaveMeARoom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class AdminCalendarFragment: Fragment(R.layout.admin_calendar_fragment) {

    private lateinit var adaptor : adminCalendarRecycleViewAdaptor
    private lateinit var adminCalendarRecycleView : RecyclerView
    private lateinit var acceptedResList: ArrayList<adminCalendarAcceptResData>
    private lateinit var resText : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.admin_calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filterBuildings = getBuildings()

        val filterDates = getDates()

        val buildingSpin = getView()?.findViewById<Spinner>(R.id.spBuildingSelect)
        val dateSpin = getView()?.findViewById<Spinner>(R.id.spDateSelect)

        buildingSpin?.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // add function for when a building filter is clicked update the recycler view
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        dateSpin?.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // add function for when a date filter is clicked update the recycler view
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        val dateAD: ArrayAdapter<*> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, filterDates)
        val buildingAD: ArrayAdapter<*> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, filterBuildings)
        dateAD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        buildingAD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dateSpin?.adapter = dateAD
        buildingSpin?.adapter = buildingAD


        initilizeData()

        val layoutManager = LinearLayoutManager(context)
        adminCalendarRecycleView = view.findViewById(R.id.rvAdminCalendarAcceptedRes)
        adminCalendarRecycleView.layoutManager = layoutManager
        adminCalendarRecycleView.setHasFixedSize(true)
        adaptor = adminCalendarRecycleViewAdaptor(acceptedResList){
            //on click of item
        }
        adminCalendarRecycleView.adapter = adaptor
    }


    private fun getBuildings() {
        TODO("get list of buildings from query in form of array of strings")
    }

    private fun getDates(){
        TODO("Get list of dates from database in form of array of strings")
    }

    private fun initilizeData(){
        acceptedResList = arrayListOf()

        for (i in resText.indices){
            val acceptedReservations = adminCalendarAcceptResData(resText[i])
            acceptedResList.add(acceptedReservations)
        }
    }

}