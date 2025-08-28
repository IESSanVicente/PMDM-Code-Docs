package es.javiercarrasco.documentationt7_1.data

import es.javiercarrasco.documentationt7_1.model.Post
import kotlinx.coroutines.flow.Flow

// LocalDatasource.kt
class LocalDatasource(private val dao: PostsDAO) {

    // Obtiene todos los posts desde la base de datos local.
    fun getPosts(): Flow<List<Post>> = dao.getPosts()

    // Inserta una lista de posts en la base de datos local.
    suspend fun insertAllPosts(posts: List<Post>) {
        dao.insertAllPosts(posts)
    }
}