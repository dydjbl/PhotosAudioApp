package com.example.chatphotosapp.MyLogic

import com.example.chatphotosapp.Model.HistoryPicture
import com.example.chatphotosapp.Model.HistoryPictures
import com.example.chatphotosapp.Model.HttpWrap
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface PictureService {
    //查看用户历史数据
    @GET("picture/history")
    fun getHistoryUserData(@Query("uid") userid : Int, @Query("picturesNum") picturesNum: Int, @Query("index") index : Int): Call<HttpWrap<HistoryPictures>>

    /*
    @Multipart:表示请求实体是一个支持文件上传的表单，需要配合@Part和@PartMap使用，适用于文件上传
    @Part:用于表单字段，适用于文件上传的情况，@Part支持三种类型：RequestBody、MultipartBody.Part、任意类型
     */
    //, @Part picture: MultipartBody.Part
    @Multipart
    @POST("picture/handle")
    fun handlePic(@Part("uid") uid: RequestBody, @Part("name") name:RequestBody, @Part file: MultipartBody.Part) : Call<HttpWrap<HistoryPicture>>
}