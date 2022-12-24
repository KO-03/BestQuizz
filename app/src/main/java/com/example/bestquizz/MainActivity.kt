package com.example.bestquizz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bestquizz.network.ApiQuestion
import com.example.bestquizz.network.QuestionResponse
import retrofit2.Call
import retrofit2.Response

class
MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // On récupère le bouton
        val experienceButton = findViewById<View>(R.id.button) as Button
        // On crée un listener
        val experienceAction = View.OnClickListener {
            val activity_experience = Intent(this@MainActivity, PlayActivity::class.java)
            startActivity(activity_experience)
        }
        experienceButton.setOnClickListener(experienceAction)

    }
}