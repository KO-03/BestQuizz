package com.example.bestquizz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bestquizz.model.*
import com.example.bestquizz.network.ApiQuestion
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Response

class PlayActivity : BaseActivity() {
    private var choiceList : ArrayList<Choice> = ArrayList()
    private lateinit var choiceAdapter : ChoiceListAdapter
    private lateinit var quiz: Quiz
    private lateinit var player: String
    private lateinit var questions: ArrayList<Question>

    private var questionIndex: Int = 0
    private var answered: Boolean = false
    private var score: Int = 0
    private lateinit var nextBtn: CircleImageView
    private var db: DBApp? = DBApp(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        super.addActionBarListeners()

        // recuperer le extra du Intent
        player = this.intent.getStringExtra("Pseudo").toString()

        Log.w("ExtraIntent", player)

        nextBtn = findViewById(R.id.nextButton)
        nextBtn.isClickable = false
        nextBtn.alpha = .5f

        // ------------------ questions request ------------------------ //
        var questionsValue : List<QuestionEntity>
        val questionResponse = ApiQuestion.questionService.fetchQuestions(10,11,"easy","multiple")

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
                        questionsValue = result

                        quizGame(questionsValue)
                    }
                }
            }
            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                Log.e("failed", ""+ t.message)
            }
        })
    }

    fun quizGame(questionsValue : List<QuestionEntity>) {
        /* Init */
        // optional nb questions and timer in seconds
        quiz = Quiz(playerName = player, questions = mapQuestionList(questionsValue), questionsValue.size, 30)
        questions = quiz.questions

        rafraichirInterface()
        nextBtn.setOnClickListener{

            if(questionIndex < quiz.nbOfQuestions!! - 1) {
                //Toast.makeText(applicationContext, "NEXT", Toast.LENGTH_SHORT).show()
                questionIndex++
                nextBtn.isClickable = false
                rafraichirInterface()
            } else {
                Toast.makeText(applicationContext, "FIN", Toast.LENGTH_SHORT).show()
                val activity_experience = Intent(this@PlayActivity, ScoreActivity::class.java)
                // passage du nom du joueur
                activity_experience.putExtra("Pseudo", player)
                activity_experience.putExtra("Score", score)
                // ------ create User ----------
                val checkInsertData =
                    db!!.savePlayer(player, score)
                startActivity(activity_experience)
            }
        }
    }

    fun rafraichirInterface() {
        answered = false
        nextBtn.alpha = .5f
        nextBtn.isClickable = false

        // changer theme
        findViewById<TextView>(R.id.quizTheme).setText(questions.get(questionIndex).category)
        // changer numéro
        findViewById<TextView>(R.id.quizProgress).setText((questionIndex + 1).toString() + "/" + quiz.nbOfQuestions.toString())
        // changer la question
        findViewById<TextView>(R.id.quizQuestion).setText(questions.get(questionIndex).question)

        // changer les choix
        choiceList.clear()
        choiceList.addAll(questions.get(questionIndex).choiceList)

        val myAdapter = ChoiceListAdapter(choiceList)

        myAdapter.setOnClickListener(object : ChoiceListAdapter.RecyclerViewClickListener{
            override fun recyclerViewListClicked(v: Button, position: Int) {
                if(!answered) {
                    if(choiceList[position].isCorrect) {
                        v.setTextColor(Color.GREEN)
                        score+=10
                        findViewById<TextView>(R.id.score).setText(score.toString())
                    } else {
                        v.setTextColor(Color.RED)
                    }
                    nextBtn.alpha = 1f
                    nextBtn.isClickable = true
                }
                answered = true
            }
        })

        val recyclerView : RecyclerView = findViewById(R.id.quizChoiceList)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@PlayActivity)
            adapter = myAdapter
        }
    }

    fun verifierResponse(button : Button, position: Int,) {
    }

    fun lancerChrono() {
        TODO("Timmer")
    }

    fun mapQuestionList(questionEntities : List<QuestionEntity>) : ArrayList<Question>{
        var questionsList : ArrayList<Question> = arrayListOf()
        var newChoiceList : ArrayList<Choice> = arrayListOf()

        for(questionEntity in questionEntities) {
            questionEntity.incorrect_answers.forEach{
                newChoiceList.add(Choice(it, false))
            }
            newChoiceList.add(Choice(questionEntity.correct_answer, true))
            newChoiceList.shuffle()
            questionsList.add(Question(category = questionEntity.category, type = questionEntity.type, difficulty = questionEntity.difficulty, question = questionEntity.question, choiceList = newChoiceList))
            newChoiceList = arrayListOf()
        }
        return questionsList
    }
}