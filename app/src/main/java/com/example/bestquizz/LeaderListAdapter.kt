package com.example.bestquizz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bestquizz.model.Choice
import com.example.bestquizz.model.Player


class LeaderListAdapter(private val leaderList : List<Player>) : RecyclerView.Adapter<LeaderListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /*
        val rank: TextView = view.findViewById(R.id.rankLeaderBoard)
        val pseudo: TextView = view.findViewById(R.id.pseudoLeaderBoard)
        val bestScore: TextView = view.findViewById(R.id.scoreLeaderBoard)
*/
        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_score, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*
        holder.rank.text = position.toString()
        holder.pseudo.text = leaderList[position].name
        holder.bestScore.text = leaderList[position].bestScore.toString()
        */
    }

    override fun getItemCount(): Int {
        return leaderList.size
    }
}