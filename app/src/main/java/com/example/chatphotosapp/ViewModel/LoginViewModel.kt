package com.example.chatphotosapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatphotosapp.Model.HttpWrap
import com.example.chatphotosapp.Model.User
import com.example.chatphotosapp.MyLogic.Network
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val saveName = MutableLiveData<String>()
    val savePassword = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>(false)
    val isLogin = MutableLiveData<Boolean>(true)

    val userResult = MutableLiveData<HttpWrap<User>>()

    fun userLoginOrUser(userName:String?, userPassword:String?) {
        isLoading.value = true
        if (userName == null || userPassword == null || userName.isEmpty() || userPassword.isEmpty()) {
            isLoading.value = false
        } else {
            viewModelScope.launch {
                if (isLogin.value == true) {
                    userResult.postValue(Network.loginUser(userName, userPassword))
                } else {
                    userResult.postValue(Network.registerUser(userName, userPassword))
                }
                isLoading.value = false
            }
        }
    }

    fun testLiveData(){
        
    }
}