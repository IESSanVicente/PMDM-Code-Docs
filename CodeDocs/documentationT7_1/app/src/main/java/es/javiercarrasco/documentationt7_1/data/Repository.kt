package es.javiercarrasco.documentationt7_1.data

import android.util.Log
import es.javiercarrasco.documentationt7_1.model.Post
import kotlinx.coroutines.flow.first

// Repository.kt
class Repository(
    private val remoteDatasource: RemoteDatasource,
    private val localDatasource: LocalDatasource
) {
    // Manejo de errores básico con try-catch.
    // En caso de error, se devuelve una lista vacía.
    suspend fun getPosts(): List<Post>? {
        return try {
            val response = remoteDatasource.getPosts()
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                // Almacenar los posts obtenidos en la base de datos local.
                localDatasource.insertAllPosts(posts)
                posts
            } else {
                Log.e("Repository", "Error response: ${response.code()} - ${response.message()}")
                localDatasource.getPosts().first() // Se obtienen los posts almacenados localmente.
                // emptyList()
            }
        } catch (e: Exception) {
            Log.e("Repository", e.message, e)
            val dbdata = localDatasource.getPosts().first()
            if (dbdata.isNotEmpty())
                dbdata
            else throw e // Lanzar la excepción para que el ViewModel pueda manejarla.
        }
    }

    // Obtener un post por su ID con manejo de errores.
    // En caso de error, se devuelve null.
    suspend fun getPostById(id: Int): Post? {
        return try {
            val response = remoteDatasource.getPostById(id)
            if (response.isSuccessful) {
                val post = response.body()
                post
            } else {
                Log.e("Repository", "Error response: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching post by ID", e)
            throw e
        }
    }
}