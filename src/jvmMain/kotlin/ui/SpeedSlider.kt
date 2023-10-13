package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SpeedSlider(
    value: Float = 0.5F,
    onValueChange: (Float) -> Unit = {},
) {
    Row(horizontalArrangement = Arrangement.SpaceAround) {
        Text("delay-")
        Slider(
            value = value, onValueChange = onValueChange, modifier = Modifier.width(250.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTickColor = Color.Black,
                activeTrackColor = Color.Black,
                inactiveTickColor = Color.White,
                inactiveTrackColor = Color.White
            )
        )
        Text("delay+")
    }

}

@Preview
@Composable
private fun PreviewSpeedSlider() {
    SpeedSlider()
}