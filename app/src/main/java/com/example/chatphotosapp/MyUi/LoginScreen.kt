package com.example.chatphotosapp.MyUi

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.chatphotosapp.MainActivity
import com.example.chatphotosapp.R
import com.example.chatphotosapp.Utils.LoadingView
import com.example.chatphotosapp.ViewModel.LoginViewModel
import kotlin.math.log

/*
* 登录页图标
* */
@Composable
fun LoginIcon() {
    Image(
        painter = painterResource(id = R.drawable.dog),
        contentDescription = "登录图标",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .clip(CircleShape)
            .border(2.dp, colorResource(id = android.R.color.black), CircleShape)
    )
}

/*
分割线
 */
@Composable
fun VSpacer(height: Int) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
    )
}

/*
账号和密码输入框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputEdit(hint: String, isHided: Boolean = false, isPasswordInput: Boolean, vm:LoginViewModel) {
    var textState = if (isPasswordInput) vm.savePassword.observeAsState() else vm.saveName.observeAsState()
    TextField(
        visualTransformation = if (isHided) { PasswordVisualTransformation()} else {
            VisualTransformation.None},
        value = textState.value ?: "",
        onValueChange = {
            if (isPasswordInput) {
                vm.savePassword.value = it
            } else {
                vm.saveName.value = it
            }
        },
        modifier = Modifier
            .width(260.dp)
            .height(56.dp),
        label = {
            Text(if (isPasswordInput) "密码" else "用户名")
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Previous,
            keyboardType = KeyboardType.Password
        ),
        placeholder = { Text(text = hint, modifier = Modifier.alpha(0.5f)) }
    )
}

/*
登录按钮
 */

@Composable
fun LoginButton(vm:LoginViewModel) {
    val context = LocalContext.current
    Button(
        onClick = {
            vm.userLoginOrUser(vm.saveName.value, vm.savePassword.value)
        }
    ) {
        if (vm.isLogin.value == true) {
            Text(text = "登录")
        } else {
            Text(text = "注册")
        }
    }
}

/*
注册按钮和重置密码按钮
 */
@Composable
fun ResetButton(vm: LoginViewModel){
    Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(70.dp, 5.dp)) {
        Text("注册账号",fontSize = 16.sp, color = Color.Gray, modifier = Modifier.clickable {
            vm.isLogin.value = false
        })
        Text("忘记密码",fontSize = 16.sp, color = Color.Gray)
    }
}
/*
注册返回按钮
 */
@Composable
fun backLogin(vm: LoginViewModel) {
    Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(70.dp, 5.dp)) {
        Text("返回登录",fontSize = 16.sp, color = Color.Gray, modifier = Modifier.clickable {
            vm.isLogin.value = true
        })
        Text("帮助中心",fontSize = 16.sp, color = Color.Gray)
    }
}
/*
初始化登录界面函数
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun initLoginView(loginVM: LoginViewModel) {
    val isLoading by loginVM.isLoading.observeAsState()
    val isLogin by loginVM.isLogin.observeAsState()
    val userResult by loginVM.userResult.observeAsState()
    if (isLogin == true) {
        Box (modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){
            Column (
                modifier = Modifier
                    .wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                LoginIcon()
                VSpacer(30)
                Text(text = "欢迎登录账号使用最新功能", fontWeight = FontWeight.Bold)
                VSpacer(30)
                InputEdit(hint = "请输入用户名",false, false, loginVM)
                VSpacer(5)
                InputEdit(hint = "请输入密码", true, true, loginVM)
                ResetButton(loginVM)
                VSpacer(10)
                LoginButton(loginVM)
            }
            LoadingView(isLoading!!)
        }
    } else {
        Box (modifier = Modifier.fillMaxSize()){
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginIcon()
                VSpacer(30)
                Text(text = "欢迎注册账号使用最新功能", fontWeight = FontWeight.Bold)
                VSpacer(30)
                InputEdit(hint = "请输入注册用户名",false, false, loginVM)
                VSpacer(5)
                InputEdit(hint = "请输入注册密码", true, true, loginVM)
                backLogin(loginVM)
                VSpacer(10)
                LoginButton(loginVM)
            }
            LoadingView(isLoading!!)
        }
    }
    val context = LocalContext.current
    LaunchedEffect(userResult) {
        if (userResult == null) {
        }else if (userResult!!.isSuccess) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("uid", userResult?.data?.uid)
            context.startActivity(intent)
        } else if (userResult!!.errorCode == 1){
            Toast.makeText(context, "认证失败", Toast.LENGTH_SHORT).show()
        } else if (userResult!!.errorCode == 2){
            Toast.makeText(context, "注册用户已存在", Toast.LENGTH_SHORT).show()
        }
    }
}

