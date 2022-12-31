package com.example.bestquizz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.bestquizz.model.*
import com.example.bestquizz.network.ApiQuestion
import retrofit2.Call
import retrofit2.Response

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        super.addActionBarListeners()

        val player = findViewById<TextView>(R.id.playerNameInput).text.toString()

        // ------------------ Intent vers le prochain activity ------------------------ //
        // On récupère le bouton
        val playButton = findViewById<View>(R.id.playButton) as Button
        // On crée un listener
        val playBtnAction = View.OnClickListener {
            val activity_experience = Intent(this@MainActivity, PlayActivity::class.java)
            // passage du nom du joueur
            activity_experience.putExtra("Pseudo", player)
            activity_experience.putExtra("Timer", player)
            startActivity(activity_experience)
        }
        playButton.setOnClickListener(playBtnAction)

        // Easter Egg
        val easterEggBtn = findViewById<View>(R.id.creditsTxt)
        val easterAction = View.OnClickListener {
            val activity_experience = Intent(this@MainActivity, ScoreActivity::class.java)
            startActivity(activity_experience)
        }

        easterEggBtn.setOnClickListener(easterAction)
    }
}