package com.example.chatphotosapp.MyUi

import android.annotation.SuppressLint
import android.graphics.RectF
import android.graphics.Region
import android.icu.text.CaseMap.Title
import android.util.Log
import androidx.camera.core.processing.SurfaceProcessorNode.In
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatphotosapp.MainActivity
import com.example.chatphotosapp.Model.HistoryPicture
import com.example.chatphotosapp.Utils.CameraScreen
import com.example.chatphotosapp.Utils.LoadingView
import com.example.chatphotosapp.ViewModel.PictureViewModel



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: PictureViewModel) {
    //val isLoading by viewModel.isLoading.observeAsState()
    /*
    remember 会将对象存储在组合中,而如果在重组期间未再次调用之前调用 remember 的来源位置，则会忘记对象.
    如果下一次重组的时候调用了remember的来源位置，则重新开始
     */
    val cureentState : MutableState<Screen> = remember { mutableStateOf(Screen.HomeScreen) }
    val isLoading by viewModel.isLoading.observeAsState()
    Scaffold (
        //定义头部
        topBar = {

        },
        //底部导航
        bottomBar = {
            BottomView(currentScreen = cureentState)
        },
        //信息提示区
        snackbarHost = {

        },
    ) { innerPadding ->
        cureentState.value.loadScreen(innerPadding, viewModel)
        LoadingView(isLoading!!)
    }

}

@Composable
fun DisplayScreen(innerPadding: PaddingValues, title: String, backgroundColor: Color = Color.Transparent, vm: PictureViewModel) {
//    val regionList = remember {
//        listOf(
//            Region(),
//            Region(),
//            Region()
//        )
//    }
    //        val dogImage = ImageBitmap.imageResource(id = R.drawable.dog)
//        val customPainter = remember {
//            OverlayImagePainter(dogImage, rectList = mutableListOf(RectF(10f,10f,40f,40f), RectF(10f,50f,40f,80f), RectF(10f,90f,40f,120f)), regionList = regionList)
//        }
//        Image(
//            painter = customPainter,
//            contentDescription = "a dog",
//            contentScale = ContentScale.Fit,
//            modifier = Modifier.size(300.dp, 600.dp).pointerInput(Unit) {
//                detectTapGestures (onDoubleTap = {
//                    offset: Offset ->
//                    regionList.forEachIndexed { index, it->
//                        Log.d("zhuzihang", "top: ${it.bounds.top}"+"left: ${it.bounds.left}"+"right: ${it.bounds.right}")
//                        Log.d("zhuzihang", "x:${offset.x}, y:${offset.y}")
//                        if (it.contains(offset.x.toInt(), offset.y.toInt())) {
//                            Log.d("zhuzihang", "点击坐标，$offset,  在第${index+1} 矩形区域")
//                        }
//                    }
//                })
//            }
//        )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color.Transparent),
    ){
        CameraScreen(vm)
    }
}


sealed class Screen(val route:String, val title: String, val icon: ImageVector, val loadScreen: @Composable (innerPadding:  PaddingValues, vm : PictureViewModel) -> Unit) {
    object HomeScreen : Screen("home", "拍照", Icons.Default.Home, @Composable { innerPadding , vm ->
        DisplayScreen(innerPadding, "拍照", vm = vm)
    })
    object SettingScreen : Screen("history", "历史记录", Icons.Default.Favorite, @Composable { innerPadding , vm ->
       // val itemsOld = mutableListOf(HistoryPicture("https://photo.16pic.com/00/55/02/16pic_5502542_b.jpg", ""),
           // HistoryPicture("https://photo.16pic.com/00/87/11/16pic_8711080_b.jpg", ""), HistoryPicture("https://photo.16pic.com/00/72/58/16pic_7258484_b.png", ""), HistoryPicture("https://photo.16pic.com/00/40/16/16pic_4016072_b.jpg", ""))
        val items by vm.pictures.observeAsState()
        if (items == null) {
            vm.getHistory(5,0)
        }
        items?.let {
                if (it.isSuccess) {
                   historyColum(it.data.items)
                }
        }
    })
    object HlepScreen : Screen("个人中心", "个人中心", Icons.Default.AccountCircle, @Composable { innerPadding, _ ->
        ProfileScreen(modifier = Modifier.padding(innerPadding))
    })
}
@Composable
fun BottomView(currentScreen: MutableState<Screen>, modifier: Modifier = Modifier) {
    val screens = listOf(Screen.HomeScreen, Screen.SettingScreen, Screen.HlepScreen)
    NavigationBar (
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ){
        screens.forEach {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = it.route)
                },
                selected = true,
                onClick = {
                    currentScreen .value= it
                }
            )
        }
    }
}
