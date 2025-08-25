package es.javiercarrasco.documentationt6_1.data

import androidx.lifecycle.LiveData
import es.javiercarrasco.documentationt6_1.model.Editorial
import es.javiercarrasco.documentationt6_1.model.SuperHero
import es.javiercarrasco.documentationt6_1.model.SuperWithEditorial
import kotlinx.coroutines.flow.Flow

// LocalDatasource.kt
class LocalDatasource(private val dao: SupersDAO) {
    // Version FLOW.
    val currentSupers: Flow<List<SuperWithEditorial>> = dao.getSuperHerosWithEditorials()
    val currentEditorials: Flow<List<Editorial>> = dao.getAllEditorials()

    // Version LIVEDATA.
    val currentSupersLD: LiveData<List<SuperWithEditorial>> = dao.getSuperHerosWithEditorialsLD()
    val currentEditorialsLD: LiveData<List<Editorial>> = dao.getAllEditorialsLD()

    suspend fun deleteSuper(superHero: SuperHero): Int { // Returns the number of rows deleted.
        return dao.deleteSuperHero(superHero)
    }

    suspend fun saveSuper(superHero: SuperHero) {
        dao.insertSuperHero(superHero)
    }

    suspend fun getSuperById(superId: Int): SuperHero? = dao.getSuperById(superId)

    suspend fun saveEditorial(editorial: Editorial) {
        dao.insertEditorial(editorial)
    }

    suspend fun getEdById(editorialId: Int): Editorial? = dao.getEditorialById(editorialId)
}