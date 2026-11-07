package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MayaPinkPrimary
import com.example.ui.theme.MayaPinkSecondary

@Composable
fun QuickPromptsRow(
    assistantName: String,
    onPromptClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val prompts = listOf(
        "कैसी हो $assistantName? 💖",
        "कोई रोमांटिक शायरी सुनाओ 🌹",
        "एक मजेदार चुटकुला सुनाओ 😂",
        "समय क्या हुआ है? ⏰",
        "मुझे तुम्हारी याद आ रही है 💕",
        "आज का सुविचार बताओ ✨",
        "खाना खाया तुमने? 🍲",
        "मौसम कैसा है? ☕",
        "आज क्या तारीख है? 📅",
        "तुम कौन हो? 🤖"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        prompts.forEach { prompt ->
            FilterChip(
                selected = false,
                onClick = { onPromptClick(prompt) },
                label = {
                    Text(
                        text = prompt,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFF26143E),
                    labelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = false,
                    borderColor = MayaPinkPrimary.copy(alpha = 0.35f),
                    borderWidth = 1.dp
                )
            )
        }
    }
}
