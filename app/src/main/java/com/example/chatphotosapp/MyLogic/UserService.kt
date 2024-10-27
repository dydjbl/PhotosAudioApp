package com.example.chatphotosapp.MyLogic

import com.example.chatphotosapp.Model.HistoryPictures
import com.example.chatphotosapp.Model.HttpWrap
import com.example.chatphotosapp.Model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/*
retrofit中，定义好相关去请求类型的接口即可发起请求
 */
interface UserService {
    //用户注册账号
    /*
    FormUrlEncoded这个注解表明请求实体是一个From表单，每个键值对都需要使用@Field注解
    FieldMap 这个注解用于设置不定个数的参数
     */
    @POST("user/register")
    fun regieterUser(@Body user: RequestBody) : Call<HttpWrap<User>>

    @POST("user/login")
    fun loginUser(@Body user: RequestBody) : Call<HttpWrap<User>>

}