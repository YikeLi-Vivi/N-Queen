package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import viewModel.ViewModel

@Composable
fun NumButton(
    boardModel: ViewModel,
    minusEnabled: Boolean = true,
    plusEnabled: Boolean = true,
    count: Int
) {
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
        Button(
            onClick = boardModel::minus,
            enabled = minusEnabled,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x00FFFFFF))
        ) {
            Text(
                "-",
                style = MaterialTheme.typography.h5
            )
        }
        Text(count.toString(), style = MaterialTheme.typography.h5)
        Button(
            onClick = boardModel::plus,
            enabled = plusEnabled,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x00FFFFFF))
        ) {
            Text(
                "+",
                style = MaterialTheme.typography.h5
            )
        }
    }
}
