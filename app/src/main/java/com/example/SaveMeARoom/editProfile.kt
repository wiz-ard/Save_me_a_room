package com.example.SaveMeARoom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.editprofile.*
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime


class editProfile : AppCompatActivity(), OnItemSelectedListener {
    var colleges = arrayOf<String?>("COES", "Business", "ANS", "Liberal Arts", "Education")
    private lateinit var email:String
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {

        val policy = ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.editprofile)

        val admin = intent.getStringExtra("admin")

        if(admin == "1"){
            newcollege.isInvisible = true
            newcollegetext.isInvisible = true
        }

        //admin admin username password test
        btnSaveProfile.setOnClickListener {
            //pulls values from text boxes
            val newusername = newusername.text.toString()
            val oldUsername = intent.getStringExtra("olduser")

            //checks for SQL injection characters
            if(validInput(newusername)){
                if(newusername.length > 0){
                    val ip = "http://3.132.20.107:3000"
                    //queries database to see if user exists
                    var query = "/search?query=SELECT%20*%20FROM%20users%20WHERE%20Username=%27" + newusername +"%27"

                    var url = URL(ip.plus(query))

                    var text = url.readText()

                    query = "/search?query=SELECT%20Email%20FROM%20users%20WHERE%20Username=%27" + oldUsername +"%27"

                    url = URL(ip.plus(query))

                    email = url.readText().substringAfter(":").substringAfter("\"").substringBefore("\"")

                    if (text.length > 2) {
                        Toast.makeText(this, "User already exists.", Toast.LENGTH_SHORT).show()
                    } else {
                        if(!(admin == "1")) {
                            val newcollege = newcollege.selectedItem.toString()

                            //check if college is the same
                            var query =
                                "/search?query=SELECT%20College%20FROM%20users%20WHERE%20Username=%27" + oldUsername + "%27"

                            var url = URL(ip.plus(query))

                            val text = url.readText()

                            val college =
                                text.substringAfter(":").substringAfter("\"").substringBefore("\"")

                            if (college == newcollege) {

                                query =
                                    "/search?query=SELECT%20*%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"

                                url = URL(ip.plus(query))

                                val text = url.readText()

                                if(text.length > 2){
                                    query =
                                        "/search?query=UPDATE%20users%20SET%20Username=%27" + newusername + "%27%20WHERE%20Email=%27" + email + "%27"

                                    url = URL(ip.plus(query))

                                    url.readText()

                                    query =
                                        "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"

                                    url = URL(ip.plus(query))

                                    url.readText()

                                    Toast.makeText(this, "Username updated, current college picked, cancelling request.", Toast.LENGTH_SHORT).show()
                                }else{

                                    query =
                                        "/search?query=UPDATE%20users%20SET%20Username=%27" + newusername + "%27%20WHERE%20Email=%27" + email + "%27"

                                    url = URL(ip.plus(query))

                                    url.readText()

                                    query = "/search?query=UPDATE%20statusrequests%20SET%20Viewing=0%20WHERE%20College_Request=1%20AND%20Email=%27" + email + "%27"

                                    url = URL(ip.plus(query))

                                    url.readText()

                                    Toast.makeText(this, "Username updated.", Toast.LENGTH_SHORT).show()
                                }
                                finish()

                            } else {
                                query =
                                    "/search?query=UPDATE%20users%20SET%20Username=%27" + newusername + "%27%20WHERE%20Email=%27" + email + "%27"

                                url = URL(ip.plus(query))

                                url.readText()

                                query =
                                    "/search?query=SELECT%20*%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"

                                url = URL(ip.plus(query))

                                val text = url.readText()

                                if(text.length > 2){
                                    query =
                                        "/search?query=UPDATE%20statusrequests%20SET%20New_College=%27" + newcollege + "%27%20AND%20Viewing=0%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"

                                    url = URL(ip.plus(query))

                                    url.readText()
                                }else{
                                    query =
                                        "/search?query=INSERT%20INTO%20statusrequests%20VALUES(%27" + email + "%27,%270%27,%271%27,%27null%27,%27" + college + "%27,%27" + newcollege + "%27,0)"

                                    url = URL(ip.plus(query))

                                    url.readText()
                                }
                                Toast.makeText(this, "Account updated.", Toast.LENGTH_SHORT).show()
                            }
                            finish()
                        }else{
                            query =
                                "/search?query=UPDATE%20users%20SET%20Username=%27" + newusername + "%27%20WHERE%20Email=%27" + email + "%27"

                            url = URL(ip.plus(query))

                            url.readText()

                            Toast.makeText(this, "Account updated.", Toast.LENGTH_SHORT).show()

                            finish()
                        }
                    }
                }else{
                    if(admin == "0") {
                        val oldUsername = intent.getStringExtra("olduser")

                        val ip = "http://3.132.20.107:3000"

                        var query =
                            "/search?query=SELECT%20Email%20FROM%20users%20WHERE%20Username=%27" + oldUsername + "%27"

                        var url = URL(ip.plus(query))

                        email = url.readText().substringAfter(":").substringAfter("\"")
                            .substringBefore("\"")

                        //check if college is the same
                        query =
                            "/search?query=SELECT%20College%20FROM%20users%20WHERE%20Username=%27" + oldUsername + "%27"

                        url = URL(ip.plus(query))

                        val text = url.readText()

                        val college =
                            text.substringAfter(":").substringAfter("\"").substringBefore("\"")

                        val newcollege = newcollege.selectedItem.toString()

                        if (!(college == newcollege)) {

                            query =
                                "/search?query=SELECT%20*%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"

                            url = URL(ip.plus(query))

                            val text = url.readText()

                            if(text.length > 2){
                                query =
                                    "/search?query=UPDATE%20statusrequests%20SET%20New_College=%27" + newcollege + "%27%20AND%20Viewing=0%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"

                                url = URL(ip.plus(query))

                                url.readText()

                                Toast.makeText(this, "College change request updated.", Toast.LENGTH_SHORT).show()
                            }else{
                                query =
                                    "/search?query=INSERT%20INTO%20statusrequests%20VALUES(%27" + email + "%27,%270%27,%271%27,%27null%27,%27" + college + "%27,%27" + newcollege + "%27,0)"

                                url = URL(ip.plus(query))

                                url.readText()

                                Toast.makeText(this, "College change request sent.", Toast.LENGTH_SHORT).show()
                            }
                            finish()
                        } else {
                            query =
                                "/search?query=SELECT%20*%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"

                            url = URL(ip.plus(query))

                            val text = url.readText()
                            if(text.length > 2){
                                query = "/search?query=DELETE%20FROM%20statusrequests%20WHERE%20Email=%27" + email + "%27%20AND%20College_Request=%271%27"

                                url = URL(ip.plus(query))

                                url.readText()

                                Toast.makeText(this, "Current college chosen, deleting change request.", Toast.LENGTH_SHORT).show()

                                finish()
                            }else{
                                Toast.makeText(this, "Nothing changed.", Toast.LENGTH_SHORT).show()
                                query = "/search?query=UPDATE%20statusrequests%20SET%20Viewing=0%20WHERE%20College_Request=1%20AND%20Email=%27" + email + "%27"

                                url = URL(ip.plus(query))

                                url.readText()
                                finish()
                            }
                        }
                    }else{
                        Toast.makeText(this, "Nothing changed.", Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                Toast.makeText(this, "Banned characters are < > = ' and \"", Toast.LENGTH_SHORT).show()
            }
        }
        btnBackProfile.setOnClickListener {

            val oldUsername = intent.getStringExtra("olduser")

            val ip = "http://3.132.20.107:3000"

            var query = "/search?query=SELECT%20Email%20FROM%20users%20WHERE%20Username=%27" + oldUsername +"%27"

            var url = URL(ip.plus(query))

            email = url.readText().substringAfter(":").substringAfter("\"").substringBefore("\"")

            query = "/search?query=UPDATE%20statusrequests%20SET%20Viewing=0%20WHERE%20College_Request=1%20AND%20Email=%27" + email + "%27"

            url = URL(ip.plus(query))

            url.readText()
            finish()
        }
        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        val spin = findViewById<Spinner>(R.id.newcollege)
        spin.onItemSelectedListener = this

        // Create the instance of ArrayAdapter
        // having the list of courses
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, colleges)

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin.adapter = ad
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
    //checks for SQL injection characters
    fun validInput(userText: String): Boolean{
        var invalidcount = 0
        for (letter in userText) {
            val ascii = letter.code
            if (ascii == 34 || ascii == 39 || ascii == 60 || ascii == 61 || ascii == 62) {
                invalidcount += 1
            }
        }
        return (invalidcount == 0)
    }
}