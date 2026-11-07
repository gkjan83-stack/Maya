package com.example.data.model

enum class PersonalityMode(val displayName: String, val description: String) {
    ROMANTIC_CARING("रोमांटिक & केयरिंग 💖", "प्यार भरे शब्द, खास अंदाज़ और बेपनाह मोहब्बत"),
    SWEET_FRIEND("प्यारी सहेली 🌸", "दोस्ताना बातचीत, साथ और मस्ती"),
    POETIC_SHAYARI("शायराना अंदाज़ ✨", "दिलकश शेर, नज़्में और गहरी बातें"),
    SMART_ASSISTANT("स्मार्ट पर्सनल AI ⚡", "सटीक जवाब, समय और मददगार टूल्स")
}

data class AssistantSettings(
    val assistantName: String = "माया",
    val userCallName: String = "जान",
    val speechRate: Float = 0.95f,
    val speechPitch: Float = 1.15f,
    val autoSpeak: Boolean = true,
    val personalityMode: PersonalityMode = PersonalityMode.ROMANTIC_CARING
)
