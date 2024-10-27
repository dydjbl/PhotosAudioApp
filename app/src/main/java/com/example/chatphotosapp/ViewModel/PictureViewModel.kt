package com.example.chatphotosapp.ViewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatphotosapp.Model.HistoryPicture
import com.example.chatphotosapp.Model.HistoryPictures
import com.example.chatphotosapp.Model.HttpWrap
import com.example.chatphotosapp.MyLogic.Network
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.io.encoding.ExperimentalEncodingApi

class PictureViewModel:ViewModel() {
    val audio = MutableLiveData<HttpWrap<HistoryPicture>>()
    val pictures = MutableLiveData<HttpWrap<HistoryPictures>>()
    val isLoading = MutableLiveData<Boolean>(false)
    var uid :String = "1"
    fun uploadImage(file:File) {
        isLoading.value = true
        viewModelScope.launch {
            val uidPart = uid.toRequestBody("text/plain".toMediaTypeOrNull())
            val namePart = "zhuzihang".toRequestBody("text/plain".toMediaTypeOrNull())
            val fileResquestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val picturePart = MultipartBody.Part.createFormData("picture", file.name, fileResquestBody)
            audio.postValue(Network.handlePicture(uidPart, namePart, picturePart))
            isLoading.value = false
        }
    }

    fun getHistory(picturesNum: Int, index: Int) {
        isLoading.value = true
        viewModelScope.launch {
            val wrap = Network.getHistoryPicture(uid.toInt(), picturesNum, index)
            pictures.value = wrap
            println("zhuzihang")
            pictures.value?.data?.items?.forEach {
                println(it.imageUrl)
            }
            isLoading.value = false
        }
    }
    fun createBinaryRequestBody(fileBytes: ByteArray): RequestBody {
        return RequestBody.create("application/octet-stream".toMediaTypeOrNull(), fileBytes)
    }
    // 将图片文件转换为 Base64 字符串
    @OptIn(ExperimentalEncodingApi::class)
    fun convertFileToBase64(file: File): String {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val byteArrayOutputStream = ByteArrayOutputStream()

        // 压缩图片到字节数组
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        // 将字节数组转换为 Base64 编码字符串
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}