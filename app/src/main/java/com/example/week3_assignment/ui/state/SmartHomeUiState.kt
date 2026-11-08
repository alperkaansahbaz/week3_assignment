package com.example.week3_assignment.ui.state

import androidx.compose.runtime.Immutable
import com.example.week3_assignment.data.model.SmartDevice

/**
 * Compose UI katmanının tek doğruluk kaynağı (Single Source of Truth) olan Immutable durum modeli.
 * Tüm matematiksel hesaplamalar ve filtreleme sonuçları ViewModel tarafından önceden hesaplanıp
 * buraya hazır olarak aktarılır; böylece Compose UI katmanı %100 stateless (durumsuz) kalır.
 */
@Immutable
data class SmartHomeUiState(
    val searchQuery: String = "",
    val selectedRoom: String = ALL_ROOMS,
    val availableRooms: List<String> = listOf(ALL_ROOMS),
    val filteredDevices: List<SmartDevice> = emptyList(),
    val totalActiveWattage: Double = 0.0,
    val formattedTotalWattage: String = "0.0 Watt",
    val activeOnlineDeviceCount: Int = 0,
    val onlineDeviceCount: Int = 0,
    val totalDeviceCount: Int = 0
) {
    companion object {
        const val ALL_ROOMS = "Tümü"
    }
}
