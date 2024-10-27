package com.example.chatphotosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.chatphotosapp.MyUi.initLoginView
import com.example.chatphotosapp.ViewModel.LoginViewModel
import com.example.compose.AppTheme

class LoginActivity : ComponentActivity(){
    private val loginViewModel by lazy{
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface (
                    tonalElevation = 5.dp,
                ) {
                    initLoginView(loginViewModel)
                }
            }
        }
    }
}