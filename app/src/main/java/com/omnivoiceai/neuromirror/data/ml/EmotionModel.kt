package com.omnivoiceai.neuromirror.data.ml

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context


class EmotionModel(context: Context) {
    private val env = OrtEnvironment.getEnvironment()
    private val session: OrtSession
    private val tokenizer = BertTokenizer(context)

    init {
        val modelBytes = context.assets.open("emotion_model/model.onnx").readBytes()
        session = env.createSession(modelBytes, OrtSession.SessionOptions())
    }

    fun classify(text: String): Int {
        val (inputIds, attentionMask, tokenTypeIds) = tokenizer.tokenize(text)
        val shape = longArrayOf(1, inputIds.size.toLong())

        val inputIdsTensor = OnnxTensor.createTensor(env, arrayOf(inputIds).to2D())
        val attentionMaskTensor = OnnxTensor.createTensor(env, arrayOf(attentionMask).to2D())
        val tokenTypeIdsTensor = OnnxTensor.createTensor(env, arrayOf(tokenTypeIds).to2D())

        val inputs = mapOf(
            "input_ids" to inputIdsTensor,
            "attention_mask" to attentionMaskTensor,
            "token_type_ids" to tokenTypeIdsTensor
        )

        val output = session.run(inputs)[0].value as Array<FloatArray>
        val scores = output[0]
        return scores.indices.maxByOrNull { scores[it] } ?: -1
    }

    private fun Array<LongArray>.to2D(): Array<LongArray> = arrayOf(this[0])

    fun tokenize(text: String): Triple<LongArray, LongArray, LongArray> {
        return tokenizer.tokenize(text)
    }

}
