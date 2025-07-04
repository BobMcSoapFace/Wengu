package com.languageApp.wengu.data

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlin.math.abs

@Stable
@Entity(tableName = "Test")
@Serializable
data class Test(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val dateTaken : Long,
    val language : String,
) : DataEntry {
    enum class Properties(val label : String){
        ID("id"),
        LANGUAGE("language"),
        DATE("dateTaken")
    }
    companion object {
        fun getNextIndex(tests : List<Test>) : Int {
            return if(tests.isNotEmpty()) tests.map { it.id }.maxOf{ it } + 1 else 1
        }
        fun getTestPercent(test : Test, results: List<TestResult>) : Float{
            return results.filter{ it.testId == test.id }.filter { it.correct }.size.toFloat() /
                    getTestResultCount(test, results)
        }
        fun getTestResultCount(test : Test, results: List<TestResult>) : Int {
            return results.filter{ it.testId == test.id }.size
        }
        fun getTotalTimeTaken(test : Test, results: List<TestResult>) : Int {
            return results.filter { it.testId == test.id }.sumOf { it.secondsTaken }
        }
        fun getBestTest(tests: List<Test>, results : List<TestResult>) : Test? {
            return tests
                .filter { !getTestPercent(it, results).isNaN() }
                .sortedBy { getTotalTimeTaken(it, results) }
                .sortedByDescending { getTestResultCount(it, results) }
                .maxByOrNull { getTestPercent(it, results) }
        }
    }
}