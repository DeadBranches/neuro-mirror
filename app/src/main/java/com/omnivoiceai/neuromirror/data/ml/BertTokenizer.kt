package com.omnivoiceai.neuromirror.data.ml
import android.content.Context

import java.text.Normalizer
import java.util.regex.Pattern

class BertTokenizer(context: Context) {
    private val vocab: Map<String, Int>

    init {
        val lines = context.assets
            .open("emotion_model/vocab.txt")
            .bufferedReader()
            .readLines()
        vocab = lines.mapIndexed { idx, tok -> tok.trim() to idx }.toMap()
    }

    /**
     * Tokenizza in tre array:
     *  - inputIds: [CLS], sub-token BERT, [SEP]
     *  - attentionMask: 1 per ogni token
     *  - tokenTypeIds: tutti 0 (single sequence)
     */
    fun tokenize(text: String): Triple<LongArray, LongArray, LongArray> {
        // 1) Basic tokenize: lowercase, strip accents, split punteggiatura/spazi
        val basicTokens = basicTokenize(text)
        // 2) WordPiece
        val wpTokens = basicTokens.flatMap { wordPieceTokenize(it) }

        // 3) Aggiungo i marcatori
        val tokens = mutableListOf<String>()
        tokens.add("[CLS]")
        tokens += wpTokens
        tokens.add("[SEP]")

        // 4) Converto in IDs
        val inputIds = tokens.map { vocab[it] ?: vocab["[UNK]"]!! }.map { it.toLong() }
        val mask      = List(inputIds.size) { 1L }
        val types     = List(inputIds.size) { 0L }

        return Triple(
            inputIds.toLongArray(),
            mask.toLongArray(),
            types.toLongArray()
        )
    }

    // ------------------------------------------------------------
    // Basic tokenizer: lowercase + strip accents + split punctuazione
    // ------------------------------------------------------------
    private fun basicTokenize(text: String): List<String> {
        // normalize e rimuovo accenti
        val normalized = Normalizer
            .normalize(text, Normalizer.Form.NFD)
            .replace("\\p{M}".toRegex(), "") // diacritics

        val sb = StringBuilder()
        for (ch in normalized) {
            when {
                ch.isWhitespace() -> sb.append(' ')
                isControl(ch)     -> { /* skip */ }
                isPunctuation(ch) -> { sb.append(' '); sb.append(ch); sb.append(' ') }
                else              -> sb.append(ch.lowercaseChar())
            }
        }

        // split on whitespace
        return sb.toString().split("\\s+".toRegex()).filter { it.isNotEmpty() }
    }

    private fun isControl(ch: Char): Boolean {
        // skip cc e cf
        val t = Character.getType(ch)
        return t == Character.CONTROL.toInt() || t == Character.FORMAT.toInt()
    }
    private fun isPunctuation(ch: Char): Boolean {
        // Punctuation Unicode categories
        val p = Pattern.compile("\\p{P}")
        return p.matcher(ch.toString()).matches()
    }

    // ------------------------------------------------------------
    // WordPiece tokenizer (greedy longest‐match)
    // ------------------------------------------------------------
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
                end -= 1
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
