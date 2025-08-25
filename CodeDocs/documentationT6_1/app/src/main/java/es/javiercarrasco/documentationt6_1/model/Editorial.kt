package es.javiercarrasco.documentationt6_1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Editorial.kt
@Entity(tableName = "editorial")
data class Editorial(
    @PrimaryKey(autoGenerate = true) val idEd: Int = 0,
    val name: String
)
