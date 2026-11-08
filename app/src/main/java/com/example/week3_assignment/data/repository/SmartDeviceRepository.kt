package com.example.week3_assignment.data.repository

import com.example.week3_assignment.data.model.DeviceCategory
import com.example.week3_assignment.data.model.SmartDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Kurumsal MVVM Veri Katmanı (Repository).
 * Ödevde verilen Ham Veri Setini asenkron olarak (Dispatchers.IO) sağlar.
 */
class SmartDeviceRepository {

    suspend fun getInitialDevices(): List<SmartDevice> = withContext(Dispatchers.IO) {
        listOf(
            SmartDevice(
                id = 1,
                name = "Köşe Lambası",
                category = DeviceCategory.LIGHT,
                room = "Salon",
                isOnline = true,
                isActive = true,
                powerConsumptionWatts = 15.0
            ),
            SmartDevice(
                id = 2,
                name = "Tavan Avizesi",
                category = DeviceCategory.LIGHT,
                room = "Salon",
                isOnline = true,
                isActive = false,
                powerConsumptionWatts = 60.0
            ),
            SmartDevice(
                id = 3,
                name = "Bahçe Kamerası",
                category = DeviceCategory.CAMERA,
                room = "Bahçe",
                isOnline = true,
                isActive = true,
                powerConsumptionWatts = 8.5
            ),
            SmartDevice(
                id = 4,
                name = "Giriş Kamerası",
                category = DeviceCategory.CAMERA,
                room = "Antre",
                isOnline = true,
                isActive = true,
                powerConsumptionWatts = 8.5
            ),
            SmartDevice(
                id = 5,
                name = "Merkezi Termostat",
                category = DeviceCategory.THERMOSTAT,
                room = "Salon",
                isOnline = true,
                isActive = true,
                powerConsumptionWatts = 12.0
            ),
            SmartDevice(
                id = 6,
                name = "Yatak Odası Kliması",
                category = DeviceCategory.THERMOSTAT,
                room = "Yatak Odası",
                isOnline = true,
                isActive = false,
                powerConsumptionWatts = 1200.0
            ),
            SmartDevice(
                id = 7,
                name = "Mutfak Spotları",
                category = DeviceCategory.LIGHT,
                room = "Mutfak",
                isOnline = true,
                isActive = false,
                powerConsumptionWatts = 45.0
            ),
            SmartDevice(
                id = 8,
                name = "Roborock S7",
                category = DeviceCategory.VACUUM,
                room = "Koridor",
                isOnline = true,
                isActive = true,
                powerConsumptionWatts = 50.0
            ),
            SmartDevice(
                id = 9,
                name = "Balkon Aydınlatması",
                category = DeviceCategory.LIGHT,
                room = "Balkon",
                isOnline = false,
                isActive = false,
                powerConsumptionWatts = 25.0
            ),
            SmartDevice(
                id = 10,
                name = "Çalışma Odası Lambası",
                category = DeviceCategory.LIGHT,
                room = "Ofis",
                isOnline = true,
                isActive = true,
                powerConsumptionWatts = 12.0
            )
        )
    }
}
