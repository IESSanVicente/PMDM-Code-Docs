package es.javiercarrasco.ejemplologin.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import es.javiercarrasco.ejemplologin.data.model.SessionManager.Companion.TOKEN_KEY
import es.javiercarrasco.ejemplologin.data.model.SessionManager.Companion.USERNAME_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore recomienda crear las propiedades de nivel superior.
// Inicialización con extensión delegada.
val Context.dataStore by preferencesDataStore(name = "settings")

/**
 * Clase para gestionar la sesión del usuario con DataStore.
 * @param dataStore DataStore para almacenar la sesión.
 * @property TOKEN_KEY Clave para almacenar el token.
 * @property USERNAME_KEY Clave para almacenar el nombre de usuario.
 * @property sessionFlow Flujo de datos para la sesión.
 */
class SessionManager(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USERNAME_KEY = stringPreferencesKey("username")
    }

    // Flujo de datos para la sesión. Devuelve un par con el token y el nombre de usuario.
    val sessionFlow: Flow<Pair<String?, String?>> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY] to preferences[USERNAME_KEY]
    }

    // Función para guardar los datos de la sesión.
    suspend fun saveSession(token: String, username: String) {
        dataStore.edit { preferences -> // Edita los datos del DataStore, si no existen los crea.
            preferences[TOKEN_KEY] = token
            preferences[USERNAME_KEY] = username
        }
    }

    // Función para limpiar la sesión. Borra los datos del fichero settings.
    suspend fun clearSession() {
        // dataStore.edit { it.clear() } // Limpia todos los datos del DataStore.

        dataStore.edit { // Versión para borrar únicamente el token.
            it.remove(TOKEN_KEY)
        }
    }
}