
package com.example.hydrosync.shared

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable


@Serializable
data class HydroSyncStatus(
    val device: String,
    val connected: Boolean,
    val plant: PlantStatus
)

@Serializable
data class PlantStatus(
    val moisture: Int
)

class HydroSyncApi {
    private val client = createPlatformHttpClient()

    suspend fun getStatus(): HydroSyncStatus {
        return client.get("http://10.0.0.48/api/status").body()
    }

    fun close() {
        client.close()
    }
}
