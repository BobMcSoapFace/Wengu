package com.languageApp.wengu.data

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.security.KeyStore.TrustedCertificateEntry

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
}