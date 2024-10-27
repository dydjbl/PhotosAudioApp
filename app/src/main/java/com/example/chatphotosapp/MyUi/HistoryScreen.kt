package com.example.chatphotosapp.MyUi

import android.content.Intent
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.chatphotosapp.Model.HistoryPicture
import com.example.chatphotosapp.PlayerActivity
import com.example.chatphotosapp.R
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


private fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed{
    val offsetX = remember {
       Animatable(0f)
    }
    pointerInput(Unit){
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            while (true) {
                //如果接收向下轻触事件的同时，动画还在进行，则对其进行拦截
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                offsetX.stop()
                val velocityTracker = VelocityTracker()
                awaitPointerEventScope {
                    horizontalDrag(pointerId) {
                        change ->
                        //将动画值和触摸事件的拖动进行一个同步
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch {
                            offsetX.snapTo(horizontalDragOffset)
                        }
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                //如果偏移了这么多，则动画要马上结束
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    if (targetOffsetX.absoluteValue <= size.width) {
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        offsetX.animateDecay(velocity, decay)
                        onDismissed()
                    }
                }
            }
        }
    }
        .offset {
            IntOffset(offsetX.value.roundToInt(), 0)
        }
}
@Composable
fun historyColum(items: List<HistoryPicture>) {
//    val items = mutableListOf(HistoryPicture("https://photo.16pic.com/00/55/02/16pic_5502542_b.jpg", ""),
//        HistoryPicture("https://photo.16pic.com/00/87/11/16pic_8711080_b.jpg", ""), HistoryPicture("https://photo.16pic.com/00/72/58/16pic_7258484_b.png", ""), HistoryPicture("https://photo.16pic.com/00/40/16/16pic_4016072_b.jpg", ""))
    val context = LocalContext.current
    var selectedItem by remember {
        mutableStateOf<Int?>(null)
    }
    val state = rememberLazyListState()
    Column (modifier = Modifier.fillMaxWidth().padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            style = MaterialTheme.typography.headlineLarge,
            text = "历史记录",
            textAlign = TextAlign.Center
        )
        LazyColumn (
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(0.dp, 30.dp)
        ){
            items(items.size, key = { items[it].imageUrl }) { index ->
                cardPicture(items[index]) {
                    selectedItem = index
                }
            }
        }
    }
    LaunchedEffect(selectedItem) {
        selectedItem?.let {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("imageUrl", items[selectedItem!!].imageUrl)
            intent.putExtra("audioUrl", items[selectedItem!!].audioUrl)
            intent.putExtra("text", items[selectedItem!!].text)
            context.startActivity(intent)
            selectedItem = null
        }
    }
}

@Composable
fun cardPicture(item : HistoryPicture, onclick: () -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row (modifier = Modifier
        .padding(12.dp)
        .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium))
    ){
        Column (
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ){
            Text(text = "图片", style = MaterialTheme.typography.titleLarge)
            val painter = rememberAsyncImagePainter(item.imageUrl)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentWidth().height(100.dp).clickable {
                        onclick()
                    },
                contentScale = ContentScale.Fit,
            )
            if (expanded) {
                Text(
                    text = item.text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        IconButton(
            onClick = {expanded = !expanded},
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                contentDescription = if (expanded) stringResource(R.string.less) else stringResource(R.string.more)
            )
        }
    }
}
