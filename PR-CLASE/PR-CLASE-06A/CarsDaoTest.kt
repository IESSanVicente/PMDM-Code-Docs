

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CarsDaoTest {
    private lateinit var db: CarsDatabase
    private lateinit var dao: CarsDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            CarsDatabase::class.java
        ).allowMainThreadQueries() // solo en tests
            .build()

        dao = db.carsDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_y_getBrand_funcionan_correctamente() = runTest {
        val brand = Brand(name = "Test Brand")

        dao.insertBrand(brand)

        val result = dao.getAllBrands().first()

        assertEquals(1, result.size)
        assertEquals("Test Brand", result.first().name)
    }

    @Test
    fun insert_y_getCar_funcionan_correctamente() = runTest {
        dao.insertBrand(Brand(name = "Test Brand"))
        val car = Car(model = "Car Test", motor = "Motor Test", year = 1998, favorite = true, idBrand = 1)

        dao.insertCar(car)

        val result = dao.getCarsWithBrands().first()

        assertEquals(1, result.size)
        assertEquals("Car Test", result.first().car.model)
    }

    @Test
    fun update_brand_name() = runTest {
        dao.insertBrand(Brand(name = "Test Brand"))

        val inserted = dao.getAllBrands().first().first()
        val updated = inserted.copy(name = "Brand mod")
        dao.insertBrand(updated)

        val result = dao.getAllBrands().first().first()
        assertTrue(result.name.equals("Brand mod"))
    }

    @Test
    fun update_y_getbyid_car_model() = runTest {
        dao.insertCar(Car(idCar = 1, model = "Model Test", motor = "Motor Test", year = 1998, favorite = true, idBrand = 1))

        val inserted = dao.getCarById(1).first()
        val updated = inserted!!.copy(model = "Model mod")
        dao.insertCar(updated)

        val result = dao.getCarById(1).first()
        assertTrue(result!!.model.equals("Model mod"))
    }
}