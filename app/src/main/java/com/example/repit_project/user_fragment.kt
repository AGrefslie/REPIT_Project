package com.example.repit_project


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast


class user_fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Toast.makeText(activity, "Test Started", Toast.LENGTH_SHORT).show()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }


}
