package com.example

import com.example.data.engine.MayaAiEngine
import com.example.data.model.AssistantEmotion
import com.example.data.model.AssistantSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testMayaAiEngine_romanticResponse() {
        val settings = AssistantSettings(assistantName = "माया", userCallName = "जान")
        val response = MayaAiEngine.generateResponse("मुझे तुमसे बहुत प्यार है", settings)

        assertNotNull(response)
        assertEquals(AssistantEmotion.LOVE, response.emotion)
        assertTrue(response.replyText.contains("जान") || response.replyText.contains("प्यार") || response.replyText.contains("दिन") || response.replyText.contains("दिल"))
    }

    @Test
    fun testMayaAiEngine_timeResponse() {
        val settings = AssistantSettings(assistantName = "अनन्या", userCallName = "हुज़ूर")
        val response = MayaAiEngine.generateResponse("समय क्या हुआ है?", settings)

        assertNotNull(response)
        assertEquals(AssistantEmotion.POETIC, response.emotion)
        assertTrue(response.replyText.contains("हुज़ूर"))
        assertTrue(response.replyText.contains("बज") || response.replyText.contains("हुए हैं") || response.replyText.contains("कीमती"))
    }

    @Test
    fun testMayaAiEngine_shayariResponse() {
        val settings = AssistantSettings(assistantName = "माया", userCallName = "जान")
        val response = MayaAiEngine.generateResponse("कोई अच्छी शायरी सुनाओ", settings)

        assertNotNull(response)
        assertEquals(AssistantEmotion.POETIC, response.emotion)
        assertTrue(response.replyText.isNotBlank())
    }

    @Test
    fun testMayaAiEngine_jokeResponse() {
        val settings = AssistantSettings(assistantName = "अनन्या", userCallName = "दोस्त")
        val response = MayaAiEngine.generateResponse("एक चुटकुला सुनाओ", settings)

        assertNotNull(response)
        assertEquals(AssistantEmotion.WITTY, response.emotion)
        assertTrue(response.replyText.isNotBlank())
    }

    @Test
    fun testMayaAiEngine_exitResponse() {
        val settings = AssistantSettings(assistantName = "माया", userCallName = "जान")
        val response = MayaAiEngine.generateResponse("बाय, बंद हो जाओ", settings)

        assertNotNull(response)
        assertEquals(AssistantEmotion.CARING, response.emotion)
        assertTrue(response.replyText.contains("अलविदा") || response.replyText.contains("ख्याल") || response.replyText.contains("इंतज़ार"))
    }
}
