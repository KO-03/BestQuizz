package com.example.bestquizz.model

import android.os.Parcel
import android.os.Parcelable

data class Quiz(val playerName: String?, val questions: ArrayList<Question>, val nbOfQuestions: Int? = 10, val timer: Int? = 30)
