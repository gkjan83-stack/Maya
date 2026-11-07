package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AssistantSettings
import com.example.data.model.PersonalityMode
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.MayaCyanAccent
import com.example.ui.theme.MayaPinkPrimary
import com.example.ui.theme.MayaPinkSecondary
import com.example.ui.theme.TextMuted
import java.util.Locale

@Composable
fun SettingsScreen(
    settings: AssistantSettings,
    onSettingsChanged: (AssistantSettings) -> Unit,
    onTestVoice: () -> Unit,
    onClearChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCustomNameField by remember { mutableStateOf(false) }
    var customNameInput by remember { mutableStateOf("") }

    var showCustomCallField by remember { mutableStateOf(false) }
    var customCallInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "आवाज़ और व्यक्तित्व सेटिंग्स ⚙️",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "अपनी सहायक का नाम, बोलने की गति, सुर और स्वभाव बदलें",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }

        // Card 1: Assistant Name (Maya vs Ananya)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x33FF3377), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MayaPinkSecondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "सहायक का नाम (AI Name)",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val names = listOf("माया", "अनन्या")
                        names.forEach { nameChoice ->
                            FilterChip(
                                selected = settings.assistantName == nameChoice,
                                onClick = {
                                    showCustomNameField = false
                                    onSettingsChanged(settings.copy(assistantName = nameChoice))
                                },
                                label = { Text(nameChoice, fontWeight = FontWeight.SemiBold) },
                                shape = RoundedCornerShape(14.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MayaPinkPrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = DarkCardElevated,
                                    labelColor = TextMuted
                                )
                            )
                        }

                        FilterChip(
                            selected = showCustomNameField || (!listOf("माया", "अनन्या").contains(settings.assistantName)),
                            onClick = { showCustomNameField = true },
                            label = { Text("कस्टम नाम") },
                            shape = RoundedCornerShape(14.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MayaPinkPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = DarkCardElevated,
                                labelColor = TextMuted
                            )
                        )
                    }

                    if (showCustomNameField) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = customNameInput,
                                onValueChange = { customNameInput = it },
                                label = { Text("नया नाम दर्ज करें") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MayaPinkPrimary,
                                    unfocusedBorderColor = Color(0x33FF3377),
                                    focusedLabelColor = MayaPinkPrimary,
                                    unfocusedLabelColor = TextMuted,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (customNameInput.isNotBlank()) {
                                        onSettingsChanged(settings.copy(assistantName = customNameInput.trim()))
                                        showCustomNameField = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MayaPinkPrimary)
                            ) {
                                Text("सेव")
                            }
                        }
                    }
                }
            }
        }

        // Card 2: User's Call Name (संबोधन)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x3300E5FF), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = MayaCyanAccent
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "आपको किस नाम से पुकारे?",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val titles = listOf("जान", "हुज़ूर", "दोस्त", "मालिक")
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        titles.forEach { titleChoice ->
                            FilterChip(
                                selected = settings.userCallName == titleChoice,
                                onClick = {
                                    showCustomCallField = false
                                    onSettingsChanged(settings.copy(userCallName = titleChoice))
                                },
                                label = { Text(titleChoice, fontWeight = FontWeight.SemiBold) },
                                shape = RoundedCornerShape(14.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MayaCyanAccent,
                                    selectedLabelColor = Color.Black,
                                    containerColor = DarkCardElevated,
                                    labelColor = TextMuted
                                )
                            )
                        }
                    }
                }
            }
        }

        // Card 3: Voice Rate & Pitch Controls
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x33FFB300), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = Color(0xFFFFB300)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "आवाज़ ट्यूनिंग (Voice Controls)",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Speech Rate Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "बोलने की गति (Speed)",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = String.format(Locale.ENGLISH, "%.2fx", settings.speechRate),
                            color = Color(0xFFFFB300),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Slider(
                        value = settings.speechRate,
                        onValueChange = { onSettingsChanged(settings.copy(speechRate = it)) },
                        valueRange = 0.6f..1.5f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFFFB300),
                            activeTrackColor = Color(0xFFFFB300)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Speech Pitch Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "आवाज़ का सुर / पिच (Female Tone)",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = String.format(Locale.ENGLISH, "%.2fx", settings.speechPitch),
                            color = MayaPinkSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Slider(
                        value = settings.speechPitch,
                        onValueChange = { onSettingsChanged(settings.copy(speechPitch = it)) },
                        valueRange = 0.7f..1.6f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = MayaPinkPrimary,
                            activeTrackColor = MayaPinkPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Test Voice Button
                    Button(
                        onClick = onTestVoice,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MayaPinkPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "आवाज़ टेस्ट करें (Listen Sample)",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Card 4: Personality Mode
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x33CE93D8), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "व्यक्तित्व स्वभाव (Personality Mode)",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PersonalityMode.values().forEach { mode ->
                        val isSelected = settings.personalityMode == mode
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFF321A4F) else DarkCardElevated,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(
                                    1.dp,
                                    if (isSelected) MayaPinkPrimary else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onSettingsChanged(settings.copy(personalityMode = mode)) }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = mode.displayName,
                                    color = if (isSelected) MayaPinkSecondary else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = mode.description,
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Card 5: Auto-Speak toggle & Clear Chat
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "उत्तर बोलकर सुनाएं (Auto Speak)",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "हर उत्तर को तुरंत आवाज़ में सुनें",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                        Switch(
                            checked = settings.autoSpeak,
                            onCheckedChange = { onSettingsChanged(settings.copy(autoSpeak = it)) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MayaPinkPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onClearChat,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFFF5252)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "चैट इतिहास साफ करें")
                    }
                }
            }
        }

        // About Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF140A23)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MayaCyanAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Maya & Ananya AI Hindi Voice Assistant",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "हिंदी वॉइस असिस्टेंट • भावनात्मक रिस्पॉन्स • शायरी और समय",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
