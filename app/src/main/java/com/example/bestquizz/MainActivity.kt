package com.example.bestquizz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        super.addActionBarListeners()

        // On récupère le bouton
        val experienceButton = findViewById<View>(R.id.playButton) as Button
        // On crée un listener
        val experienceAction = View.OnClickListener {
            val activity_experience = Intent(this@MainActivity, PlayActivity::class.java)
            startActivity(activity_experience)
        }
        experienceButton.setOnClickListener(experienceAction)

        val easterEggBtn = findViewById<View>(R.id.creditsTxt)

        val easterAction = View.OnClickListener {
            val activity_experience = Intent(this@MainActivity, ScoreActivity::class.java)
            startActivity(activity_experience)
        }

        easterEggBtn.setOnClickListener(easterAction)
    }
}