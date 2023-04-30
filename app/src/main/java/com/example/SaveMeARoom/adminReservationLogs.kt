package com.example.SaveMeARoom


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.admin_reservation_logs.*
import kotlinx.android.synthetic.main.admin_user_logs.*
import kotlinx.android.synthetic.main.admin_userlog_info.*
import kotlinx.android.synthetic.main.create_account.*
import java.net.URL
import java.time.LocalDate

class adminReservationLogs : AppCompatActivity(), OnItemSelectedListener {
    private lateinit var adaptor: adminLogRecycleViewAdaptor
    private lateinit var userLogRecycleView: RecyclerView
    private lateinit var resLogList: ArrayList<adminLogData>
    private lateinit var resLogText: ArrayList<String>
    private lateinit var spinData: ArrayList<String>
    private lateinit var bldg: String
    private lateinit var room: String
    private lateinit var email: String
    private lateinit var college: String
    private lateinit var building: String
    private lateinit var action: String
    private lateinit var roomList: ArrayList<String>
    private lateinit var emailList: ArrayList<String>
    private lateinit var ad3: ArrayAdapter<*>
    private lateinit var displayId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_reservation_logs)
        college = intent.getStringExtra("college").toString()
        displayId = ""

        //gets list of buildings for spinner
        val ip = "http://3.132.20.107:3000"
        var query =
            "/search?query=SELECT%20DISTINCT%20Building%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27"
        var url = URL(ip.plus(query))
        var text = url.readText()
        var buildings = text.split(",")
        var buildingList = ArrayList<String>()
        buildingList.add("Any")
        for (i in buildings.indices) {
            if(buildings[i] != "[]"){
                val spBuilding = buildings[i].substringAfter(":").substringAfter("\"").substringBefore("\"")
                buildingList.add(spBuilding)
            }
        }

        //gets list of actions for spinner
        var actionList = ArrayList<String>()
        actionList.add("Any")
        actionList.add("Request")
        actionList.add("Updating")
        actionList.add("Cancel")
        actionList.add("Accept")
        actionList.add("Deny")
        actionList.add("Accept_Update")
        actionList.add("Deny_Update")

        //gets list of rooms for spinner
        building = "Any"
        getRooms(building)

        //gets list of emails for spinner
        action = "Any"
        getEmails(action)



        spinData = arrayListOf()
        spinData.add("")
        spinData.add("")
        spinData.add("")
        spinData.add("")
        getResLogs(spinData)
        userLogRecycleView = findViewById(R.id.rvAdminLogs)
        userLogRecycleView.layoutManager = LinearLayoutManager(this)
        userLogRecycleView.setHasFixedSize(true)

        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        val spin = findViewById<Spinner>(R.id.spBuildingSelect)
        spin.onItemSelectedListener = this

        // Create the instance of ArrayAdapter
        // having the list of courses
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, buildingList as List<Any?>)

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin.adapter = ad

        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        val spin2 = findViewById<Spinner>(R.id.spActionSelect)
        spin2.onItemSelectedListener = this

        // Create the instance of ArrayAdapter
        // having the list of courses
        val ad2: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, actionList as List<Any?>)

        // set simple layout resource file
        // for each item of spinner
        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin2.adapter = ad2

        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        val spin3 = findViewById<Spinner>(R.id.spRoomSelect)
        spin3.onItemSelectedListener = this

        // Create the instance of ArrayAdapter
        // having the list of courses
        ad3 = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, roomList as List<Any?>)

        // set simple layout resource file
        // for each item of spinner
        ad3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin3.adapter = ad3

        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        val spin4 = findViewById<Spinner>(R.id.spEmailSelect)
        spin4.onItemSelectedListener = this

        // Create the instance of ArrayAdapter
        // having the list of courses
        val ad4: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, emailList as List<Any?>)

        // set simple layout resource file
        // for each item of spinner
        ad4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin4.adapter = ad4



        btnResSearch.setOnClickListener {
            getResLogs(spinData)

            adaptor = adminLogRecycleViewAdaptor(resLogList) {

                //place to put click action
                val intent = Intent(this, adminReservationlogInfo::class.java)
                intent.putExtra("log info", it.component1())
                intent.putExtra("action", action)
                intent.putExtra("displayId", displayId)
                startActivity(intent)
                finish()
            }
            userLogRecycleView.adapter = adaptor
        }

        btnBackRL.setOnClickListener{
            finish()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        if (parent!!.id == R.id.spBuildingSelect) {
            // first spinner selected
            val item = parent!!.getItemAtPosition(position)
            building = item.toString()
            spinData[0] = item.toString()
            getRooms(building)
        } else if (parent!!.id == R.id.spActionSelect) {
            // second spinner selected
            val item = parent!!.getItemAtPosition(position)
            action = item.toString()
            spinData[1] = item.toString()
            getEmails(action)
        }else if (parent!!.id == R.id.spRoomSelect) {
            // second spinner selected
            val item = parent!!.getItemAtPosition(position)
            spinData[2] = item.toString()
        }else{
            val item = parent!!.getItemAtPosition(position)
            spinData[3] = item.toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun getResLogs(spinData: ArrayList<String>) {
        if (spinData.size == 0) {
        } else {
            bldg = spinData[0]
            action = spinData[1]
            room = spinData[2]
            email = spinData[3]
            if(spinData.size == 0){
            }else {
                if (bldg.equals("Any") && action.equals("Any") && room.equals("Any") && email.equals("Any")) {
                    getLogs("1")
                } else if (bldg.equals("Any") && action.equals("Any") && room.equals("Any") && !(email.equals("Any"))) {
                    getLogs("2")
                } else if (bldg.equals("Any") && action.equals("Any") && !(room.equals("Any")) && email.equals("Any")) {
                    getLogs("3")
                } else if (bldg.equals("Any") && action.equals("Any") && !(room.equals("Any")) && !(email.equals("Any"))) {
                    getLogs("4")
                } else if (bldg.equals("Any") && !(action.equals("Any")) && room.equals("Any") && email.equals("Any")) {
                    getLogs("5")
                } else if (bldg.equals("Any") && !(action.equals("Any")) && room.equals("Any") && !(email.equals("Any"))) {
                    getLogs("6")
                } else if (bldg.equals("Any") && !(action.equals("Any")) && !(room.equals("Any")) && email.equals("Any")) {
                    getLogs("7")
                } else if (bldg.equals("Any") && !(action.equals("Any")) && !(room.equals("Any")) && !(email.equals("Any"))) {
                    getLogs("8")
                } else if (!(bldg.equals("Any")) && action.equals("Any") && room.equals("Any") && email.equals("Any")) {
                    getLogs("9")
                } else if (!(bldg.equals("Any")) && action.equals("Any") && room.equals("Any") && !(email.equals("Any"))) {
                    getLogs("10")
                } else if (!(bldg.equals("Any")) && action.equals("Any") && !(room.equals("Any")) && email.equals("Any")) {
                    getLogs("11")
                } else if (!(bldg.equals("Any")) && action.equals("Any") && !(room.equals("Any")) && !(email.equals("Any"))) {
                    getLogs("12")
                } else if (!(bldg.equals("Any")) && !(action.equals("Any")) && room.equals("Any") && email.equals("Any")) {
                    getLogs("13")
                } else if (!(bldg.equals("Any")) && !(action.equals("Any")) && room.equals("Any") && !(email.equals("Any"))) {
                    getLogs("14")
                } else if (!(bldg.equals("Any")) && !(action.equals("Any")) && !(room.equals("Any")) && email.equals("Any")) {
                    getLogs("15")
                } else {
                    if(bldg.equals("")){

                    }else{
                        getLogs("16")
                    }
                }
            }
        }
    }
    private fun getRooms(building:String) {
        if(!(building.equals("Any"))){
            roomList = arrayListOf()
            val ip = "http://3.132.20.107:3000"
            val query = "/search?query=SELECT%20DISTINCT%20Room%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Building=%27" + building + "%27"
            val url = URL(ip.plus(query))
            val text = url.readText()
            val rooms = text.split(",")
            roomList.add("Any")
            for (i in rooms.indices) {
                if(rooms[i] != "[]"){
                    val spRoom = rooms[i].substringAfter(":").substringAfter("\"").substringBefore("\"")
                    roomList.add(spRoom)
                }
            }
            // Take the instance of Spinner and
            // apply OnItemSelectedListener on it which
            // tells which item of spinner is clicked
            val spin3 = findViewById<Spinner>(R.id.spRoomSelect)

            // Create the instance of ArrayAdapter
            // having the list of courses
            val ad3= ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, roomList as List<Any?>)

            // set simple layout resource file
            // for each item of spinner
            ad3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Set the ArrayAdapter (ad) data on the
            // Spinner which binds data to spinner
            spin3.adapter = ad3

        }else{
            roomList = arrayListOf()
            val ip = "http://3.132.20.107:3000"
            val query = "/search?query=SELECT%20DISTINCT%20Room%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27"
            var url = URL(ip.plus(query))
            var text = url.readText()
            var rooms = text.split(",")
            roomList.add("Any")
            for (i in rooms.indices) {
                if(rooms[i] != "[]"){
                    val spRoom = rooms[i].substringAfter(":").substringAfter("\"").substringBefore("\"")
                    roomList.add(spRoom)
                }
            }
        }
    }
    private fun getEmails(action:String) {
        if(!(action.equals("Any"))){
            emailList = arrayListOf()
            val ip = "http://3.132.20.107:3000"
            val query = "/search?query=SELECT%20DISTINCT%20Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20" + action + "=%27True%27"
            val url = URL(ip.plus(query))
            val text = url.readText()
            val emails = text.split(",")
            emailList.add("Any")
            for (i in emails.indices) {
                if(emails[i] != "[]"){
                    val spEmail = emails[i].substringAfter(":").substringAfter("\"").substringBefore("\"")
                    emailList.add(spEmail)
                }
            }
            // Take the instance of Spinner and
            // apply OnItemSelectedListener on it which
            // tells which item of spinner is clicked
            val spin4 = findViewById<Spinner>(R.id.spEmailSelect)

            // Create the instance of ArrayAdapter
            // having the list of courses
            val ad4: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, emailList as List<Any?>)

            // set simple layout resource file
            // for each item of spinner
            ad4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Set the ArrayAdapter (ad) data on the
            // Spinner which binds data to spinner
            spin4.adapter = ad4
        }else{
            emailList = arrayListOf()
            val ip = "http://3.132.20.107:3000"
            val query = "/search?query=SELECT%20DISTINCT%20Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27"
            var url = URL(ip.plus(query))
            var text = url.readText()
            var emails = text.split(",")
            emailList.add("Any")
            for (i in emails.indices) {
                if(emails[i] != "[]"){
                    val spEmail = emails[i].substringAfter(":").substringAfter("\"").substringBefore("\"")
                    emailList.add(spEmail)
                }
            }
        }
    }
    private fun getLogs(dId:String){
        var query = ""
        resLogList = arrayListOf()
        resLogText = arrayListOf()
        val ip = "http://3.132.20.107:3000"
        when(dId){
            "1" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27"
            "2" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Email_Of_Action=%27" + email + "%27"
            "3" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Room=%27" + room + "%27"
            "4" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20And%20Room=%27" + room + "%27%20AND%20Email_Of_Action=%27" + email + "%27"
            "5" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20And%20" + action + "=%27True%27"
            "6" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Email_Of_Action=%27" + email + "%27%20AND%20" + action + "=%27True%27"
            "7" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Room=%27" + room + "%27%20AND%20" + action + "=%27True%27"
            "8" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Room=%27" + room + "%27%20AND%20" + action + "=%27True%27%20AND%20Email_Of_Action=%27" + email + "%27"
            "9" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Building=%27" + bldg + "%27"
            "10" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Building=%27" + bldg + "%27%20AND%20Email_Of_Action=%27" + email + "%27"
            "11" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Building=%27" + bldg + "%27%20AND%20Room=%27" + room + "%27"
            "12" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Building=%27" + bldg + "%27%20AND%20Room=%27" + room + "%27%20AND%20Email_Of_Action=%27" + email + "%27"
            "13" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Building=%27" + bldg + "%27%20AND%20" + action + "=%27True%27"
            "14" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Building=%27" + bldg + "%27%20AND%20" + action + "=%27True%27%20AND%20Email_Of_Action=%27" + email + "%27"
            "15" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Building=%27" + bldg + "%27%20AND%20" + action + "=%27True%27%20AND%20Room=%27" + room + "%27"
            "16" -> query = "/search?query=SELECT%20Reservation_Id,Building,Room,Reservation_Time,Reserver_Email,Time_Of_Action,Email_Of_Action%20FROM%20reservationlogs%20WHERE%20College=%27" + college + "%27%20AND%20Building=%27" + bldg + "%27%20AND%20" + action + "=%27True%27%20AND%20Room=%27" + room + "%27%20AND%20Email_Of_Action=%27" + email + "%27"
        }
        var url = URL(ip.plus(query))
        var text = url.readText()
        var logs = text.split(",")

        var track = 1
        //loop to add reservations to the recycle view
        for (i in logs.indices) {
            val log = adminLogData(logs[i].substringAfter(":").substringAfter('"').substringBefore('"'))
            val logtrim = log.toString().substringAfter("=").substringBefore(")")
            resLogText.add(logtrim)
            if(track % 7 == 0){
                val timeOfRes = resLogText[i-3]
                var date = timeOfRes.substringBefore(" ") + " "
                val start = (timeOfRes.substringAfter(" ").substringBefore(":").toInt()-12).toString() + " - "
                val end = (timeOfRes.substringAfter(" - ").substringAfter(" ").substringBefore(":").toInt()-12).toString() + "pm"
                val finalTime = date + start + end
                val timeOfAction = resLogText[i-1]
                date = timeOfAction.substringAfter(" ") + " "
                var actionTime = timeOfAction.substringBefore(" ").substringBefore(".")
                if(actionTime.substringBefore(":").toInt() > 12){
                    actionTime = (actionTime.substringBefore(":").toInt()-12).toString() + ":" + actionTime.substringAfter(":") + "pm"
                }else{
                    actionTime = actionTime + "am"
                }
                val finalActionTime = date + actionTime
                var finalLog = adminLogData(resLogText[i - 6] + ", " + resLogText[i - 5] + ", " + resLogText[i - 4] + ", " + finalTime + ", " + resLogText[i - 2] + ", " + finalActionTime + ", " + resLogText[i - 0])
                resLogList.add(finalLog)
            }
            track += 1
        }
    }
}