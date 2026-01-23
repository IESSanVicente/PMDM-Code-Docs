package es.javiercarrasco.ejemplologin.data

import es.javiercarrasco.ejemplologin.data.model.Coffee
import es.javiercarrasco.ejemplologin.data.model.LoginRequest
import es.javiercarrasco.ejemplologin.data.model.LoginResponse
import es.javiercarrasco.ejemplologin.data.remote.RemoteDataSource

class Repository() {
    private val remoteDataSource = RemoteDataSource()

    // Función para obtener el login.
    suspend fun login(request: LoginRequest): LoginResponse {
        return remoteDataSource.login(request)
    }

    // Función para obtener el café.
    suspend fun getCoffees(token: String): List<Coffee> {
        try {
            return remoteDataSource.getCoffees(token)
        } catch (e: Exception) {
            return emptyList() // Devuelve una lista vacía si hay algún error en la petición.
        }
    }
}