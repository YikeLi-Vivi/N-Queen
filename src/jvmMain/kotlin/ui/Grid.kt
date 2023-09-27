package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SafeGrid(size: Dp = 60.dp) {
    Box(
        modifier = Modifier.size(size).border(width = 2.dp, color = Color.Black).background(ColorTheme.SafeGrid)
    )
}

@Composable
fun NewUnsafeGrid(size: Dp = 60.dp) {
    Box(
        modifier = Modifier.size(size).border(width = 2.dp, color = Color.Black).background(ColorTheme.NewUnsafeGrid)
    )
}

@Composable
fun UnsafeGrid(size: Dp = 60.dp) {
    Box(
        modifier = Modifier.size(size).border(width = 2.dp, color = Color.Black).background(ColorTheme.UnsafeGrid)
    )
}

@Composable
fun QueenGrid(size: Dp = 60.dp) {
    Box(
        modifier = Modifier.size(size).border(width = 2.dp, color = Color.Black).background(ColorTheme.SafeGrid),
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

@Composable
fun Marker(
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(120.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SafeGrid(40.dp)
            Text("safe", fontSize = 13.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(120.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            UnsafeGrid(40.dp)
            Text("unsafe", fontSize = 13.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(120.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            NewUnsafeGrid(40.dp)
            Text("unsafe", fontSize = 13.sp)
        }
    }
}


@Preview
@Composable
fun Preview() {
    Row {
        SafeGrid()
        UnsafeGrid()
        QueenGrid()
    }
}