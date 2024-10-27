package com.example.chatphotosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.example.chatphotosapp.MyUi.showDetails
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class PlayerActivity :ComponentActivity() {
    val player  by lazy {
        ExoPlayer.Builder(this).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val imageUrl = intent.getStringExtra("imageUrl")
        val audioUrl = intent.getStringExtra("audioUrl")
        var text = intent.getStringExtra("text")
        if (text == null || text.length == 0) {
            text = "你好，中国";
        }
        setContent {
            if (imageUrl != null && audioUrl != null) {
                val mediaItem = MediaItem.fromUri(audioUrl)
                player.setMediaItem(mediaItem)
                showDetails(player,imageUrl,{
                    this.finish()
                },{
                    this.finish()
                }, text)
            }
        }
    }

}