package com.example.bestquizz

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.bestquizz.model.QuestionEntity
import com.example.bestquizz.model.QuestionResponse
import com.example.bestquizz.network.ApiQuestion
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Response

open class BaseActivity : AppCompatActivity() {

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(findViewById(R.id.toolBar))
    }

    var difficulties = arrayOf("easy", "medium", "hard")
    var difficulty : String = ""
    val nbresponse : Int = 10
    val questionType : String = "multiple"
    val questionEncoding : String = ""
    lateinit var questionsValue : ArrayList<QuestionEntity>
    var questionIsLoad : Boolean = false

    var categories = arrayOf(
               "9| General Knowledge"
            , "10| Entertainment: Books"
            , "11| Entertainment: Film"
            , "12| Entertainment: Music"
            , "13| Entertainment: Musicals Theatres"
            , "14| Entertainment: Television"
            , "15| Entertainment: Video Games"
            , "16| Entertainment: Board Games"
            , "17| Science Nature"
            , "18| Science: Computers"
            , "19| Science: Mathematics"
            , "20| Mythology"
            , "21| Sports"
            , "22| Geography"
            , "23| History"
            , "24| Politics"
            , "25| Art"
            , "26| Celebrities"
            , "27| Animals"
            , "28| Vehicles"
           ,  "29| Entertainment: Comics"
            , "30| Science: Gadgets"
            , "31| Entertainment: Japanese Anime Manga"
            , "32| Entertainment: Cartoon Animations")
    var category : Int = 0

    fun goBackButtonListener() {
        // On récupère le bouton
        val backBtn = findViewById<View>(R.id.backButton) as CircleImageView
        // On crée un listener
        val goBackAction = View.OnClickListener {
            finish()
        }
        backBtn.setOnClickListener(goBackAction)
    }

    fun gameSettings() {
        // On récupère le bouton
        val settingsButton = findViewById<View>(R.id.profileIconButton) as CircleImageView
        // On crée un listener
        val experienceAction = View.OnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogLayout = layoutInflater.inflate(R.layout.game_settings,null)
            builder.setView(dialogLayout)

            // ajouter les listes déroulantes
            addDifficultySpinner(dialogLayout)
            addCategorySpinner(dialogLayout)

            builder.setPositiveButton("Close",
                DialogInterface.OnClickListener { dialog, id ->
                    //Toast.makeText(applicationContext, "Close " + difficulty + " " + category, Toast.LENGTH_SHORT).show()
                    loadQuestions()
                })

            builder.create()
            builder.show()
        }
        settingsButton.setOnClickListener(experienceAction)
    }

    fun addDifficultySpinner(dialogLayout : View) {
        val spinner: Spinner = dialogLayout.findViewById(R.id.difficultySpinner)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, difficulties)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                difficulty = difficulties[position]
                Log.w("difficulties", difficulty)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    fun addCategorySpinner(dialogLayout : View) {
        val spinner: Spinner = dialogLayout.findViewById(R.id.categorySpinner)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                category = categories[position].substringBefore('|', "invlaid").toInt()
                Log.w("categories", category.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
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
                Toast.makeText(this@BaseActivity, "Les questions n'ont pas pu être chargé", Toast.LENGTH_SHORT).show()

            }
        })
    }
}
