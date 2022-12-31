package com.example.bestquizz

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bestquizz.model.*
import com.example.bestquizz.network.ApiQuestion
import retrofit2.Call
import retrofit2.Response

class PlayActivity : BaseActivity() {
    private var choiceList : ArrayList<Choice> = ArrayList()
    private lateinit var choiceAdapter : ChoiceListAdapter
    private lateinit var quiz: Quiz
    private lateinit var player: String
    private lateinit var questions: ArrayList<Question>

    private var questionIndex: Int = 0
    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        super.addActionBarListeners()

        // recuperer le extra du Intent
        player = this.intent.getStringExtra("Pseudo").toString()

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

/*
        for (i in questions.indices) {
            questionIndex = i
            //
            rafraichirInterface()
            //lancerChrono()

        }
*/


    }

    fun rafraichirInterface() {
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
                Toast.makeText(this@PlayActivity, "$position + et ", Toast.LENGTH_LONG).show()
                //verifierResponse(position, v)

                if(choiceList.get(position).isCorrect) {
                    Log.w("Button", choiceList.get(position).isCorrect.toString())
                }

                choiceList.get(position).isCorrect

            }
        })

        val recyclerView : RecyclerView = findViewById(R.id.quizChoiceList)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@PlayActivity)
            adapter = myAdapter
        }
    }

    fun verifierResponse(position: Int, button : Button) {
        if(choiceList.get(position).isCorrect) {
        }
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