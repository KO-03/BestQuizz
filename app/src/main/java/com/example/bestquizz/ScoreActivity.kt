package com.example.bestquizz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bestquizz.model.DBApp
import com.example.bestquizz.model.Player
import com.example.bestquizz.model.SonQuizz

class ScoreActivity : BaseActivity() {
    private var db: DBApp? = DBApp(this)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        super.goBackButtonListener()
        // ------------------ Intent vers le prochain activity ------------------------ //
        // On récupère le bouton
        val mainMenuButton = findViewById<View>(R.id.mainMenuBtn) as Button
        // On crée un listener
        val mainMenuBtnAction = View.OnClickListener {
            SonQuizz.playSon(this@ScoreActivity,R.raw.click)

            val back_main_experience = Intent(this@ScoreActivity, MainActivity::class.java)
            startActivity(back_main_experience)
        }
        mainMenuButton.setOnClickListener(mainMenuBtnAction)
        val player = this.intent.getExtras()

            Log.e("Score player ", player.toString())
        if (player != null) {
           /* val pseudo = this.intent.getStringExtra("Pseudo")
            Log.e("Score player ", pseudo.toString())*/
            player.getString("Pseudo")?.let { Log.e("Score player 1", it) }
            player.getInt("Score")?.let { Log.e("Score player 2", it.toString()) }
        }

        var pseudo = player?.getString("Pseudo")
        var score = player?.getInt("Score")

        findViewById<TextView>(R.id.scoreEndGame).setText(pseudo)
        findViewById<TextView>(R.id.pseudoEndGame).setText(score.toString())

        var compliment: String = ""

        when (score) {
            in 0..40 -> compliment = "BIEN TENTÉ"
            in 41..59 -> compliment = "PLUTOT BON"
            in 60..80 -> compliment = "TRES BON"
            in 81..100 -> compliment = "EXCELLENT"
        }
        findViewById<TextView>(R.id.complimentScore).setText(compliment)


        // ------ on affiche le score des joueurs
        var listPlayer : List<Player> = db!!.getListUser(db!!.getData())
        Log.e("list player ",listPlayer.toString())
        //Toast.makeText(this@ScoreActivity, listPlayer.toString(), Toast.LENGTH_LONG).show()

        // ----- Affichage du score des joueurs ----------
        val recyclerView : RecyclerView = findViewById(R.id.leaderBoardList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LeaderListAdapter(listPlayer)
        // _____________________________________________________

                /*   val linearLayout = findViewById<LinearLayout>(R.id.leaderLayout)
                // ------------------ Placeholder pour le leader board qui n'affiche pas encore les données
                for (player in listPlayer) {
                    linearLayout.addView(LayoutInflater.from(this).inflate(R.layout.leader_board_item, null))
                }*/

                /*    --------------------  Recycler view qui ne fonctionne pas
                        val myAdapter = LeaderListAdapter(listPlayer)
                        val linearLayoutManager = LinearLayoutManager(this)

                        val recyclerView : RecyclerView = findViewById(R.id.leaderBoardList)
                        with(recyclerView) {
                            layoutManager = linearLayoutManager
                            adapter = myAdapter
                        }
                */


        // ---------- partager de données sur une autre App ----------------
                // On initialise l'élément à envoyer

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Voici mon score sur le BestQuizz App : " + score.toString() + " pts. \nL'app est dispo prochainement sur Android et iOS !!")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)

                // ***************** ajouter un boutton ici ************
                val shareButton = findViewById<View>(R.id.shareBtn) as Button
                // On crée un listener
                val shareAction = View.OnClickListener {
                    startActivity(shareIntent) // On appelle l'élément pour l'envoie
                }
                shareButton.setOnClickListener(shareAction)
                // _________________________________________________________

        SonQuizz.playTheme()

    }
    override fun onDestroy() {
        SonQuizz.destroy()
        super.onDestroy()
    }

}