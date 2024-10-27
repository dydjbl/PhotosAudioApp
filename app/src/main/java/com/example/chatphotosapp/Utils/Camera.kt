package com.example.chatphotosapp.Utils

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.LifecycleOwner
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.example.chatphotosapp.MainActivity
import com.example.chatphotosapp.MyUi.PictureActions
import com.example.chatphotosapp.PlayerActivity
import com.example.chatphotosapp.R
import com.example.chatphotosapp.ViewModel.PictureViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.CamSelector
import com.ujizin.camposer.state.ImageCaptureResult
import com.ujizin.camposer.state.rememberCamSelector
import com.ujizin.camposer.state.rememberCameraState
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(viewModel: PictureViewModel) {
    val permissionsState = rememberMultiplePermissionsState(
        mutableListOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(android.Manifest.permission.POST_NOTIFICATIONS)
                add(android.Manifest.permission.READ_MEDIA_AUDIO)
                add(android.Manifest.permission.READ_MEDIA_VIDEO)
                add(android.Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    )

    LaunchedEffect(true) {
        if(!permissionsState.allPermissionsGranted && permissionsState.shouldShowRationale) {
            //这个请求是一个异步过程
            permissionsState.launchMultiplePermissionRequest()
        }
    }
    if (permissionsState.allPermissionsGranted) {
        previewPhotoAndPre(viewModel)
    } else {
        noPermisiionScreen(permissionsState)
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun noPermisiionScreen(cameraPermissionState: MultiplePermissionsState) {
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        val message: String = if (cameraPermissionState.shouldShowRationale){
            "未获取照相机权限导致无法使用照相机功能"
        } else {
            "请授权使用相机权限"
        }
        Text(message)
        Spacer(modifier = Modifier.size(2.dp, 8.dp))
        Button(
            onClick = { cameraPermissionState.launchMultiplePermissionRequest() }
        ) {
            Text("请求授权")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun previewPhotoAndPre(vm: PictureViewModel) {

    val cameraState = rememberCameraState()
    var camSelector by rememberCamSelector(CamSelector.Back)
    val context = LocalContext.current
    val uploadResult by vm.audio.observeAsState()
    //这个文件不要存在系统路径下面，不然会导致发送错误
    //存储在临时文件就好了
    //至于为什么有带研究，可能是后台不支持兼容相关格式
    val file = createTempFile("temp", ".jpg")
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    if (imageUri != null) {
        DisplayCapture(imageUri!!, imageUri?.toFile()!!, onConfirm = {
            vm.uploadImage(imageUri!!.toFile())
        }, onCancel = {
            imageUri = null
        })
    } else {
        CameraPreview(
            cameraState = cameraState,
            camSelector = camSelector,

            ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(600.dp))
                PictureActions(modifier = Modifier.fillMaxSize()
                    .padding(bottom = 32.dp, top = 16.dp),
                    isVideo = false,
                    onRecording = {},
                    onTakePicture = {
                        cameraState.takePicture(file) { result ->
                            when (result) {
                                is ImageCaptureResult.Success -> {
                                    imageUri = result.savedUri!!
                                }
                                is ImageCaptureResult.Error -> {}
                            }
                        }
                    },
                    onGalleryClick = {},
                    onSwitchCamera = {},
                    isRecording = false,
                    lastPicture = file
                ) {
                }
            }
        }
    }
    LaunchedEffect(uploadResult) {
        uploadResult?.let {
            if (it.isSuccess) {
                Log.d("zhuzihang", "进入音频播放器")
                Log.d("zhuzihang", "imageUrl: "+it.data.imageUrl)
                Log.d("zhuzihang", "audioUrl: "+it.data.audioUrl)
                //加载完成收到返回的音频url之后，跳转播放器
                val intent = Intent(context, PlayerActivity::class.java)
                intent.putExtra("imageUrl", it.data.imageUrl)
                intent.putExtra("audioUrl", it.data.audioUrl)
                intent.putExtra("text", it.data.text)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun DisplayCapture(uri:Uri,file: File, onConfirm: () -> Unit, onCancel: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // 显示拍照的图像
        val context = LocalContext.current
        val imageLoader = remember {
            ImageLoader.Builder(context)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .respectCacheHeaders(true)
                .crossfade(true)
                .build()
        }
        val painter = rememberAsyncImagePainter(
            model = uri,
            imageLoader = imageLoader
        )
        if (painter.state is AsyncImagePainter.State.Loading) {
            Image(
                painter = painterResource(id = R.drawable.dog),
                contentDescription = null,
                modifier = Modifier.wrapContentSize(),
                contentScale = ContentScale.Crop,
            )
        }
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .wrapContentWidth()
                .height(600.dp)
        )
        // 显示操作按钮
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Button(onClick = onConfirm) {
                Text("确认")
            }
            Button(onClick = onCancel) {
                Text("取消")
            }
        }
    }
}

