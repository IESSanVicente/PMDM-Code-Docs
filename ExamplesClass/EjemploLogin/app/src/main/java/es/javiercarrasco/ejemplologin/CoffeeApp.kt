package es.javiercarrasco.ejemplologin

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import es.javiercarrasco.ejemplologin.data.Repository
import es.javiercarrasco.ejemplologin.data.model.SessionManager
import es.javiercarrasco.ejemplologin.data.model.dataStore

/**
 * Coffee app
 * @author Javier Carrasco
 *
 * Esta clase extiende de Application y se utiliza para inicializar componentes globales como la base de datos,
 * el SessionManager y el Repository. Esto permite que estos componentes estén disponibles en toda la aplicación
 * sin necesidad de inicializarlos en cada actividad o fragmento.
 */
class CoffeeApp : Application() {
    // Instancia del SessionManager para manejar la sesión de usuario.
    lateinit var sessionManager: SessionManager
        private set

    // Instancia del Repositorio para manejar la lógica de negocio.
    lateinit var repository: Repository
        private set

    override fun onCreate() {
        super.onCreate()

        // Inicializa el SessionManager con el DataStore.
        val dataStore: DataStore<Preferences> = this.dataStore
        sessionManager = SessionManager(dataStore)

        // Inicializa el Repositorio.
        repository = Repository(sessionManager)
    }
}