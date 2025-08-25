package es.javiercarrasco.documentationt6_1.model

import androidx.room.Embedded
import androidx.room.Relation

// SuperWithEditorial.kt
data class SuperWithEditorial(
    @Embedded val supers: SuperHero,
    @Relation(
        parentColumn = "idEditorial",
        entityColumn = "idEd"
    )
    val editorial: Editorial
)
