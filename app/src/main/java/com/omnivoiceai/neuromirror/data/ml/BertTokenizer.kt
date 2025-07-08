package com.omnivoiceai.neuromirror.data.ml

import android.content.Context
import java.text.Normalizer

class BertTokenizer(context: Context) {
    private val vocab: Map<String, Int> = context.assets
        .open("emotion_model/vocab.txt")
        .bufferedReader()
        .readLines()
        .mapIndexed { idx, tok -> tok.trim() to idx }
        .toMap()

    fun tokenize(text: String): Triple<LongArray, LongArray, LongArray> {
        val tokens = listOf("[CLS]") +
                basicTokenize(text).flatMap { wordPieceTokenize(it) } +
                listOf("[SEP]")

        val inputIds = tokens.map { vocab[it] ?: vocab["[UNK]"]!! }.map { it.toLong() }
        val mask = LongArray(inputIds.size) { 1L }
        val types = LongArray(inputIds.size)

        return Triple(inputIds.toLongArray(), mask, types)
    }

    private fun basicTokenize(text: String): List<String> {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace("\\p{M}".toRegex(), "")
            .lowercase()
            .replace("[\\p{Cc}\\p{Cf}]".toRegex(), "")
            .replace("([\\p{Punct}])".toRegex(), " $1 ")
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }
    }


    private fun wordPieceTokenize(token: String): List<String> {
        if (token.length > 200) return listOf("[UNK]")
        val subTokens = mutableListOf<String>()
        var start = 0

        while (start < token.length) {
            var end = token.length
            var curSub: String? = null

            while (start < end) {
                var piece = token.substring(start, end)
                if (start > 0) piece = "##$piece"
                if (vocab.containsKey(piece)) {
                    curSub = piece
                    break
                }
                end--
            }

            if (curSub == null) {
                subTokens.add("[UNK]")
                break
            }

            subTokens.add(curSub)
            start = end
        }

        return subTokens
    }
}
