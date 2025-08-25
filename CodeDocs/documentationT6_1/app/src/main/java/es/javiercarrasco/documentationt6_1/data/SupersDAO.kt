package es.javiercarrasco.documentationt6_1.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import es.javiercarrasco.documentationt6_1.model.Editorial
import es.javiercarrasco.documentationt6_1.model.SuperHero
import es.javiercarrasco.documentationt6_1.model.SuperWithEditorial
import kotlinx.coroutines.flow.Flow

// SupersDAO.kt
@Dao
interface SupersDAO {
    // Versión de consultas que devuelven un FLOW.
    @Transaction // Permite obtener datos de varias tablas relacionadas con una sola consulta.
    @Query("SELECT * FROM SuperHero ORDER BY superName")
    fun getSuperHerosWithEditorials(): Flow<List<SuperWithEditorial>>

    @Query("SELECT * FROM Editorial")
    fun getAllEditorials(): Flow<List<Editorial>>

    // Versión de consultas que devuelven un LIVEDATA (...LD).
    @Transaction
    @Query("SELECT * FROM SuperHero ORDER BY superName")
    fun getSuperHerosWithEditorialsLD(): LiveData<List<SuperWithEditorial>>

    @Query("SELECT * FROM Editorial")
    fun getAllEditorialsLD(): LiveData<List<Editorial>>

    // Resto de consultas.

    @Query("SELECT * FROM SuperHero WHERE idSuper = :idSuper")
    suspend fun getSuperById(idSuper: Int): SuperHero?

    @Query("SELECT * FROM Editorial WHERE idEd = :editorialId")
    suspend fun getEditorialById(editorialId: Int): Editorial?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEditorial(editorial: Editorial)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuperHero(superHero: SuperHero)

    @Delete
    suspend fun deleteSuperHero(superHero: SuperHero): Int
}
