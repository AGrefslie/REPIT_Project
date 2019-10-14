package com.example.repit_project.Models

import com.google.firebase.auth.FirebaseUser

data class Quiz(var title : String, var description : String, var image : String, var public : Boolean,
                val questions : List<Question>/*, val user : FirebaseUser*/)

