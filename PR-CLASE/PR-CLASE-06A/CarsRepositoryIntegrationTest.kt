package es.javiercarrasco.pr_clase_06

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import es.javiercarrasco.pr_clase_06.data.LocalDatasource
import es.javiercarrasco.pr_clase_06.data.CarsDatabase
import es.javiercarrasco.pr_clase_06.data.model.Brand
import es.javiercarrasco.pr_clase_06.data.Repository
import es.javiercarrasco.pr_clase_06.data.model.Car
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CarsRepositoryIntegrationTest {
    private lateinit var db: CarsDatabase
    private lateinit var repository: Repository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            CarsDatabase::class.java
        ).allowMainThreadQueries() // solo en tests
            .build()

        val dao = db.carsDao()
        val localDataSource = LocalDatasource(dao)
        repository = Repository(localDataSource)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun saveBrand_y_currentBrands() = runTest {
        repository.saveBrand(Brand(name = "Test Integración"))

        val result = repository.currentBrands.first()

        assertEquals(1, result.size)
        assertEquals("Test Integración", result.first().name)
    }

    @Test
    fun saveCar_y_currentCars() = runTest {
        repository.saveBrand(Brand(name = "Test Integración"))
        repository.saveCar(
            Car(model = "Model Test", motor = "Motor Test", year = 1998 , idBrand = 1)
        )
        val result = repository.currentCars.first()

        assertEquals(1, result.size)
        assertEquals("Model Test", result.first().car.model)
    }
}