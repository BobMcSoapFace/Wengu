package com.languageApp.wengu.data

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.Date

@Stable
@Entity(tableName = "TestResult")
@Serializable
data class TestResult(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val dateTaken : Long,
    val correct : Boolean,
    val secondsTaken : Int,
    val vocabId : Int,
    val testId : Int
) : DataEntry {
    override fun toString(): String {
        return "ID:${id},DATE:${getDate()},CORRECT:${correct}"
    }
    fun getDate() : Date {
        return Date(dateTaken)
    }
    enum class Properties(val label : String){
        ID("id"),
        DATE("dateTaken"),
        SECONDS("secondsTaken"),
        TEST("testId"),
        CORRECT("correct")
    }
    companion object {
        fun getVocabPercent(vocab : Vocab, testResults : List<TestResult>) : Float {
            return testResults.filter{ it.vocabId == vocab.id }.filter { it.correct }.size.toFloat() /
                    testResults.filter{ it.vocabId == vocab.id }.size.toFloat()
        }
        fun getWorstVocab(vocabs : List<Vocab>, testResults: List<TestResult>) : Vocab {
            var worstVocab = vocabs.first()
            var worstPercent = getVocabPercent(worstVocab, testResults)
            vocabs.forEach{
                val vocabPercent = getVocabPercent(it, testResults)
                if(!vocabPercent.isNaN() && worstPercent > vocabPercent){
                    worstVocab = it
                    worstPercent = vocabPercent
                }
            }
            return worstVocab
        }
        fun getGradeRating(grade : Float) : String {
            return when{
                grade.isNaN() -> "N/A"
                grade < 0.1f -> "Abhorrent"
                grade < 0.2f -> "Abysmal"
                grade < 0.3f -> "Poor"
                grade < 0.4f -> "Insufficient"
                grade < 0.5f -> "Mediocre"
                grade < 0.6f -> "Passable"
                grade < 0.7f -> "Sufficient"
                grade < 0.8f -> "Proficient"
                grade < 0.9f -> "Advanced"
                grade < 0.99f -> "Incredible"
                else -> "Perfect"
            }
        }
    }
}