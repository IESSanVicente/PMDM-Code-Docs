package es.javiercarrasco.documentationt7_1.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.javiercarrasco.documentationt7_1.model.Post
import kotlinx.coroutines.flow.Flow

// PostsDAO.kt
@Dao
interface PostsDAO {
    // Obtiene todos los posts como un Flow para observar cambios en tiempo real.
    @Query("SELECT * FROM posts")
    fun getPosts(): Flow<List<Post>>

    // Inserta una lista de posts. Si ya existen, los reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPosts(posts: List<Post>)
}
