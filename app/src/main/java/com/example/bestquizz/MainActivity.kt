package com.example.bestquizz

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.bestquizz.model.*

class MainActivity : BaseActivity() {
    private var db: DBApp? = DBApp(this)
    private var sonQ : SonQuizz = SonQuizz()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        super.addActionBarListeners()

        // ---- class qui gère la bd en local ----
        val res : Cursor = db!!.getData()

        // ! ------ play son theme  -----
        sonQ.playTheme()
        // ------------------------------------------


        // ------------------ Score and pseudo ------------------------ //
        var listPlayer : List<Player> = db!!.getListUser(db!!.getData())

        var pseudo = ""
        var score = ""

        if(listPlayer.isEmpty()) {
            pseudo = "Loris El Crackito"
            score = "1 000 000"
        } else {
            pseudo = listPlayer[0].name
            score = listPlayer[0].bestScore.toString()
        }

        findViewById<TextView>(R.id.pseudoLabelHome).text = pseudo
        findViewById<TextView>(R.id.scoreLabelHome).text = score

        // ------------------ Intent vers le prochain activity ------------------------ //
        // On récupère le bouton
        val playButton = findViewById<View>(R.id.playButton) as Button
        // On crée un listener
        val playBtnAction = View.OnClickListener {
            val player = findViewById<EditText>(R.id.playerNameInput).text.toString()
            Log.w("MainPage", player + " / " + findViewById<EditText>(R.id.playerNameInput).text.toString())

            val playIntent = Intent(this@MainActivity, PlayActivity::class.java)
            // ! ------ play son clik exemple  -----
            sonQ.playSon(this@MainActivity,R.raw.click)
            // ------------------------------------------
            // ! ----- stop theme son ----------
            sonQ.stopTheme()
            // -------------------------------------
            // passage du nom du joueur
            playIntent.putExtra("Pseudo", player)
            startActivity(playIntent)
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

    override fun onDestroy() {
        // ! ----- libérer les ressources de l'audio ----------
        sonQ.destroy()
        // ---------------
        super.onDestroy()
    }
}