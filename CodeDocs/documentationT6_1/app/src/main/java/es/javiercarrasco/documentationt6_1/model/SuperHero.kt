package es.javiercarrasco.documentationt6_1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// SuperHero.kt
@Entity(tableName = "superhero")
data class SuperHero(
    @PrimaryKey(autoGenerate = true)
    var idSuper: Int = 0,
    var superName: String,
    var realName: String,
    var favorite: Boolean = false,
    var idEditorial: Int = 0
)
