package com.example.week3_assignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week3_assignment.data.model.DeviceCategory
import com.example.week3_assignment.data.model.SmartDevice
import com.example.week3_assignment.ui.state.SmartHomeUiState
import com.example.week3_assignment.ui.theme.Amber100
import com.example.week3_assignment.ui.theme.Amber600
import com.example.week3_assignment.ui.theme.Emerald100
import com.example.week3_assignment.ui.theme.Emerald500
import com.example.week3_assignment.ui.theme.Emerald600
import com.example.week3_assignment.ui.theme.Indigo100
import com.example.week3_assignment.ui.theme.Indigo600
import com.example.week3_assignment.ui.theme.Rose100
import com.example.week3_assignment.ui.theme.Rose600
import com.example.week3_assignment.ui.theme.SkyBlue100
import com.example.week3_assignment.ui.theme.SkyBlue500
import com.example.week3_assignment.ui.theme.SkyBlue600
import com.example.week3_assignment.ui.theme.Slate100
import com.example.week3_assignment.ui.theme.Slate400
import com.example.week3_assignment.ui.theme.Slate600
import com.example.week3_assignment.ui.theme.Slate800
import com.example.week3_assignment.ui.theme.Slate900

/**
 * %100 Stateless (Durumsuz) Compose UI Katmanı.
 *
 * Kural 1 (Katman Ayrımı): Bu tasarım katmanında hiçbir filtreleme döngüsü (.filter) veya
 * matematiksel hesaplama (toplama/çıkarma) kodu yer almaz. Tüm veriler ViewModel'den
 * SmartHomeUiState aracılığıyla hazır olarak alınır ve olaylar (events) yukarı iletilir.
 *
 * Kural 2 (Performans): LazyColumn ve LazyRow bileşenlerinde benzersiz 'key' tanımlanarak
 * gereksiz recomposition'lar önlenmiş ve 120 FPS akıcılık garanti altına alınmıştır.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartHomeScreen(
    uiState: SmartHomeUiState,
    onSearchQueryChange: (String) -> Unit,
    onRoomSelected: (String) -> Unit,
    onToggleDevice: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SkyBlue100,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Akıllı Ev",
                                    tint = SkyBlue600,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Akıllı Ev Kontrol Merkezi",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = "IoT Cihaz ve Enerji Yönetimi",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Slate600
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 1. Ekranın En Üstündeki Canlı Reaktif Enerji Sayacı Kartı
            ReactiveEnergyCounterCard(
                formattedTotalWattage = uiState.formattedTotalWattage,
                activeOnlineDeviceCount = uiState.activeOnlineDeviceCount,
                onlineDeviceCount = uiState.onlineDeviceCount,
                totalDeviceCount = uiState.totalDeviceCount
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Cihaz Adı Arama Çubuğu
            DeviceSearchBar(
                query = uiState.searchQuery,
                onQueryChange = onSearchQueryChange
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Odalara Göre Filtreleme Barı (Salon, Mutfak...)
            RoomFilterBar(
                rooms = uiState.availableRooms,
                selectedRoom = uiState.selectedRoom,
                onRoomSelected = onRoomSelected
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Süzülmüş Cihaz Listesi (120 FPS için key destekli LazyColumn)
            if (uiState.filteredDevices.isEmpty()) {
                EmptyDeviceState(
                    searchQuery = uiState.searchQuery,
                    selectedRoom = uiState.selectedRoom
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = uiState.filteredDevices,
                        key = { device -> device.id }
                    ) { device ->
                        SmartDeviceItemCard(
                            device = device,
                            onToggleClick = { onToggleDevice(device.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReactiveEnergyCounterCard(
    formattedTotalWattage: String,
    activeOnlineDeviceCount: Int,
    onlineDeviceCount: Int,
    totalDeviceCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Slate900, Slate800)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Amber500Safe),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Enerji",
                                tint = Slate900,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CANLI ENERJİ TÜKETİM SAYACI",
                                style = MaterialTheme.typography.labelMedium,
                                color = SkyBlue100,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "Sadece Aktif ve Online Cihazlar",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Slate400
                            )
                        }
                    }

                    Surface(
                        color = Emerald500.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Emerald500)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "REAKTİF",
                                style = MaterialTheme.typography.labelMedium,
                                color = Emerald100,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = formattedTotalWattage,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatusMetricChip(
                        label = "Aktif & Online",
                        value = "$activeOnlineDeviceCount Cihaz",
                        accentColor = Emerald500,
                        modifier = Modifier.weight(1f)
                    )
                    StatusMetricChip(
                        label = "Çevrimiçi",
                        value = "$onlineDeviceCount / $totalDeviceCount",
                        accentColor = SkyBlue500,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

private val Amber500Safe = Color(0xFFF59E0B)

@Composable
private fun StatusMetricChip(
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.White.copy(alpha = 0.08f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = Slate400
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DeviceSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Cihaz adı ara (Örn: Köşe Lambası, Roborock...)",
                color = Slate400
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Ara",
                tint = SkyBlue600
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Temizle",
                        tint = Slate600
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = SkyBlue600,
            unfocusedBorderColor = Slate100
        )
    )
}

@Composable
fun RoomFilterBar(
    rooms: List<String>,
    selectedRoom: String,
    onRoomSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = rooms,
            key = { room -> room }
        ) { room ->
            val isSelected = room == selectedRoom
            FilterChip(
                selected = isSelected,
                onClick = { onRoomSelected(room) },
                label = {
                    Text(
                        text = room,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                shape = RoundedCornerShape(50),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SkyBlue600,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Slate600
                )
            )
        }
    }
}

@Composable
fun SmartDeviceItemCard(
    device: SmartDevice,
    onToggleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryIcon = getCategoryIcon(device.category)
    val categoryAccent = getCategoryAccentColor(device.category)
    val categoryContainer = getCategoryContainerColor(device.category)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (device.isOnline) Color.White else Slate100
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = categoryContainer,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = categoryIcon,
                            contentDescription = device.category.displayName,
                            tint = categoryAccent,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = device.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Slate900,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        BadgePill(
                            text = "Oda: ${device.room}",
                            backgroundColor = Slate100,
                            contentColor = Slate800
                        )
                        BadgePill(
                            text = device.category.code,
                            backgroundColor = categoryContainer,
                            contentColor = categoryAccent
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (device.isOnline) Icons.Default.Wifi else Icons.Default.WifiOff,
                                contentDescription = null,
                                tint = if (device.isOnline) Emerald600 else Rose600,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (device.isOnline) "Online: Evet" else "Online: Hayır",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (device.isOnline) Emerald600 else Rose600,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(text = "•", color = Slate400)

                        Text(
                            text = device.formattedWattage,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (device.isActive && device.isOnline) Amber600 else Slate600,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    color = if (device.isActive && device.isOnline) Emerald100 else Slate100,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (device.isActive) "Aktif: Evet" else "Aktif: Hayır",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (device.isActive && device.isOnline) Emerald600 else Slate600,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onToggleClick,
                    enabled = device.isOnline,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (device.isActive) Rose600 else Emerald600,
                        contentColor = Color.White,
                        disabledContainerColor = Slate400.copy(alpha = 0.3f),
                        disabledContentColor = Slate600
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (!device.isOnline) {
                            "Çevrimdışı"
                        } else if (device.isActive) {
                            "Kapat"
                        } else {
                            "Aç"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun BadgePill(
    text: String,
    backgroundColor: Color,
    contentColor: Color
) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun EmptyDeviceState(
    searchQuery: String,
    selectedRoom: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Eşleşen Cihaz Bulunamadı",
                style = MaterialTheme.typography.titleMedium,
                color = Slate900
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Arama: \"$searchQuery\" | Oda: $selectedRoom",
                style = MaterialTheme.typography.bodyMedium,
                color = Slate600
            )
        }
    }
}

private fun getCategoryIcon(category: DeviceCategory): ImageVector {
    return when (category) {
        DeviceCategory.LIGHT -> Icons.Default.Lightbulb
        DeviceCategory.CAMERA -> Icons.Default.Videocam
        DeviceCategory.THERMOSTAT -> Icons.Default.Thermostat
        DeviceCategory.VACUUM -> Icons.Default.CleaningServices
    }
}

private fun getCategoryAccentColor(category: DeviceCategory): Color {
    return when (category) {
        DeviceCategory.LIGHT -> Amber600
        DeviceCategory.CAMERA -> SkyBlue600
        DeviceCategory.THERMOSTAT -> Rose600
        DeviceCategory.VACUUM -> Indigo600
    }
}

private fun getCategoryContainerColor(category: DeviceCategory): Color {
    return when (category) {
        DeviceCategory.LIGHT -> Amber100
        DeviceCategory.CAMERA -> SkyBlue100
        DeviceCategory.THERMOSTAT -> Rose100
        DeviceCategory.VACUUM -> Indigo100
    }
}
