package com.example.chatphotosapp.MyLogic

import com.example.chatphotosapp.Model.HttpWrap
import com.example.chatphotosapp.Model.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


object Network {
    private val userService = ServiceCreator.create<UserService>()
    private val pictureService = ServiceCreator.create<PictureService>()
    suspend fun loginUser(userName: String, password: String): HttpWrap<User> {
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("userName", userName)
        jsonObject.put("password", password)
        val requestBody = jsonObject.toString().toRequestBody(JSON)
        return userService.loginUser(requestBody).await()
    }
    suspend fun registerUser(userName: String, password: String): HttpWrap<User> {
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("userName", userName)
        jsonObject.put("password", password)
        val requestBody = jsonObject.toString().toRequestBody(JSON)
        return userService.regieterUser(requestBody).await()
    }
    suspend fun handlePicture(uid: RequestBody, name:RequestBody, picture:MultipartBody.Part) = pictureService.handlePic(uid, name, picture).await()
    suspend fun getHistoryPicture(uid: Int, picturesNum: Int, index: Int) = pictureService.getHistoryUserData(uid, picturesNum, index).await()
    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine { continuation ->
            //object : Callback<T> 这一行代码的意思是一个实现了Callback的接口
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body!=null)continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}