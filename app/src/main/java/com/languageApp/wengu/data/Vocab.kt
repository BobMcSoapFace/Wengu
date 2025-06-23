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
    constructor(): this(
        vocab = "",
        pronunciation = "",
        translation = "",
        type = "",
        vocabLanguage = "",
        dateCreated = 0L,
    )
    fun getDate() : Date {
        return Date(dateCreated)
    }
    fun getTypes() : Set<String> {
        return this.type.split(TYPE_DELIMITER).map { it.trim().lowercase() } as Set<String>
    }
    companion object {
        val TYPE_DELIMITER = '/'
        /**
        * Enumerations used to edit SQL commands to specify specific properties to get/find entries by.
         * @property label Name of property inserted into SQL queries.
         */
    }
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