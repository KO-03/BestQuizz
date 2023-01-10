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
import android.widget.Toast
import com.example.bestquizz.model.*
import com.example.bestquizz.network.ApiQuestion
import retrofit2.Call
import retrofit2.Response

class MainActivity : BaseActivity() {
    private var db: DBApp? = DBApp(this)
    val nbresponse : Int = 10
    val questionType : String = "multiple"
    val questionEncoding : String = ""
    lateinit var questionsValue : ArrayList<QuestionEntity>
    var questionIsLoad : Boolean = false

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        super.addActionBarListeners()

        loadQuestions()
        // ---- class qui gère la bd en local ----
        val res : Cursor = db!!.getData()

        // ! ------ play son theme  -----
        SonQuizz.playTheme()
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
            if(questionIsLoad){

                val playerName = findViewById<EditText>(R.id.playerNameInput).text.toString()

                if(playerName != "") {
                    Log.w(
                        "MainPage",
                        playerName + " / " + findViewById<EditText>(R.id.playerNameInput).text.toString()
                    )

                    val playIntent = Intent(this@MainActivity, PlayActivity::class.java)

                    // ! ------ play son clik exemple  -----
                    SonQuizz.playSon(this@MainActivity, R.raw.click)
                    // ------------------------------------------

                    // passage du nom du joueur
                    playIntent.putExtra("Pseudo", playerName)

                    val bundle = Bundle();
                    bundle.putParcelableArrayList("questions",questionsValue)
                    playIntent.putExtras(bundle)


                    startActivity(playIntent)
                }
                else{
                    Toast.makeText(this@MainActivity, "Le nom ne doit pas être vide", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this@MainActivity, "chargement des données", Toast.LENGTH_SHORT).show()

                loadQuestions()
            }
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

    fun loadQuestions(){
        questionIsLoad = false;
        // ------------------- API call -------------------
        // ------------------ questions request ------------------------ //

        val questionResponse = ApiQuestion.questionService.fetchQuestions(nbresponse,category,difficulty,questionType, questionEncoding)

        questionResponse.enqueue(object : retrofit2.Callback<QuestionResponse> {
            override fun onResponse(
                call: Call<QuestionResponse>,
                response: Response<QuestionResponse>
            ) {
                if(response.isSuccessful) {
                    Log.w("questions", ""+response.body())
                    val result = response.body()?.result
                    result?.let {
                        // use result val here
                        questionsValue = result as ArrayList<QuestionEntity>
                        questionIsLoad = true
                    }
                }
            }
            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                Log.e("failed", ""+ t.message)
                Toast.makeText(this@MainActivity, "Les questions n'ont pas pu être chargé", Toast.LENGTH_SHORT).show()

            }
        })
    }
    override fun onDestroy() {
        // ! ----- libérer les ressources de l'audio ----------
        SonQuizz.destroy()
        // ---------------
        super.onDestroy()
    }
}