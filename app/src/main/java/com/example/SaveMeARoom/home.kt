package com.example.SaveMeARoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_home.*

class home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeFragment = Homefragment()
        val mapFragment = Mapfragment()
        val reservationFragment = Reservationfragment()
        val profileFragment = Profilefragment()

        setCurrentFragment(homeFragment)

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

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.flFragment, fragment)
            commit()
        }

}