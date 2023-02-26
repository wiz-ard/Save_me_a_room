package com.example.SaveMeARoom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_account.*
import java.net.URL

class createacc : AppCompatActivity(), OnItemSelectedListener {
    // create array of Strings
    // and store name of courses
    var colleges = arrayOf<String?>("COES", "Business", "ANS", "Liberal Arts", "Education")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        newcreatebtn.setOnClickListener {
            val username = newusername.text.toString()
            val password = newpassword.text.toString()
            var capitalcount = 0
            var lowercount = 0
            var numbercount = 0
            var symbolcount = 0
            for (letter in password) {
                val ascii = letter.code
                if (48 <= ascii && ascii <= 57) {
                    numbercount += 1
                } else if (65 <= ascii && ascii <= 90) {
                    capitalcount += 1
                } else if (97 <= ascii && ascii <= 122) {
                    lowercount += 1
                } else if (ascii == 33 || (ascii >= 35 && ascii <= 38) || ascii == 40 || ascii == 41 || (43 <= ascii && ascii <= 47) || ascii == 58 || ascii == 59 || ascii == 63 || ascii == 64 || (91 <= ascii && ascii <= 96) || (123 <= ascii && ascii <= 126)) {
                    symbolcount += 1
                }
            }
            val hashed = password.hashCode()
            val email = email.text.toString()
            val confirm = confirm.text.toString()
            val college = college.selectedItem.toString()
            val nextPage = Intent(this, MainActivity::class.java)
            val ip = "http://3.132.20.107:3000"
            val query = "/search?query=SELECT%20*%20FROM%20users%20WHERE%20Username=%27" + username + "%27%20OR%20Email=%27" + email + "%27"
            val query2 = "/search?query=INSERT%20into%20users%20values(%27" + username + "%27," + hashed + ",%27" + email + "%27,%27" + college + "%27,0,0,0)"
            if(validInput(username) && validInput(password) && validInput(confirm) && validInput(email)){
                if (username.length > 0 && 8 <= password.length && password.length <= 20 && email.length > 0 && password.equals(confirm)) {
                    if (capitalcount >= 1 && lowercount >= 1 && symbolcount >= 1 && numbercount >= 1) {
                        val url = URL(ip.plus(query))
                        val text = url.readText()
                        if (text.length > 2) {
                            Toast.makeText(this, "Account already exists.", Toast.LENGTH_SHORT).show()
                        } else {
                            val url2 = URL(ip.plus(query2))
                            val text2 = url2.readText()
                            Toast.makeText(this, "Account creation successful!", Toast.LENGTH_SHORT).show()
                            startActivity(nextPage)
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Password needs 1 uppercase letter, 1 lowercase, 1 symbol, 1 number.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Incorrect Fields", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Banned characters are < > = ' and \"", Toast.LENGTH_SHORT).show()
            }
        }

        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        val spin = findViewById<Spinner>(R.id.college)
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
        // make toastof name of course
        // which is selected in spinner
        Toast.makeText(applicationContext, colleges[position], Toast.LENGTH_LONG).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

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
