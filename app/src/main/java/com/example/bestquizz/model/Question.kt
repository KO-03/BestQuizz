package com.example.bestquizz.model

import android.os.Parcel
import android.os.Parcelable

data class Question (
    val category: String?,
    val type: String?,
    val difficulty: String?,
    val question: String?,
    val choiceList: ArrayList<Choice>
)