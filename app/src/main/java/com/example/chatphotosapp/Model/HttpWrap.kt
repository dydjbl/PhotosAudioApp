package com.example.chatphotosapp.Model

import android.health.connect.datatypes.ExerciseRoute.Location
import org.w3c.dom.Text

data class HttpWrap<T>(val errorCode: Int, val data: T) {
    val isSuccess: Boolean
        get() = errorCode == 0
}

data class User(val uid: String, val userName: String)


data class HistoryPictures(val picturesNum: Int, val items:List<HistoryPicture>)

data class HistoryPicture(val imageUrl: String, val audioUrl:String, val text: String)