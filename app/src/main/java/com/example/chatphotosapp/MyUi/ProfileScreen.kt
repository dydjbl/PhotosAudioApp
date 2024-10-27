package com.example.chatphotosapp.MyUi

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatphotosapp.R

@Preview
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    Column (
        /*
        verticalScroll需要创建一个滑动状态对象去接受滑动状态
         */
        modifier = modifier.verticalScroll(rememberScrollState())
    ){
        Spacer(Modifier.height(16.dp))
        SearchBar(Modifier.padding(horizontal = 16.dp))
        ProfileHeaderSection()
        BodySection(
            title = R.string.restore
        ) {
            AlignBodyRow()
        }
        BodySection(
            title = R.string.favorite
        ) {
            FavoriteCollection()
        }
        Spacer(Modifier.height(16.dp))
    }
}
@Composable
fun ProfileHeaderSection(
    isOwnProfile: Boolean = true,
    isFollowing: Boolean = true,
    onEditClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onMessageClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Image(ImageBitmap.imageResource(R.drawable.dog), contentDescription = "header",contentScale = ContentScale.Crop, modifier = Modifier.clip(
            CircleShape).size(100.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.offset(
                x = if (isOwnProfile) {
                    (30.dp) / 2f
                } else 0.dp
            )
        ) {
            Text(
                text = "zhuzihang",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.width(30.dp))
            IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.edit)
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            IconButton(
                onClick = onLogoutClick,
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.logout)
                )
            }
        }

    }

}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {Icons.Default.Search},
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text(text = stringResource(R.string.search))
        },
        modifier = modifier.fillMaxWidth().heightIn(min = 56.dp),
    )
}

@Composable
fun BodyElement(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier,
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = "一张图片",
            modifier = Modifier.size(88.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = stringResource(text),
            modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun collectionCard(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier,
) {
    Surface (
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row (
            modifier = Modifier.size(255.dp, 80.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Image(
                painter = painterResource(drawable),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = stringResource(text),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)

private val alignBodyData = listOf(DrawableStringPair(R.drawable.ab1_inversions, R.string.inversion),DrawableStringPair(R.drawable.ab1_inversions, R.string.inversion),DrawableStringPair(R.drawable.ab1_inversions, R.string.inversion),DrawableStringPair(R.drawable.ab1_inversions, R.string.inversion),DrawableStringPair(R.drawable.ab1_inversions, R.string.inversion),DrawableStringPair(R.drawable.ab1_inversions, R.string.inversion),DrawableStringPair(R.drawable.ab1_inversions, R.string.inversion))
@Composable
fun AlignBodyRow(
    modifier: Modifier = Modifier
) {
    /*
    contentPadding和padding的不同在于，后者相当于把column的可视范围缩小了
    而前者只是设置了第一个列表项和最后一个列表项距离column的距离
     */
    LazyRow (
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ){
        items(alignBodyData) {
            item ->
            BodyElement(item.drawable, item.text)
        }
    }
}

@Composable
fun FavoriteCollection(
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = modifier.height(168.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(alignBodyData) {
            item ->
            collectionCard(item.drawable, item.text)
        }
    }
}

@Composable
fun BodySection(
    @StringRes title : Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column (modifier){
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )
        content()
    }
}

