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
import kotlinx.android.synthetic.main.create_account.newpassword
import kotlinx.android.synthetic.main.forgot_password.*
import java.net.URL

class forgotpass : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)

        resetbtn.setOnClickListener {
            val username = forgotusername.text.toString()
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
            val email = forgotemail.text.toString()
            val confirm = forgotconfirm.text.toString()
            val nextPage = Intent(this, MainActivity::class.java)
            val ip = "http://3.132.20.107:3000"
            val query = "/search?query=SELECT%20*%20FROM%20users%20WHERE%20Username=%27" + username + "%27%20AND%20Email=%27" + email + "%27"
            val query2 = "/search?query=UPDATE%20users%20SET%20Password=%27" + hashed + "%27%20WHERE%20Username=%27" + username + "%27%20AND%20Email=%27" + email + "%27"
            if(validInput(username) && validInput(password) && validInput(confirm) && validInput(email)){
                if (username.length > 0 && 8 <= password.length && password.length <= 20 && email.length > 0 && password.equals(confirm)) {
                    if (capitalcount >= 1 && lowercount >= 1 && symbolcount >= 1 && numbercount >= 1) {
                        val url = URL(ip.plus(query))
                        val text = url.readText()
                        if (text.length > 2) {
                            val url2 = URL(ip.plus(query2))
                            val text2 = url2.readText()
                            Toast.makeText(this, "Password reset successful!", Toast.LENGTH_SHORT).show()
                            startActivity(nextPage)
                            finish()
                        } else {
                            Toast.makeText(this, "Incorrect information provided.", Toast.LENGTH_SHORT).show()
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
        tologinbtn.setOnClickListener {
            val nextPage = Intent(this, MainActivity::class.java)
            startActivity(nextPage)
        }
    }

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
