package com.example.yatzy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yatzy.model.Player

@Composable
fun PlayerBottomBar(
    players: List<Player>,
    selectedIndex: Int,
    onPlayerSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.navigationBarsPadding()
            .background(Color.White)
    ) {
        players.forEachIndexed { index, player ->
            PlayerTab(
                player = player,
                isSelected = index == selectedIndex,
                onClick = { onPlayerSelected(index) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PlayerTab(
    player: Player,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Farben basierend auf dem Auswahl-Status
    val backgroundColor = if (isSelected) Color(0xFFFFF7ED) else Color.White
    val topBorderColor = if (isSelected) Color(0xFFEA580C) else Color.Transparent
    val contentColor = if (isSelected) Color(0xFFEA580C) else Color(0xFF6B7280)
    val badgeColor = if (isSelected) Color(0xFF8B5CF6) else Color(0xFFE5E7EB) // Lila vs. Grau
    val badgeTextColor = if (isSelected) Color.White else Color(0xFF4B5563)

    Column(
        modifier = modifier
            .background(backgroundColor)
            .drawBehind {
                drawLine(
                    color = topBorderColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 4.dp.toPx()
                )
            }
            .clickable { onClick() }
            .navigationBarsPadding()
            .padding(top = 12.dp, bottom = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = player.name,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(6.dp))

    }
}