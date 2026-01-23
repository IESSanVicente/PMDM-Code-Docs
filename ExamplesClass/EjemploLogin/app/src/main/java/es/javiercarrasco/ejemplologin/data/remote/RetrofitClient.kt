package es.javiercarrasco.ejemplologin.data.remote

import es.javiercarrasco.ejemplologin.data.model.Coffee
import es.javiercarrasco.ejemplologin.data.model.LoginRequest
import es.javiercarrasco.ejemplologin.data.model.LoginResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * RetrofitClient, object for Retrofit2 API.
 * ApiService, interface for Retrofit2 API.
 * @author Javier Carrasco
 */
object RetrofitClient {
    private val BASE_URL = "https://www.javiercarrasco.es/api/coffee/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

interface ApiService {
    // Función para obtener el login, se pasa un objeto LoginRequest en el body.
    @POST("login") // https://www.javiercarrasco.es/api/coffee/login
    @Headers("Content-Type: application/json") // Indica que el contenido es JSON.
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("coffee") // https://www.javiercarrasco.es/api/coffee/coffee
    suspend fun getCoffees(@Header("Authorization") token: String): List<Coffee>
}