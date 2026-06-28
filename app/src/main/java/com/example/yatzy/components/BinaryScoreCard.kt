package com.example.yatzy.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BinaryScoreCard(
    categoryName: String,
    scoreValue: Int,
    currentValue: Int?,
    onScoreSelected: (Int?) -> Unit // Ersetzt onSuccess und onFail
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = categoryName, fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Success Button (Lila/Blau)
            ScoreActionButton(
                label = "Success",
                value = scoreValue.toString(),
                icon = Icons.Default.Check,
                isSelected = currentValue == scoreValue,
                activeColor = Color(0xFF9333EA),
                modifier = Modifier.weight(1f),
                onClick = {
                    // Toggle-Logik: Wenn bereits Success, setze auf null, sonst auf scoreValue
                    val nextValue = if (currentValue == scoreValue) null else scoreValue
                    onScoreSelected(nextValue)
                }
            )

            // Fail Button (Rot)
            ScoreActionButton(
                label = "Fail",
                value = "0",
                icon = Icons.Default.Close,
                isSelected = currentValue == 0,
                activeColor = Color(0xFFD4183D),
                modifier = Modifier.weight(1f),
                onClick = {
                    // Toggle-Logik: Wenn bereits Fail, setze auf null, sonst auf 0
                    val nextValue = if (currentValue == 0) null else 0
                    onScoreSelected(nextValue)
                }
            )
        }
    }
}


@Composable
fun ScoreActionButton(
    label: String,
    value: String,
    icon: ImageVector,
    isSelected: Boolean,
    activeColor: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.height(70.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, if (isSelected) activeColor else Color(0xFFF3F3F5)),
        color = if (isSelected) activeColor.copy(alpha = 0.1f) else Color.White
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = if (isSelected) activeColor else Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(label, color = if (isSelected) activeColor else Color.Gray, fontSize = 12.sp)
            }
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (isSelected) activeColor else Color.Black)
        }
    }
}