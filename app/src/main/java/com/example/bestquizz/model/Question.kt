package com.example.bestquizz.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Question (
    val category : String,
    val type : String,
    val difficulty : String,
    val question : String,
    val correct_answer : String,
    val incorrect_answers : List<String>
)

data class QuestionResponse(@Json(name="results")
                            val result : List<Question>)