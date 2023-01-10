package com.example.bestquizz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bestquizz.model.*
import de.hdodenhof.circleimageview.CircleImageView

class PlayActivity : BaseActivity() {
    private var choiceList : ArrayList<Choice> = ArrayList()
    private lateinit var quiz: Quiz
    private lateinit var player: String
    private lateinit var questions: ArrayList<Question>

    private var questionIndex: Int = 0
    private var answered: Boolean = false
    private var score: Int = 0
    private lateinit var nextBtn: CircleImageView
    private lateinit var timer: TextView
    private var db: DBApp? = DBApp(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        super.goBackButtonListener()

        // ! ----- stop theme son ----------
        SonQuizz.pauseTheme()
        // -------------------------------------

        // recuperer le extra du Intent
        player = this.intent.getStringExtra("Pseudo").toString()
        timer = findViewById(R.id.timer)

        Log.w("ExtraIntent", player)

        nextBtn = findViewById(R.id.nextButton)
        nextBtn.isClickable = false
        nextBtn.alpha = .5f

        // ------------- on récupère la liste des questions
        val bundleObject : Bundle? = getIntent().extras
        val questionlist : ArrayList<QuestionEntity>? = bundleObject!!.getParcelableArrayList<QuestionEntity>("questions")

        if (questionlist != null) {
            quizGame(questionlist)
        }

    }

    fun quizGame(questionsValue : List<QuestionEntity>) {
        /* Init */
        // optional nb questions and timer in seconds
        quiz = Quiz(player, mapQuestionList(questionsValue), questionsValue.size, 30)
        questions = quiz.questions

        rafraichirInterface()
        nextBtn.setOnClickListener{
            SonQuizz.playSon(this@PlayActivity,R.raw.click)

            if(questionIndex < quiz.nbOfQuestions!! - 1) {
                //Toast.makeText(applicationContext, "NEXT", Toast.LENGTH_SHORT).show()
                questionIndex++
                nextBtn.isClickable = false
                rafraichirInterface()
            } else {
                SonQuizz.playSon(this@PlayActivity,R.raw.end_party)

                Toast.makeText(applicationContext, "FIN", Toast.LENGTH_SHORT).show()
                val activity_experience = Intent(this@PlayActivity, ScoreActivity::class.java)
                // passage du nom du joueur
                val bundle = Bundle()

                bundle.putString("Pseudo", player)
                bundle.putInt("Score", this.score)

                activity_experience.putExtras(bundle)
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

        lancerChrono()

        val myAdapter = ChoiceListAdapter(choiceList)

        myAdapter.setOnClickListener(object : ChoiceListAdapter.RecyclerViewClickListener{
            override fun recyclerViewListClicked(v: Button, position: Int) {
                if(!answered) {
                    if(choiceList[position].isCorrect) {
                        SonQuizz.playSon(this@PlayActivity,R.raw.good_answer)
                        v.setTextColor(Color.GREEN)
                        score+=10
                        findViewById<TextView>(R.id.score).text = score.toString()
                    } else {
                        SonQuizz.playSon(this@PlayActivity,R.raw.bad_answer)
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

    fun lancerChrono() {
        var counter = 30
        SonQuizz.playSon(this@PlayActivity,R.raw.chrono)

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if(!answered) {
                    timer.text = counter.toString()
                    counter--
                } else {
                    this.cancel()
                }
            }
            override fun onFinish() {
                timer.text = "Time's up !"
                answered = true
                nextBtn.alpha = 1f
                nextBtn.isClickable = true
                SonQuizz.playSon(this@PlayActivity,R.raw.bad_answer)
            }
        }.start()
    }

    fun mapQuestionList(questionEntities : List<QuestionEntity>) : ArrayList<Question>{
        var questionsList : ArrayList<Question> = arrayListOf()
        var newChoiceList : ArrayList<Choice> = arrayListOf()
        var question : String = ""
        var choiceFormatted : String = ""

        for(questionEntity in questionEntities) {
            // Add all incorrect answers
            questionEntity.incorrect_answers.forEach{
                choiceFormatted = HtmlCompat.fromHtml(it, FROM_HTML_MODE_LEGACY).toString()
                newChoiceList.add(Choice(choiceFormatted, false))
            }
            question = questionEntity.question
            question = HtmlCompat.fromHtml(question, FROM_HTML_MODE_LEGACY).toString()

            // add correct answer
            choiceFormatted = HtmlCompat.fromHtml(questionEntity.correct_answer, FROM_HTML_MODE_LEGACY).toString()
            newChoiceList.add(Choice(choiceFormatted, true))
            newChoiceList.shuffle()
            questionsList.add(Question(questionEntity.category, questionEntity.type, questionEntity.difficulty, question, newChoiceList))
            newChoiceList = arrayListOf()
        }
        return questionsList
    }

    override fun onDestroy() {
        SonQuizz.destroy()
        super.onDestroy()
    }

}