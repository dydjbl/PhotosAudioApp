package com.example.chatphotosapp.MyUi

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun showDetails(player: ExoPlayer, imageUri: String, onDelete: () -> Unit, onBack: () -> Unit, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().verticalScroll(state = ScrollState(0)),
    ) {
        var playWhenReady = remember { mutableStateOf(true) }
        val lifecycleOwner = LocalLifecycleOwner.current
        var exoPlayer : ExoPlayer?= remember {
            player
        }
        val painter = rememberAsyncImagePainter(
            model = imageUri,
        )
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(300.dp) // 设置正方形的大小
                .clip(RectangleShape)
                .padding(10.dp), // 裁切为正方形
            contentScale = ContentScale.Fit // 设置裁切模式为Crop
        )
        AndroidView(
            factory = {context ->
                PlayerView(context).apply {
                    this.player = player
                }
            },
            modifier = Modifier.padding(10.dp).wrapContentWidth().height(100.dp)
        )
        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(10.dp),
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Button(onClick = onDelete) {
                Text("删除")
            }
            Button(onClick = onBack) {
                Text("返回")
            }
        }
        DisposableEffect (lifecycleOwner) {
            val observer = object : DefaultLifecycleObserver {
                override fun onResume(owner: LifecycleOwner) {
                    super.onResume(owner)
                    exoPlayer?.playWhenReady = playWhenReady.value
                    exoPlayer?.prepare()
                    exoPlayer?.play()
                }
                override fun onPause(owner: LifecycleOwner) {
                    super.onPause(owner)
                    exoPlayer?.stop()
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    exoPlayer?.release()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}