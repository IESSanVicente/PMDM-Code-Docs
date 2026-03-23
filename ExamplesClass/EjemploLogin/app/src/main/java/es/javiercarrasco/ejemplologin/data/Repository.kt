package es.javiercarrasco.ejemplologin.data

import android.util.Log
import es.javiercarrasco.ejemplologin.data.model.Coffee
import es.javiercarrasco.ejemplologin.data.model.LoginRequest
import es.javiercarrasco.ejemplologin.data.model.SessionManager
import es.javiercarrasco.ejemplologin.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.firstOrNull

class Repository(private val sessionManager: SessionManager) {
    private val remoteDataSource = RemoteDataSource()
    private val TAG = Repository::class.java.simpleName

    // Función para obtener el login.
    suspend fun login(request: LoginRequest): Result<String> {
        return try {
            // Realiza la llamada al remoteDataSource para iniciar sesión.
            val response = remoteDataSource.login(request)

            if (response.token != null) {
                // Guarda la sesión utilizando el SessionManager.
                sessionManager.saveSession(
                    response.token,
                    request.user
                ) // Guarda la sesión en el SessionManager.
                Result.success(response.token)
            } else {
                Log.e(TAG, "Error durante el login: Token es null")
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            // Maneja cualquier error que ocurra durante el inicio de sesión.
            Log.e(TAG, "Error durante el login", e)
            Result.failure(e) // Devuelve un Result de fallo con la excepción capturada para que el ViewModel pueda manejarla.
        }
    }

    // Función para obtener el café.
    suspend fun fetchCoffees(): List<Coffee> {
        try {
            val session: Pair<String?, String?>? = sessionManager.sessionFlow.firstOrNull()
            if (session == null) {
                Log.e(TAG, "No hay sesión activa. No se puede obtener el token.")
                return emptyList() // Devuelve una lista vacía si no hay sesión activa.
            }
            return remoteDataSource.getCoffees(session.first!!)
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo cafés", e)
            return emptyList() // Devuelve una lista vacía si hay algún error en la petición.
        }
    }
}