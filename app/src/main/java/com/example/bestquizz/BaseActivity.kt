package com.example.bestquizz

import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import de.hdodenhof.circleimageview.CircleImageView

open class BaseActivity : AppCompatActivity() {
    var category : Int = 11
    var difficulty : String = "easy"
    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(findViewById(R.id.toolBar))
    }

    fun addActionBarListeners() {
        // On récupère le bouton
        val backBtn = findViewById<View>(R.id.backButton) as CircleImageView
        // On crée un listener
        val goBackAction = View.OnClickListener {
            finish()
        }
        backBtn.setOnClickListener(goBackAction)

        // On récupère le bouton
        val profileButton = findViewById<View>(R.id.profileIconButton) as CircleImageView
        // On crée un listener
        val experienceAction = View.OnClickListener {
            Toast.makeText(applicationContext, "Profile click", Toast.LENGTH_SHORT).show()
        }
        profileButton.setOnClickListener(experienceAction)
    }
}