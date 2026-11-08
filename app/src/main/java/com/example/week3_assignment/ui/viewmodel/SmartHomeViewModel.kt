package com.example.week3_assignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week3_assignment.data.model.SmartDevice
import com.example.week3_assignment.data.repository.SmartDeviceRepository
import com.example.week3_assignment.ui.state.SmartHomeUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Akıllı Ev (IoT) Kontrol Merkezi ViewModel Katmanı.
 *
 * Mimari Kurallar:
 * - Kural 1 (Katman Ayrımı): Tüm cihaz filtreleme (.filter) ve aktif/online enerji tüketimi
 *   toplama (.sumOf) operasyonları yalnızca bu ViewModel katmanında gerçekleştirilir.
 * - Kural 2 (Performans & 120 FPS): Arama, oda süzme ve enerji hesaplama işlemleri
 *   Coroutines + StateFlow (combine + flowOn(Dispatchers.Default)) ile arka plan
 *   thread havuzunda (CPU-bound) asenkron olarak yürütülür; Main (UI) thread asla bloklanmaz.
 */
class SmartHomeViewModel(
    private val repository: SmartDeviceRepository = SmartDeviceRepository(),
    private val computationDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val _allDevices = MutableStateFlow<List<SmartDevice>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _selectedRoom = MutableStateFlow(SmartHomeUiState.ALL_ROOMS)

    private val _uiState = MutableStateFlow(SmartHomeUiState())
    val uiState: StateFlow<SmartHomeUiState> = _uiState.asStateFlow()

    init {
        observeReactiveStreams()
        loadInitialDevices()
    }

    private fun loadInitialDevices() {
        viewModelScope.launch {
            val initialList = repository.getInitialDevices()
            _allDevices.value = initialList
        }
    }

    /**
     * Cihaz listesi, arama sorgusu veya seçili oda değiştiğinde arka planda (Dispatchers.Default)
     * reaktif olarak süzme ve toplam enerji tüketimi hesaplamasını yapar.
     */
    private fun observeReactiveStreams() {
        combine(
            _allDevices,
            _searchQuery,
            _selectedRoom
        ) { devices, query, selectedRoom ->
            calculateUiState(
                devices = devices,
                query = query,
                selectedRoom = selectedRoom
            )
        }
            .flowOn(computationDispatcher)
            .onEach { newState ->
                _uiState.value = newState
            }
            .launchIn(viewModelScope)
    }

    private fun calculateUiState(
        devices: List<SmartDevice>,
        query: String,
        selectedRoom: String
    ): SmartHomeUiState {
        val normalizedQuery = query.trim()

        // Odalar listesini dinamik olarak çıkart ("Tümü", "Salon", "Bahçe"...)
        val rooms = buildList {
            add(SmartHomeUiState.ALL_ROOMS)
            addAll(devices.map { it.room }.distinct())
        }

        // 1. Arama ve Oda Filtrelemesi (Arka planda ViewModel katmanında yapılır)
        val filteredList = devices.filter { device ->
            val matchesRoom = selectedRoom == SmartHomeUiState.ALL_ROOMS ||
                device.room.equals(selectedRoom, ignoreCase = true)

            val matchesSearch = normalizedQuery.isEmpty() ||
                device.name.contains(normalizedQuery, ignoreCase = true)

            matchesRoom && matchesSearch
        }

        // 2. Reaktif Enerji Sayacı: Sadece o an aktif ve online olan cihazların anlık tüketimleri toplamı
        val activeOnlineDevices = devices.filter { device ->
            device.isOnline && device.isActive
        }
        val totalWattage = activeOnlineDevices.sumOf { device ->
            device.powerConsumptionWatts
        }
        val onlineDevicesCount = devices.count { it.isOnline }

        return SmartHomeUiState(
            searchQuery = query,
            selectedRoom = selectedRoom,
            availableRooms = rooms,
            filteredDevices = filteredList,
            totalActiveWattage = totalWattage,
            formattedTotalWattage = String.format(Locale.US, "%.1f Watt", totalWattage),
            activeOnlineDeviceCount = activeOnlineDevices.size,
            onlineDeviceCount = onlineDevicesCount,
            totalDeviceCount = devices.size
        )
    }

    /**
     * Kullanıcı arama çubuğuna metin girdiğinde StateFlow'u günceller.
     */
    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    /**
     * Kullanıcı oda filtre çiplerinden birini seçtiğinde StateFlow'u günceller.
     */
    fun onRoomSelected(room: String) {
        _selectedRoom.value = room
    }

    /**
     * Cihazın yanındaki Aç/Kapat butonuna basıldığında arka plan thread'inde (Dispatchers.Default)
     * cihazın durumunu değiştirir; StateFlow akışı sayesinde üstteki enerji sayacı anında güncellenir.
     */
    fun toggleDeviceStatus(deviceId: Int) {
        viewModelScope.launch {
            val updatedList = withContext(computationDispatcher) {
                _allDevices.value.map { device ->
                    if (device.id == deviceId && device.isOnline) {
                        device.copy(isActive = !device.isActive)
                    } else {
                        device
                    }
                }
            }
            _allDevices.update { updatedList }
        }
    }
}
