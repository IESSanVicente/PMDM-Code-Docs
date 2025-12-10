

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SupersRepositoryIntegrationTest {
    private lateinit var db: SupersDatabase
    private lateinit var repository: Repository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            SupersDatabase::class.java
        ).allowMainThreadQueries() // solo en tests
            .build()

        val dao = db.supersDao()
        val localDataSource = LocalDatasource(dao)
        repository = Repository(localDataSource)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun saveEditorial_y_currentEditorials() = runTest {
        repository.saveEditorial(Editorial(name = "Test Integración"))

        val result = repository.currentEditorials.first()

        assertEquals(1, result.size)
        assertEquals("Test Integración", result.first().name)
    }

    @Test
    fun saveSuperhero_y_currentSupers() = runTest {
        repository.saveEditorial(Editorial(name = "Test Integración"))
        repository.saveSuperHero(
            Superhero(superName = "Batman Test", realName = "Bruce Wayne", idEditorial = 1)
        )
        val result = repository.currentSupers.first()

        assertEquals(1, result.size)
        assertEquals("Batman Test", result.first().supers.superName)
    }
}