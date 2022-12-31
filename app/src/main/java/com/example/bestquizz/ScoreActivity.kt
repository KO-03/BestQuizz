package com.example.bestquizz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.bestquizz.model.DBApp
import com.example.bestquizz.model.Player

class ScoreActivity : BaseActivity() {
    private var db: DBApp? = DBApp(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        super.addActionBarListeners()

        // ------------------ Intent vers le prochain activity ------------------------ //
        // On récupère le bouton
        val mainMenuButton = findViewById<View>(R.id.mainMenuBtn) as Button
        // On crée un listener
        val mainMenuBtnAction = View.OnClickListener {
            val back_main_experience = Intent(this@ScoreActivity, MainActivity::class.java)
            startActivity(back_main_experience)
        }
        mainMenuButton.setOnClickListener(mainMenuBtnAction)

        findViewById<TextView>(R.id.scoreEndGame).setText(this.intent.getStringExtra("Score").toString())
        findViewById<TextView>(R.id.pseudoEndGame).setText(this.intent.getStringExtra("Pseudo").toString())

        // ------ on affiche le core des joueurs
        var listPlayer : List<Player> = db!!.getListUser(db!!.getData())
        Log.e("list player ",listPlayer.toString())
        Toast.makeText(this@ScoreActivity, listPlayer.toString(), Toast.LENGTH_LONG).show()

    }
}