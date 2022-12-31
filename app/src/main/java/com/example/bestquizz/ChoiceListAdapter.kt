package com.example.bestquizz

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.bestquizz.model.Choice


class ChoiceListAdapter(private val choiceList : List<Choice>) : RecyclerView.Adapter<ChoiceListAdapter.ViewHolder>() {

    private lateinit var onClickListener: RecyclerViewClickListener

    fun setOnClickListener(listener: RecyclerViewClickListener) {
        onClickListener = listener
    }

    interface RecyclerViewClickListener {
        fun recyclerViewListClicked(v: Button, position: Int)
    }

    class ViewHolder(view: View, listener: RecyclerViewClickListener, private val choiceList : List<Choice>) : RecyclerView.ViewHolder(view) {
        val button: Button

        init {
            button = view.findViewById(R.id.choiceBtn)

            button.setOnClickListener{
                listener.recyclerViewListClicked(button, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.choice_button, parent, false), onClickListener, choiceList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.button.text = choiceList.get(position).choice
    }

    override fun getItemCount(): Int {
        return choiceList.size
    }
}