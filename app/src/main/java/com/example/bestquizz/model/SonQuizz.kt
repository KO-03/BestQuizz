package com.example.bestquizz.model

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import java.io.IOException

class SonQuizz {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var themePlayer: MediaPlayer

    // resid_son => R.raw.click...
    fun playSon(c: Context?, resid_son: Int) {
        if(!this::mediaPlayer.isInitialized){
            mediaPlayer = MediaPlayer.create(c, resid_son)
        }
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            //return@OnClickListener
        }
        mediaPlayer!!.start()
    }

    fun playTheme(){
        val audioUrl = "https://bensound.com/bensound-music/bensound-ukulele.mp3"
        themePlayer = MediaPlayer()
        themePlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            themePlayer!!.setDataSource(audioUrl)
            themePlayer!!.prepare()
            themePlayer!!.start()
        }catch (e: IOException){
            //e.printStackTrace()
            Log.e("Erreur start","Erreur lors du lancement de l'audio")
        }
    }

    fun pauseSon() {
        if (mediaPlayer != null) {
            mediaPlayer!!.pause()
        }
    }

    fun pauseTheme(){
        if(this::themePlayer.isInitialized){
            themePlayer!!.stop()
            themePlayer!!.reset()
        }
    }
    fun stopTheme(){
        try {
            if(this::themePlayer.isInitialized){
                themePlayer!!.stop()
                themePlayer!!.release()
            }
        }catch (e: IllegalStateException){
            //e.printStackTrace()
            Log.e("Erreur stop theme","Erreur lors de l'arrêt du theme")
        }
    }
    fun stopPlayer() {
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer!!.release()
        }
    }

    fun destroy(){
        stopTheme()
        stopPlayer()
    }

}