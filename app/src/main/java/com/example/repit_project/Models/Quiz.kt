package com.example.repit_project.Models

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Exclude

data class Quiz(@get:Exclude var uid : String = "0",
                var title : String = "",
                var description : String = "",
                var image : String = "",
                var public : Boolean = false,
                val questions : List<Question> = emptyList(),
                val user : FirebaseUser)

