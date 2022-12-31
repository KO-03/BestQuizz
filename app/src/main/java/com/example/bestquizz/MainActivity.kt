package com.example.bestquizz

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.bestquizz.model.*
import com.example.bestquizz.network.ApiQuestion
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class MainActivity : BaseActivity() {
    private var db: DBApp? = null
    private var sonQ : SonQuizz = SonQuizz()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        super.addActionBarListeners()

        db = DBApp(this)
        val res : Cursor = db!!.getData()
        // ! ------ play son theme  -----
        sonQ.playTheme()
        // ------------------------------------------

        // ------------------ questions request ------------------------ //
        var questionsValue : List<Question> = listOf();
        val questionResponse = ApiQuestion.questionService.fetchQuestions(10,11,"easy","multiple")

        questionResponse.enqueue(object : retrofit2.Callback<QuestionResponse> {

            override fun onResponse(
                call: Call<QuestionResponse>,
                response: Response<QuestionResponse>
            ) {
                if(response.isSuccessful) {

                    Log.e("questions", ""+response.body())
                    val result = response.body()?.result
                    result?.let {
                        // use result val here
                        questionsValue = result;

                    }
                }
            }
            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                Log.e("failed", ""+ t.message)
            }

        })

        // On récupère le bouton
        val experienceButton = findViewById<View>(R.id.playButton) as Button
        // On crée un listener
        val experienceAction = View.OnClickListener {
            val activity_experience = Intent(this@MainActivity, PlayActivity::class.java)
            //activity_experience.putExtra("login", userName)

            // ! ------ play son clik exemple  -----
            sonQ.playSon(this@MainActivity,R.raw.click)
            // ------------------------------------------
            // ! ----- stop theme son ----------
            sonQ.stopTheme()
            // -------------------------------------
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

    fun getListUser(res: Cursor): List<Player> {
        var listPlayer : List<Player> = listOf();
        try {
            while (res.moveToNext()) {
                listPlayer += Player(res.getString(0),res.getString(1))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listPlayer;
    }
    override fun onDestroy() {
        // ! ----- libérer les ressources de l'audio ----------
        sonQ.destroy()
        // ---------------
        super.onDestroy()
    }

}