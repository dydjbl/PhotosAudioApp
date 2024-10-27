package com.example.chatphotosapp.Utils


import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import androidx.core.graphics.times
import kotlin.math.roundToInt

class OverlayImagePainter constructor(
    private val image: ImageBitmap,
    private val rectList: List<RectF>,
    private val srcOffset: IntOffset = IntOffset.Zero,
    private val srcSize: IntSize = IntSize(image.width, image.height),
    private val regionList:List<Region>
    ):Painter(){
        private val size = validateSize(srcOffset,srcSize)

    val path = Path()
    override val intrinsicSize: Size
        get() = size.toSize()


    override fun DrawScope.onDraw() {
        drawImage(
            image, srcOffset, srcSize, dstSize = IntSize(this@onDraw.size.width.roundToInt(), this@onDraw.size.height.roundToInt())
        )
        /**
         * 这里遇到一个问题是当你在图像上画了多个框的画，要怎么分别对每个框进行监听，单纯通过坐标去判断点击范围的写法不适用于大量的复杂的图像框
         * 前端通用的做法是，设计一个不渲染在屏幕上的canvas，然后给不同区域填充不同颜色，拿到点击坐标之后放到这个canvas去获取对应严肃
         * android中提供了一种region的方法，一个用单个或多个矩形（Rect）通过一定方式组合在一起形成任意图案的区域范围的类。
         * 通过 Path 创建 Region ，这就意味着，我们可以完全复制一份我们绘制的内容生成 Region。
         * Region 有一个方法 contains(int x, int y) 可以用来判断参数中指定的坐标值是否在该 Region 范围内！
         */
        val scale = this@onDraw.size.width.roundToInt()/srcSize.width
        rectList.forEachIndexed { index, it ->
            path.addRoundRect(it.times(scale), 0.5f, 0.5f, Path.Direction.CW)
            regionList[index].setPath(path, Region(0, 0, this@onDraw.size.width.toInt(), this@onDraw.size.height.toInt()))
            drawPath(path.asComposePath(), color = Color.Red, style = Stroke(2.0f) )
            path.reset()
        }
    }

    private fun validateSize(srcOffset: IntOffset, srcSize: IntSize): IntSize {
        require(
            srcOffset.x >= 0 &&
                    srcOffset.y >= 0 &&
                    srcSize.width >= 0 &&
                    srcSize.height >= 0 &&
                    srcSize.width <= image.width &&
                    srcSize.height <= image.height
        )
        return srcSize
    }
}