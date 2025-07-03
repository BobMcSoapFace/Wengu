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
}