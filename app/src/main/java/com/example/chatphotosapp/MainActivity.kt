package com.example.chatphotosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.chatphotosapp.MyUi.MainScreen
import com.example.chatphotosapp.ViewModel.PictureViewModel
import com.example.compose.AppTheme

class MainActivity : ComponentActivity() {
    val pictureViewModel by lazy {
        ViewModelProvider(this).get(PictureViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme  {
                val uid = intent.getStringExtra("uid")
                pictureViewModel.uid = uid?:""
                MainScreen(pictureViewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Android")
    }
}