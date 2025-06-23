package com.languageApp.wengu.data

import android.text.method.DateTimeKeyListener
import java.time.Instant
import java.util.Date
import java.util.Random
import kotlin.math.abs

/**
 *
 */
abstract class DebugHelper {
    companion object {
        private val GENERATED_TYPES = listOf("noun", "adjective", "verb", "adverb")
        private val RANDOM = Random()
        private const val DEFAULT_GENERATED_VOCAB_NUM = 20
        fun generateVocab(numVocab : Int = DEFAULT_GENERATED_VOCAB_NUM) : List<Vocab> {
            val list : MutableList<Vocab> = mutableListOf()
            for(i in 1..numVocab){ // kotlin for loops are ugly
                list.add(
                    Vocab(
                        id = -i,
                        vocab = "Vocab${i}",
                        pronunciation = "Pronunciation${i}",
                        translation = "Definition${i}",
                        type = arrayOfNulls<String>(abs(RANDOM.nextInt() % 3 + 1)).joinToString(Vocab.TYPE_DELIMITER.toString()){GENERATED_TYPES.random()},
                        vocabLanguage = "Language${(i/10)+1}",
                        dateCreated = Instant.now().epochSecond
                    )
                )

            }
            return list
        }
    }
}