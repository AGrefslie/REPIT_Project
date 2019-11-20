package com.example.repit_project.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(var question : String = "", var answer : String = "") : Parcelable