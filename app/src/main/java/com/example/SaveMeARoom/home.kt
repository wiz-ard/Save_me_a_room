package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.homefragment.*

class home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // setting variables to each of the fragment layout views
        val homeFragment = Homefragment()
        val mapFragment = Mapfragment()
        val reservationFragment = Reservationfragment()
        val profileFragment = Profilefragment()

        //sets the initial fragment for when first launched
        setCurrentFragment(homeFragment)

        // bottom navigation bar click listener
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.Home -> setCurrentFragment(homeFragment)
                R.id.Map -> setCurrentFragment(mapFragment)
                R.id.Reservations -> setCurrentFragment(reservationFragment)
                R.id.Profile -> setCurrentFragment(profileFragment)
            }
            true
        }



    }

    //function for navigation bar to change fragments when clicked
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.flFragment, fragment)
            commit()
        }

}