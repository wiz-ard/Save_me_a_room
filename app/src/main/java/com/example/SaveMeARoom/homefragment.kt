package com.example.SaveMeARoom

import androidx.fragment.app.Fragment

class Homefragment: Fragment(R.layout.homefragment) {

    // building list to be replaced with api eventually
    var buildingList = mutableListOf(
        buildingname("IESB"),
        buildingname("COBB"),
        buildingname("GTM"),
        buildingname("Neth"),
        buildingname("BOUG")
    )
    //recycle view adaptor settings for filling the view
    //val adapter = homeRecycleAdaptor(buildingList)
    //rvReservation.adapter = adapter
    //rvReservation.layoutManager = LinearLayoutManager(this)

}