package com.omnivoiceai.neuromirror.data.repositories

import com.omnivoiceai.neuromirror.data.database.note.EmotionDetected
import com.omnivoiceai.neuromirror.data.ml.EmotionModel

class EmotionRepository(private val model: EmotionModel) {
    private val idToLabel = mapOf(
        0 to EmotionDetected.Sadness,
        1 to EmotionDetected.Anger,
        2 to EmotionDetected.Love,
        3 to EmotionDetected.Surprise,
        4 to EmotionDetected.Fear,
        5 to EmotionDetected.Happiness,
        6 to EmotionDetected.Neutral,
        7 to EmotionDetected.Disgust,
        8 to EmotionDetected.Shame,
        9 to EmotionDetected.Guilt,
        10 to EmotionDetected.Confusion,
        11 to EmotionDetected.Desire,
        12 to EmotionDetected.Sarcasm
    )

    fun classify(text: String): EmotionDetected {
        val predictionIndex = model.classify(text)
        return idToLabel[predictionIndex] ?: EmotionDetected.Neutral
    }
}
