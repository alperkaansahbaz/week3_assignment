package com.example.week3_assignment

import com.example.week3_assignment.data.repository.SmartDeviceRepository
import com.example.week3_assignment.ui.viewmodel.SmartHomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SmartHomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_calculatesActiveOnlineEnergyCorrectly() = runTest(testDispatcher) {
        val viewModel = SmartHomeViewModel(
            repository = SmartDeviceRepository(),
            computationDispatcher = testDispatcher
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(10, state.filteredDevices.size)
        // Aktif ve Online olan cihazlar: 15.0 + 8.5 + 8.5 + 12.0 + 50.0 + 12.0 = 106.0 Watt
        assertEquals(106.0, state.totalActiveWattage, 0.01)
        assertEquals(6, state.activeOnlineDeviceCount)
    }

    @Test
    fun filterByRoomAndSearch_filtersDevicesInViewModel() = runTest(testDispatcher) {
        val viewModel = SmartHomeViewModel(
            repository = SmartDeviceRepository(),
            computationDispatcher = testDispatcher
        )
        advanceUntilIdle()

        viewModel.onRoomSelected("Salon")
        advanceUntilIdle()
        assertEquals(3, viewModel.uiState.value.filteredDevices.size)

        viewModel.onSearchQueryChanged("Termostat")
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.filteredDevices.size)
        assertEquals("Merkezi Termostat", viewModel.uiState.value.filteredDevices.first().name)
    }

    @Test
    fun toggleDeviceStatus_updatesEnergyCounterReactively() = runTest(testDispatcher) {
        val viewModel = SmartHomeViewModel(
            repository = SmartDeviceRepository(),
            computationDispatcher = testDispatcher
        )
        advanceUntilIdle()

        // Cihaz 6: Yatak Odası Kliması (1200.0 Watt) açıldığında 106.0 + 1200.0 = 1306.0 Watt olmalı
        viewModel.toggleDeviceStatus(6)
        advanceUntilIdle()

        assertEquals(1306.0, viewModel.uiState.value.totalActiveWattage, 0.01)
    }
}
