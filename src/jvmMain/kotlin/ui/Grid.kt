package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun SafeGrid(size: Dp = 60.dp) {
    Box(
        modifier = Modifier.size(size).border(width = 2.dp, color = Color.Black).background(Color(0xFFB5C99A))
    )
}

@Composable
fun NewUnsafeGrid(size: Dp = 60.dp) {
    Box(
        modifier = Modifier.size(size).border(width = 2.dp, color = Color.Black).background(Color(0xFF862B0D))
    )
}

@Composable
fun UnsafeGrid(size: Dp = 60.dp) {
    Box(
        modifier = Modifier.size(size).border(width = 2.dp, color = Color.Black).background(Color(0x99862B0D))
    )
}

@Composable
fun QueenGrid(size: Dp = 60.dp) {
    Box(
        modifier = Modifier.size(size).border(width = 2.dp, color = Color.Black).background(Color(0xFFFFF9C9)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource("Queen.png"),
            contentDescription = null,
            modifier = Modifier.size(size).background(Color.LightGray),
            contentScale = ContentScale.Fit
        )
    }
}


@Preview
@Composable
fun Preview(){
    Row {
        SafeGrid()
        UnsafeGrid()
        QueenGrid()
    }
}