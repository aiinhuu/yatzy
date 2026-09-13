package com.example.yatzy.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScoreInputCard(
    categoryName: String,
    possibleScores: List<Int>,
    currentValue: Int?,
    onScoreSelected: (Int?) -> Unit,
    onCrossOut: () -> Unit
) {
    // Speichert den Zustand: Ausgeklappt (true) oder Eingeklappt (false)
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp) // Etwas weniger Padding für ein kompakteres Listen-Gefühl
    ) {
        // 1. Der klickbare Header (Kategoriename + Aktueller Punktestand)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded } // Klappt auf/zu beim Anklicken
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

            // Rechte Seite des Headers: Punkte bzw. Platzhalter
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (currentValue != null) {
                    Text(
                        text = currentValue.toString(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = if (currentValue == 0) Color(0xFF9CA3AF) else Color(0xFFEA580C)
                    )
                } else {
                    // Platzhalter, solange noch keine Zahl eingetragen wurde
                    Text(
                        text = "–",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }
        }

        // 2. Der aufklappbare Inhalt (Zahlen und Cross Out)
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                // Die Zahlen-Reihe
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    possibleScores.forEach { score ->
                        ScoreNumberButton( // Hier wird dein bereits vorhandener runder Button aufgerufen
                            number = score,
                            isSelected = currentValue == score,
                            onClick = {
                                val nextValue = if (currentValue == score) null else score
                                onScoreSelected(nextValue)
                                expanded = false // Schließt das Menü automatisch nach Auswahl
                            }
                        )
                    }
                }

                // Der Cross Out Button
                Button(
                    onClick = {
                        if (currentValue == 0) {
                            onScoreSelected(null)
                        } else {
                            onCrossOut()
                            expanded = false // Schließt das Menü automatisch beim Streichen
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Cross Out",
                            fontWeight = FontWeight.Medium,
                            color = if (currentValue == 0) Color.Red else Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreNumberButton(
    number: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(width = 62.dp, height = 54.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, if (isSelected) Color(0xFFEA580C) else Color(0xFFD1D5DB)),
        // Hintergrundfarbe: Komplett Orange wenn ausgewählt, sonst Weiß
        color = if (isSelected) Color(0xFFEA580C) else Color.White
    ) {
        // Box.fillMaxSize() erlaubt uns, Elemente absolut darin zu positionieren
        Box(modifier = Modifier.fillMaxSize()) {
            // Die Zahl zentriert in der Mitte
            Text(
                text = number.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                // Textfarbe: Weiß wenn ausgewählt, sonst Schwarz
                color = if (isSelected) Color.White else Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )

            // Das kleine Häkchen oben rechts (nur anzeigen, wenn ausgewählt)
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp)
                        .size(14.dp)
                )
            }
        }
    }
}

@Composable
fun NumericInputCard(
    categoryName: String,
    currentValue: Int?,
    onScoreSelected: (Int?) -> Unit,
    onCrossOut: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var inputValue by remember { mutableStateOf("") }

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
                        inputValue = if (currentValue > 0) currentValue.toString() else ""
                        onScoreSelected(null)
                        expanded = true // Beim Zurücksetzen direkt wieder aufklappen
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
            Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) inputValue = newValue
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Sum", color = Color.LightGray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFEA580C),
                            unfocusedBorderColor = Color(0xFFD1D5DB)
                        )
                    )

                    Button(
                        onClick = {
                            val score = inputValue.toIntOrNull()
                            if (score != null) {
                                onScoreSelected(score)
                                inputValue = ""
                                expanded = false
                            }
                        },
                        enabled = inputValue.isNotBlank(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C))
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                    }
                }

                Button(
                    onClick = {
                        onCrossOut()
                        inputValue = ""
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Cross Out", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}