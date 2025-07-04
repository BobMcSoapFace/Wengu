package com.languageApp.wengu.data

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.Date

@Stable
@Serializable
@Entity(tableName = "Vocab")
data class Vocab(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val vocab : String,
    val pronunciation : String,
    val translation : String,
    val type : String,
    val vocabLanguage : String,
    val dateCreated : Long, //formatted in total seconds since 1970/1/1
) : DataEntry {
    companion object {
        const val TYPE_DELIMITER = "/"
        const val LANGUAGES_DELIMITER = "/"
        fun getVocabPercent(vocab: Vocab, results: List<TestResult>) : Float{
            return results.filter{ it.vocabId == vocab.id }.filter { it.correct }.size.toFloat() /
                    getVocabResultCount(vocab, results)
        }
        fun getVocabResultCount(vocab: Vocab, results: List<TestResult>) : Int {
            return results.filter{ it.vocabId == vocab.id }.size
        }
        fun getTotalTimeTaken(vocab : Vocab, results: List<TestResult>) : Int {
            return results.filter { it.vocabId == vocab.id }.sumOf { it.secondsTaken }
        }
        fun getBestVocab(vocabs: List<Vocab>, results : List<TestResult>) : Vocab? {
            return vocabs
                .filter { !getVocabPercent(it, results).isNaN() }
                .sortedBy { getTotalTimeTaken(it, results) }
                .sortedByDescending { getVocabResultCount(it, results) }
                .maxByOrNull { getVocabPercent(it, results) }

        }
    }
    /**
     * Enumerations used to edit SQL commands to specify specific properties to get/find entries by.
     * @property label Name of property inserted into SQL queries.
     */
    enum class Properties(val label : String){
        ID("id"),
        VOCAB("vocab"),
        PRONUNCIATION("pronunciation"),
        TRANSLATION("translation"),
        TYPE("type"),
        VOCAB_LANGUAGE("vocabLanguage"),
        DATE("dateCreated")
    }

}