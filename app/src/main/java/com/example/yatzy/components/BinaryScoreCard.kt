package com.example.yatzy.components

import android.R.attr.description
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onScoreSelected: (Int?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Der klickbare Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = categoryName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Rechts: Wert oder Platzhalter ("-")
            if (currentValue != null) {
                Text(
                    text = currentValue.toString(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = if (currentValue == 0) Color(0xFF9CA3AF) else Color(0xFFEA580C),
                    modifier = Modifier.clickable {
                        onScoreSelected(null)
                        expanded = true
                    }
                )
            } else {
                Text(
                    text = "–",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
            }
        }

        // Der aufklappbare Inhalt
        AnimatedVisibility(visible = expanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ScoreActionButton(
                    label = "Success",
                    value = scoreValue.toString(),
                    icon = Icons.Default.Check,
                    isSelected = currentValue == scoreValue,
                    activeColor = Color(0xFF9333EA),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val nextValue = if (currentValue == scoreValue) null else scoreValue
                        onScoreSelected(nextValue)
                        expanded = false
                    }
                )

                ScoreActionButton(
                    label = "Fail",
                    value = "0",
                    icon = Icons.Default.Close,
                    isSelected = currentValue == 0,
                    activeColor = Color(0xFFD4183D),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val nextValue = if (currentValue == 0) null else 0
                        onScoreSelected(nextValue)
                        expanded = false
                    }
                )
            }
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