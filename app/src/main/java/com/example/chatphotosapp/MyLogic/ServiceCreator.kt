package com.example.chatphotosapp.MyLogic

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.asResponseBody
import java.io.IOException
import java.util.concurrent.TimeUnit

object ServiceCreator {
    private const val url = "https://70046236dd.imdo.co/"
    val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)    // 设置读取超时时间
            .build()
        Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(service: Class<T>):T = retrofit.create(service)
    inline fun<reified  T> create() = create(T::class.java)
}

class CustomLoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body
        val buffer = okio.Buffer()
        requestBody?.writeTo(buffer)
        val requestBodyString = buffer.readUtf8()

        println("Sending request ${request.url}")
        println("Request headers: ${request.headers}")
        println("Request body: $requestBodyString")

        val response = chain.proceed(request)
        val responseBody = response.body
        val responseBodyString = responseBody?.string()

        println("Received response ${response.request.url}")
        println("Response headers: ${response.headers}")
        println("Response body: $responseBodyString")

        // Re-create the response before returning it
        return response.newBuilder()
            .body(okio.Buffer().writeUtf8(responseBodyString!!).asResponseBody(responseBody.contentType()))
            .build()
    }
}