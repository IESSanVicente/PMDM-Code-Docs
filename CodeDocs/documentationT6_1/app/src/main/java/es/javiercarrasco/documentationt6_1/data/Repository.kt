package es.javiercarrasco.documentationt6_1.data

import androidx.lifecycle.LiveData
import es.javiercarrasco.documentationt6_1.model.Editorial
import es.javiercarrasco.documentationt6_1.model.SuperHero
import es.javiercarrasco.documentationt6_1.model.SuperWithEditorial
import kotlinx.coroutines.flow.Flow

// Reposity.kt
class Repository(private val localDatasource: LocalDatasource) {
    // Versión FLOW.
    val currentSupers: Flow<List<SuperWithEditorial>> = localDatasource.currentSupers
    val currentEditorials: Flow<List<Editorial>> = localDatasource.currentEditorials

    // Versión LIVEDATA.
    val currentSupersLD: LiveData<List<SuperWithEditorial>> = localDatasource.currentSupersLD
    val currentEditorialsLD: LiveData<List<Editorial>> = localDatasource.currentEditorialsLD

    suspend fun deleteSuper(superHero: SuperHero): Int {
        return localDatasource.deleteSuper(superHero)
    }

    suspend fun saveSuper(superHero: SuperHero) {
        localDatasource.saveSuper(superHero)
    }

    suspend fun getSuperById(superId: Int): SuperHero? = localDatasource.getSuperById(superId)

    suspend fun saveEditorial(editorial: Editorial) {
        localDatasource.saveEditorial(editorial)
    }

    suspend fun getEdById(editorialId: Int): Editorial? = localDatasource.getEdById(editorialId)
}