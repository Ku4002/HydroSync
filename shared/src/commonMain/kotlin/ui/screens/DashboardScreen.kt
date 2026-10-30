
package ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydrosync.shared.HydroSyncApi
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun DashboardScreen(
    onHistoryClick: () -> Unit,
    onPlantClick: () -> Unit
) {
    var moisture by remember { mutableStateOf<Int?>(null) }
    var connectionMessage by remember { mutableStateOf("Connecting to ESP32...") }

    val api = remember { HydroSyncApi() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            try {
                val status = api.getStatus()
                moisture = status.plant.moisture
                connectionMessage =
                    if (status.connected) "Connected to ESP32"
                    else "ESP32 reports disconnected"
            } catch (e: Exception) {
                moisture = null
                connectionMessage = "Couldn't connect to ESP32"
            }

            kotlinx.coroutines.delay(5000.milliseconds)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "HydroSync",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🌱 Soil Moisture", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = moisture?.let { "$it%" } ?: "--",
                    fontSize = 26.sp
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(connectionMessage)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onHistoryClick() }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📝 Recent Activity", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Text("No recent watering activity yet.")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPlantClick() }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("View plant details")
            }
        }
    }
}
