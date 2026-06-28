package com.example.yatzy.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UpperSectionSummary(
    subtotal: Int,
    bonus: Int
) {
    val pointsNeeded = 63 - subtotal
    val isBonusReached = bonus > 0

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        color = if (isBonusReached) Color(0xFFF0FDF4) else Color(0xFFF9FAFB),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isBonusReached) Color(0xFF22C55E) else Color(0xFFE5E7EB))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Upper Subtotal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    if (!isBonusReached) {
                        Text(
                            text = "$pointsNeeded points left until bonus",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    } else {
                        Text(
                            text = "Bonus reached!",
                            fontSize = 12.sp,
                            color = Color(0xFF16A34A),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Die Punkte-Anzeige
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$subtotal",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isBonusReached) Color(0xFF16A34A) else Color.Black
                    )
                    Text(
                        text = " / 63",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            // Bonus Anzeige, wenn erreicht
            if (isBonusReached) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0xFFDCFCE7)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Upper Bonus", fontWeight = FontWeight.Medium)
                    Text(text = "+35", fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                }
            }
        }
    }
}