package es.javiercarrasco.documentationt6_1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import es.javiercarrasco.documentationt6_1.data.AppDatabase
import es.javiercarrasco.documentationt6_1.data.LocalDatasource
import es.javiercarrasco.documentationt6_1.data.Repository
import es.javiercarrasco.documentationt6_1.model.Editorial
import es.javiercarrasco.documentationt6_1.model.SuperHero
import es.javiercarrasco.documentationt6_1.model.SuperWithEditorial
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// SupersViewModel.kt
class SupersViewModel(application: Application) : AndroidViewModel(application) {
    // Se inicializa el repositorio y el datasource.
    private val repository: Repository
    private val localDatasource: LocalDatasource

    // Se exponen los StateFlow para que la UI observe los cambios.
    private val _currentSupers = MutableStateFlow<List<SuperWithEditorial>>(emptyList())
    val currentSupers: StateFlow<List<SuperWithEditorial>> = _currentSupers

    private val _currentEditorials = MutableStateFlow<List<Editorial>>(emptyList())
    val currentEditorials: StateFlow<List<Editorial>> = _currentEditorials

    // Se exponen los LiveData según sea necesario.
    val currentSupersLD: LiveData<List<SuperWithEditorial>>
    val currentEditorialLD: LiveData<List<Editorial>>

    init {
        // Inicialización del repositorio y el datasource.
        val database = AppDatabase.getInstance(application)
        val dao = database.supersDAO()
        localDatasource = LocalDatasource(dao)
        repository = Repository(localDatasource)

        // Carga inicial de superhéroes y editoriales, versión Flow.
        loadSupers()
        loadEditorials()

        // Inicialización del LiveData para los superhéroes.
        currentSupersLD = repository.currentSupersLD
        currentEditorialLD = repository.currentEditorialsLD
    }

    // Implementa funciones para interactuar con el repositorio.

    // Se observan los StateFlow para que la UI pueda reaccionar a los cambios con Flow una vez
    // que se hayan cargado los datos iniciales.
    fun loadEditorials() {
        viewModelScope.launch {
            repository.currentEditorials
                .catch { e -> e.printStackTrace() } // Manejo de errores.
                .collect { editorials ->
                    _currentEditorials.value = editorials // Actualiza el StateFlow con las editoriales.
                }
        }
    }

    fun loadSupers() {
        viewModelScope.launch {
            repository.currentSupers
                .catch { e -> e.printStackTrace() } // Manejo de errores.
                .collect { supers ->
                    _currentSupers.value = supers // Actualiza el StateFlow con los superhéroes.
                }
        }
    }

    fun saveEditorial(editorial: Editorial) {
        viewModelScope.launch {
            repository.saveEditorial(editorial)
        }
    }

    fun saveSuper(superHero: SuperHero) {
        viewModelScope.launch {
            repository.saveSuper(superHero)
        }
    }

    suspend fun delSuper(superHero: SuperHero): Int {
        return deleteSuper(superHero).await()
    }

    // Esta función devuelve un Deferred para que se pueda esperar su resultado de forma asíncrona.
    private fun deleteSuper(superHero: SuperHero): Deferred<Int> {
        return viewModelScope.async {
            repository.deleteSuper(superHero)
        }
    }

    fun getSuperById(superId: Int): Deferred<SuperHero?> {
        return viewModelScope.async { repository.getSuperById(superId) }
    }

    fun getEdById(editorialId: Int): Deferred<Editorial?> {
        return viewModelScope.async { repository.getEdById(editorialId) }
    }

    // Esta función es para cargar datos de ejemplo.
//    fun demoData() {
//        viewModelScope.launch {
//            repository.saveEditorial(Editorial(1, "Marvel"))
//            repository.saveEditorial(Editorial(2, "DC Comics"))
//            repository.saveEditorial(Editorial(3, "Dark Horse Comics"))
//
//            repository.saveSuper(SuperHero(1, "SpiderMan", "Peter Parker", false, 1))
//            repository.saveSuper(SuperHero(2, "Batman", "Bruce Wayne", true, 2))
//            repository.saveSuper(SuperHero(3, "Superman", "Clark Kent", false, 2))
//            repository.saveSuper(SuperHero(4, "Hellboy", "Angel de la Muerte", false, 3))
//            repository.saveSuper(SuperHero(5, "Wonder Woman", "Diana Prince", false, 2))
//            repository.saveSuper(SuperHero(6, "Iron Man", "Tony Stark", false, 1))
//            repository.saveSuper(SuperHero(7, "SpiderMan 2", "Peter Parker", false, 1))
//            repository.saveSuper(SuperHero(8, "Batman 2", "Bruce Wayne", true, 2))
//            repository.saveSuper(SuperHero(9, "Superman 2", "Clark Kent", false, 2))
//            repository.saveSuper(SuperHero(10, "Hellboy 2", "Angel de la Muerte", true, 3))
//            repository.saveSuper(SuperHero(11, "Wonder Woman 2", "Diana Prince", false, 2))
//            repository.saveSuper(SuperHero(12, "Iron Man 2", "Tony Stark", false, 1))
//            repository.saveSuper(SuperHero(13, "SpiderMan 3", "Peter Parker", false, 1))
//            repository.saveSuper(SuperHero(14, "Batman 3", "Bruce Wayne", true, 2))
//            repository.saveSuper(SuperHero(15, "Superman 3", "Clark Kent", false, 2))
//            repository.saveSuper(SuperHero(16, "Hellboy 3", "Angel de la Muerte", false, 3))
//            repository.saveSuper(SuperHero(17, "Wonder Woman 3", "Diana Prince", false, 2))
//            repository.saveSuper(SuperHero(18, "Iron Man 3", "Tony Stark", false, 1))
//        }
//    }
}