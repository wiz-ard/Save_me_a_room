package com.example.SaveMeARoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.create_account.*
import kotlinx.android.synthetic.main.homefragment.*
import java.net.URL

class home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        val college = intent.getStringExtra("college")
        val admin = intent.getStringExtra("admin")

        // setting variables to each of the fragment layout views
        val homeFragment = Homefragment()
        val mapFragment = Mapfragment()
        val reservationFragment = Reservationfragment()
        val profileFragment = Profilefragment()

        //sets the initial fragment for when first launched

        setCurrentFragment(homeFragment,username.toString(), email.toString(), college.toString())

        // bottom navigation bar click listener
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.Home -> setCurrentFragment(homeFragment,username.toString(), email.toString(), college.toString())
                R.id.Map -> setCurrentFragment(mapFragment,username.toString(), email.toString(), college.toString())
                R.id.Reservations -> setCurrentFragment(reservationFragment,username.toString(), email.toString(), college.toString())
                R.id.Profile -> setCurrentFragment(profileFragment,username.toString(), email.toString(), college.toString(), admin.toString())
            }
            true
        }
    }

    //function for navigation bar to change fragments when clicked
    private fun setCurrentFragment(fragment: Fragment, username: String, email:String, college:String, admin:String = ""){

        val mFragmentManager = supportFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = fragment

        val mBundle = Bundle()
        mBundle.putString("username", username)
        mBundle.putString("email", email)
        mBundle.putString("college", college)
        mBundle.putString("admin", admin)
        mFragment.arguments = mBundle
        mFragmentTransaction.replace(R.id.flFragment,mFragment).commit()

    }
}