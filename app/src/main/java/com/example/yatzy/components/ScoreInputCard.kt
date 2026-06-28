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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun ScoreInputCard(
    categoryName: String,
    possibleScores: List<Int>,
    currentValue: Int?,
    onScoreSelected: (Int?) -> Unit,
    onCrossOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 1. Header-Reihe: Titel, Beschreibung und die große Zahl rechts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = categoryName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // HIER IST DIE ÄNDERUNG: Die > 0 Bedingung ist weg.
            // Bei 0 wird die Zahl grau, bei Punkten wird sie orange.
            if (currentValue != null) {
                Text(
                    text = currentValue.toString(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = if (currentValue == 0) Color(0xFF9CA3AF) else Color(0xFFEA580C)
                )
            }
        }

        // 2. Die Zahlen-Reihe
        Row(
            modifier = Modifier.fillMaxWidth(),
            // Add Alignment.CenterHorizontally here:
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            possibleScores.forEach { score ->
                ScoreNumberButton(
                    number = score,
                    isSelected = currentValue == score,
                    onClick = {
                        val nextValue = if (currentValue == score) null else score
                        onScoreSelected(nextValue)
                    }
                )
            }
        }

        // 3. Der "Cross Out" Button
        Button(
            onClick = {
                // HIER IST DIE ÄNDERUNG: Toggle-Logik für das Streichen
                if (currentValue == 0) {
                    onScoreSelected(null) // Zurücksetzen, wenn bereits gestrichen
                } else {
                    onCrossOut() // Normal streichen
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF3F3F5),
                contentColor = Color(0xFF1F2937)
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
    var inputValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(text = categoryName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            if (currentValue != null) {
                Text(
                    text = currentValue.toString(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = if (currentValue == 0) Color(0xFF9CA3AF) else Color(0xFFEA580C),
                    modifier = Modifier.clickable {
                        // HIER IST DIE NEUE LOGIK:
                        // Wenn der Wert größer als 0 ist, schreiben wir ihn zurück ins Textfeld.
                        // War es ein "Cross Out" (0), lassen wir das Feld leer.
                        inputValue = if (currentValue > 0) currentValue.toString() else ""

                        // Danach geben wir das Feld wieder frei
                        onScoreSelected(null)
                    }
                )
            }
        }

        if (currentValue == null) {
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
                        unfocusedBorderColor = Color(0xFFD1D5DB),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Button(
                    onClick = {
                        val score = inputValue.toIntOrNull()
                        if (score != null) {
                            onScoreSelected(score)
                            // Das Feld wird erst geleert, wenn die Eingabe erfolgreich war.
                            // Wenn der User es rückgängig macht, füllt die Logik oben es wieder auf.
                            inputValue = ""
                        }
                    },
                    enabled = inputValue.isNotBlank(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEA580C),
                        disabledContainerColor = Color(0xFFF3F3F5)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Bestätigen", tint = Color.White)
                }
            }

            Button(
                onClick = {
                    onCrossOut()
                    inputValue = "" // Beim Streichen das eventuell halb getippte Feld leeren
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3F3F5),
                    contentColor = Color(0xFF1F2937)
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(12.dp)
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