package es.javiercarrasco.documentationt7_1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Post.kt
@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)
