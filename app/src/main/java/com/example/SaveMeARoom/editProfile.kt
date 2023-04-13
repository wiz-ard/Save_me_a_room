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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.editprofile.*
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime


class editProfile : AppCompatActivity(), OnItemSelectedListener {
    var colleges = arrayOf<String?>("COES", "Business", "ANS", "Liberal Arts", "Education")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {

        val policy = ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.editprofile)

        //admin admin username password test
        btnSaveProfile.setOnClickListener {
            //pulls values from text boxes
            val newusername = newusername.text.toString()

            //checks for SQL injection characters
            if(validInput(newusername)){
                if(newusername.length > 0){
                    val oldUsername = intent.getStringExtra("olduser")
                    val ip = "http://3.132.20.107:3000"
                    //queries database to see if user exists
                    val query = "/search?query=SELECT%20*%20FROM%20users%20WHERE%20Username=%27" + newusername +"%27"

                    val url = URL(ip.plus(query))

                    val text = url.readText()

                    if (text.length > 2) {
                        Toast.makeText(this, "User already exists.", Toast.LENGTH_SHORT).show()
                    } else {
                        val newcollege = newcollege.selectedItem.toString()

                        val query = "/search?query=UPDATE%20users%20SET%20Username=%27" + newusername + "%27,College=%27" + newcollege + "%27%20WHERE%20Username=%27" + oldUsername +"%27"

                        val url = URL(ip.plus(query))

                        val text = url.readText()

                        Toast.makeText(this, "Account updated.", Toast.LENGTH_SHORT).show()

                        finish()
                    }
                }else{
                    val oldUsername = intent.getStringExtra("olduser")

                    val ip = "http://3.132.20.107:3000"

                    val newcollege = newcollege.selectedItem.toString()

                    val query = "/search?query=UPDATE%20users%20SET%20College=%27" + newcollege + "%27%20WHERE%20Username=%27" + oldUsername +"%27"

                    val url = URL(ip.plus(query))

                    val text = url.readText()

                    Toast.makeText(this, "College updated.", Toast.LENGTH_SHORT).show()

                    finish()
                }

            }else{
                Toast.makeText(this, "Banned characters are < > = ' and \"", Toast.LENGTH_SHORT).show()
            }
        }
        btnBackProfile.setOnClickListener {
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