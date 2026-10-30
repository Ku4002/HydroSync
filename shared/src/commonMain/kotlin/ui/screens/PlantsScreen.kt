
package ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydrosync.data.plants
import com.example.hydrosync.data.wateringLogs
import com.example.hydrosync.model.Plant
import com.example.hydrosync.model.WateringLog

@Composable
fun PlantsScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Plants",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Button(onClick = onBackClick) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        plants.forEach { plant ->
            PlantCard(plant)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Watering History",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Soil moisture before and after watering",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        WateringGraph(wateringLogs)
    }
}

@Composable
private fun PlantCard(plant: Plant) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = plant.icon,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plant.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Last watered: ${plant.lastWatered}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${plant.moisture}%",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Moisture",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun WateringGraph(logs: List<WateringLog>) {
    val beforeColor = Color(0xFFFF9800)
    val afterColor = Color(0xFF2196F3)
    val thresholdColor = Color(0xFFE57373)
    val gridColor = Color.LightGray

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(10.dp)) {
                        drawCircle(beforeColor)
                    }
                    Spacer(modifier = Modifier.size(5.dp))
                    Text("Before", style = MaterialTheme.typography.bodySmall)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(10.dp)) {
                        drawCircle(afterColor)
                    }
                    Spacer(modifier = Modifier.size(5.dp))
                    Text("After", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Graph with labeled Y-axis
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Column(
                    modifier = Modifier
                        .height(200.dp)
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    for (value in 70 downTo 0 step 10) {
                        Text(
                            text = "$value%",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                ) {
                    if (logs.isEmpty()) return@Canvas

                    val maxHours = 30f
                    val maxMoisture = 70f
                    val graphHeight = size.height
                    val graphWidth = size.width

                    fun yPosition(value: Int): Float {
                        return graphHeight -
                                (value.coerceIn(0, 70) / maxMoisture * graphHeight)
                    }

                    // Horizontal gridlines and 40% threshold
                    for (value in 0..70 step 10) {
                        val y = yPosition(value)

                        drawLine(
                            color = if (value == 40) thresholdColor else gridColor,
                            start = Offset(0f, y),
                            end = Offset(graphWidth, y),
                            strokeWidth = if (value == 40) 2f else 1f
                        )
                    }

                    // Plot events according to elapsed time
                    logs.forEach { log ->
                        val x = (
                                (maxHours - log.hoursAgo.toFloat()) /
                                        maxHours
                                ) * graphWidth

                        val beforeY = yPosition(log.moistureBefore)
                        val afterY = yPosition(log.moistureAfter)

                        drawLine(
                            color = Color.Gray,
                            start = Offset(x, beforeY),
                            end = Offset(x, afterY),
                            strokeWidth = 3f
                        )

                        drawCircle(
                            color = beforeColor,
                            radius = 5f,
                            center = Offset(x, beforeY)
                        )

                        drawCircle(
                            color = afterColor,
                            radius = 5f,
                            center = Offset(x, afterY)
                        )
                    }
                }
            }

            // Labeled X-axis
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 42.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("30h", "24h", "18h", "12h", "6h", "Now").forEach {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "X-axis: Time before most recent watering",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Red line: 40% watering threshold",
                style = MaterialTheme.typography.bodySmall,
                color = thresholdColor
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Historical values are simulated demonstration data.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
