package com.example.SaveMeARoom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button

class AdminHomefragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.admin_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resReq: Button? = view?.findViewById(R.id.resReq)
        val statReq: Button? = view?.findViewById(R.id.statReq)

        resReq?.setOnClickListener(){
            val intent = Intent(activity, RoomRequests::class.java)
            startActivity(intent)
        }
    }

    private fun resButtonClicked(view: View) {

    }

    private fun statButtonClicked(view: View) {

    }
}
