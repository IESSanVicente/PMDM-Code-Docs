

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
class SupersDaoTest {
    private lateinit var db: SupersDatabase
    private lateinit var dao: SupersDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            SupersDatabase::class.java
        ).allowMainThreadQueries() // solo en tests
            .build()

        dao = db.supersDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_y_getEditorial_funcionan_correctamente() = runTest {
        val editorial = Editorial(name = "Test Editorial")

        dao.insertEditorial(editorial)

        val result = dao.getAllEditorials().first()

        assertEquals(1, result.size)
        assertEquals("Test Editorial", result.first().name)
    }

    @Test
    fun insert_y_getSuperhero_funcionan_correctamente() = runTest {
        dao.insertEditorial(Editorial(name = "Test Editorial"))
        val superhero = Superhero(superName = "Super Test", realName = "Real Test", favorite = true, idEditorial = 1)

        dao.insertSuperHero(superhero)

        val result = dao.getSuperHerosWithEditorials().first()

        assertEquals(1, result.size)
        assertEquals("Super Test", result.first().supers.superName)
    }

    @Test
    fun update_editorial_name() = runTest {
        dao.insertEditorial(Editorial(name = "Test Editorial"))

        val inserted = dao.getAllEditorials().first().first()
        val updated = inserted.copy(name = "Editorial mod")
        dao.insertEditorial(updated)

        val result = dao.getAllEditorials().first().first()
        assertTrue(result.name.equals("Editorial mod"))
    }

    @Test
    fun update_y_getbyid_superhero_supername() = runTest {
        dao.insertSuperHero(Superhero(idSuper = 1, superName = "Super Test", realName = "Real Test", favorite = true, idEditorial = 1))

        val inserted = dao.getSuperById(1).first()
        val updated = inserted!!.copy(superName = "Super mod")
        dao.insertSuperHero(updated)

        val result = dao.getSuperById(1).first()
        assertTrue(result!!.superName.equals("Super mod"))
    }
}