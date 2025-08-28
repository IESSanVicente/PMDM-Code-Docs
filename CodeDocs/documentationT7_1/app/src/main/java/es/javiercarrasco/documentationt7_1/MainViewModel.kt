package es.javiercarrasco.documentationt7_1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import es.javiercarrasco.documentationt7_1.data.AppDatabase
import es.javiercarrasco.documentationt7_1.data.LocalDatasource
import es.javiercarrasco.documentationt7_1.data.RemoteDatasource
import es.javiercarrasco.documentationt7_1.data.Repository
import es.javiercarrasco.documentationt7_1.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // Se inicializa el repositorio y el datasource.
    private val repository: Repository
    private val remoteDatasource: RemoteDatasource
    private val localDatasource: LocalDatasource

    // Estado para la lista de posts, estado de carga y errores.
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    // Estado de carga y errores.
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Estado de error.
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        // Se inicializa la base de datos local y el DAO.
        val database = AppDatabase.getInstance(application)
        val dao = database.postsDAO()

        remoteDatasource = RemoteDatasource()
        localDatasource = LocalDatasource(dao)
        repository = Repository(remoteDatasource, localDatasource)
    }

    fun fetchPosts() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val posts = repository.getPosts()
                _posts.value = posts ?: emptyList()
            } catch (e: Exception) {
                _error.value = "ERROR: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}