package es.javiercarrasco.ejemplologin.data.model

import com.google.gson.annotations.SerializedName

/**
 * LoginState, sealed class para el estado del login.
 * @see LoginState para el estado del login.
 * @see LoginRequest para la petición del login.
 * @see LoginResponse para la respuesta del login.
 * @author Javier Carrasco
 */
sealed class LoginState {
    object Idle : LoginState()      // Estado inactivo (esperando acción del usuario)
    object Loading : LoginState()   // Estado cargando (esperando respuesta del servidor)
    data class Success(val response: LoginResponse) : LoginState()  // Estado éxito (respuesta correcta del servidor)
    data class Error(val message: String) : LoginState()    // Estado error (respuesta incorrecta del servidor)
}

/**
 * LoginRequest, data class para la petición del login, con usuario y contraseña.
 * Deben serializarse los campos con @SerializedName para que coincidan con los campos del servidor.
 */
data class LoginRequest(
    @SerializedName("usuario")
    val user: String,
    @SerializedName("password")
    val password: String
)

/**
 * LoginResponse, data class para la respuesta del login.
 */
data class LoginResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("token") val token: String?,
    @SerializedName("message") val message: String?,
    val username: String
)