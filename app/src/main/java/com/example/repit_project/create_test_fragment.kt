package com.example.repit_project


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.repit_project.Models.Quiz
import com.google.firebase.firestore.FirebaseFirestore


class create_test_fragment : Fragment() {

    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_test, container, false)
        
    }


}
