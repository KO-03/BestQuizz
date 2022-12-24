package com.example.bestquizz.model

data class Game(val player: Player, val questionsList : ArrayList<Question>,val nbOfQuestions : Int? = 20, val duration : Int? = 600)
