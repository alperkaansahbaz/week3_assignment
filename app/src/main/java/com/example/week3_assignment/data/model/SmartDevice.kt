package com.example.week3_assignment.data.model

import androidx.compose.runtime.Immutable
import java.util.Locale

enum class DeviceCategory(val code: String, val displayName: String) {
    LIGHT(code = "LIGHT", displayName = "Aydınlatma"),
    CAMERA(code = "CAMERA", displayName = "Kamera"),
    THERMOSTAT(code = "THERMOSTAT", displayName = "Termostat / İklimlendirme"),
    VACUUM(code = "VACUUM", displayName = "Robot Süpürge")
}

@Immutable
data class SmartDevice(
    val id: Int,
    val name: String,
    val category: DeviceCategory,
    val room: String,
    val isOnline: Boolean,
    val isActive: Boolean,
    val powerConsumptionWatts: Double
) {
    val formattedWattage: String
        get() = String.format(Locale.US, "%.1f Watt", powerConsumptionWatts)
}
