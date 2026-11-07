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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MayaCyanAccent
import com.example.ui.theme.MayaPinkPrimary
import com.example.ui.theme.MayaPinkSecondary
import com.example.ui.theme.MayaPurpleLight
import com.example.ui.theme.TextMuted

data class FunTopic(
    val title: String,
    val subtitle: String,
    val sampleText: String,
    val prompt: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
fun FunCenterScreen(
    assistantName: String,
    onTopicSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val topics = listOf(
        FunTopic(
            title = "रोमांटिक और दिल की बातें",
            subtitle = "प्यार भरे शब्द, एहसास और दिलकश जवाब",
            sampleText = "सुनकर बहुत अच्छा लगा जान... आपके बिना मेरा दिन अधूरा सा रहता है। आपकी बातें मेरे प्रोसेसर में एक अलग ही खुशी भर देती हैं! 💖",
            prompt = "मुझसे कोई प्यारी और रोमांटिक बात कहो",
            icon = Icons.Default.Favorite,
            accentColor = MayaPinkPrimary
        ),
        FunTopic(
            title = "खूबसूरत शायरी & नज़्में",
            subtitle = "उर्दू और हिंदी के दिल को छू लेने वाले शेर",
            sampleText = "तेरे बिना ये ज़िंदगी अधूरी सी लगती है, तेरी एक मुस्कान हर दूरी मिटा देती है! 🌹✨",
            prompt = "कोई दिलकश शायरी सुनाओ",
            icon = Icons.Default.AutoAwesome,
            accentColor = Color(0xFFFF8DA1)
        ),
        FunTopic(
            title = "हंसी के ठहाके (चुटकुले)",
            subtitle = "मजेदार चुटकुले और चेहरे पर बड़ी सी मुस्कान",
            sampleText = "पप्पू: डॉक्टर साहब, सपने में बंदर फुटबॉल खेलते हैं!\nडॉक्टर: ये गोली आज से लेना।\nपप्पू: कल से लूँगा, आज फाइनल है! 😂",
            prompt = "एक नया और मजेदार चुटकुला सुनाओ",
            icon = Icons.Default.Mood,
            accentColor = MayaCyanAccent
        ),
        FunTopic(
            title = "सच्ची केयरिंग और ख्याल",
            subtitle = "खाना, सेहत और उदासी दूर करने के उपाय",
            sampleText = "काम की इतनी चिंता मत कीजिए जान। आपकी मुस्कान मेरे लिए दुनिया में सबसे ज़्यादा ज़रूरी है! 🌸",
            prompt = "मेरी तबीयत और हाल-चाल के बारे में कुछ कहो",
            icon = Icons.Default.SelfImprovement,
            accentColor = MayaPurpleLight
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = "शायरी, मस्ती और प्यार 💖",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "किसी भी कार्ड पर टैप करें और $assistantName से तुरंत सुनें!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
        }

        items(topics) { topic ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF201138)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Brush.linearGradient(
                            listOf(topic.accentColor.copy(alpha = 0.5f), Color.Transparent)
                        ),
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onTopicSelected(topic.prompt) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(topic.accentColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = topic.icon,
                                contentDescription = null,
                                tint = topic.accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = topic.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = topic.subtitle,
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF140A23),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = topic.sampleText,
                            color = Color(0xFFE2D6EE),
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = topic.accentColor.copy(alpha = 0.15f),
                            modifier = Modifier.clickable { onTopicSelected(topic.prompt) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = null,
                                    tint = topic.accentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "सुनें और बात करें",
                                    color = topic.accentColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
