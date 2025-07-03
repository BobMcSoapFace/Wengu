package com.languageApp.wengu.modules

import com.languageApp.wengu.data.Test
import com.languageApp.wengu.data.TestResult
import com.languageApp.wengu.data.Vocab
import java.time.Instant
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
                        type = arrayOfNulls<String>(abs(RANDOM.nextInt() % 3 + 1)).joinToString(
                            Vocab.TYPE_DELIMITER.toString()){ GENERATED_TYPES.random()},
                        vocabLanguage = "Language${(i/10)+1}",
                        dateCreated = Instant.now().epochSecond
                    )
                )

            }
            return list
        }
        fun generateVocabResults(vocabId : Int, numResults: Int = 10, testId : Int = -1) : List<TestResult>{
            val list : MutableList<TestResult> = mutableListOf()
            for(i in 1..numResults){
                list.add(
                    TestResult(
                        id = -i,
                        dateTaken = Instant.now().epochSecond,
                        correct = RANDOM.nextInt()%2==0,
                        secondsTaken = RANDOM.nextInt()%40 + 1,
                        testId = testId,
                        vocabId = vocabId
                    )
                )
            }
            return list
        }
        fun generateTests(count : Int = 10) : List<Test>{
            val list : MutableList<Test> = mutableListOf()
            for(i in 1..count){
                list.add(
                    Test(
                        id = -i,
                        dateTaken = Instant.now().epochSecond,
                        language = listOf("Mandarin", "English", "French").random()
                    )
                )
            }
            return list
        }
    }
}