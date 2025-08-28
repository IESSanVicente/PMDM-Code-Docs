package es.javiercarrasco.documentationt7_1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.javiercarrasco.documentationt7_1.model.Post

// AppDatabase.kt
@Database(
    entities = [Post::class],
    version = 1,
    exportSchema = true // Importante para migraciones
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postsDAO(): PostsDAO // Conexión con DAO de Posts.

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Posts.db"
                ).fallbackToDestructiveMigration(true) // Solo en desarrollo.
                    .build()

                INSTANCE = instance // Asigna la instancia a la variable volátil.
                instance // Devuelve la instancia de la base de datos.
            }
        }
    }
}