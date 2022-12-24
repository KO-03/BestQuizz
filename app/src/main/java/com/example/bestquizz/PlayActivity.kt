package com.example.bestquizz

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bestquizz.model.Choice

class PlayActivity : BaseActivity() {
    private var choiceList : ArrayList<Choice> = ArrayList()
    private lateinit var choiceAdapter : ChoiceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        super.addActionBarListeners()

        choiceList.add(Choice("Option 1", false))
        choiceList.add(Choice("Option 2", false))
        choiceList.add(Choice("Option 3", true))
        choiceList.add(Choice("Option 4", false))


        val recyclerView : RecyclerView = findViewById(R.id.quizChoiceList)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@PlayActivity)
            adapter = ChoiceListAdapter(choiceList)
        }
    }
}