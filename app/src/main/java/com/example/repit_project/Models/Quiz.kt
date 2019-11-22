package com.example.repit_project.Models

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Quiz(@get:Exclude var uid: String = "",
                var title: String = "",
                var description: String = "",
                var image: String = "",
                var public: Boolean = false,
                val questions: List<Question> = emptyList(),
                val userId: String = "") : Parcelable

