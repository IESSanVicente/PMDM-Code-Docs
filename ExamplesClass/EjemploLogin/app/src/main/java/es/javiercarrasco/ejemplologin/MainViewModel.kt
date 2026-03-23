package es.javiercarrasco.ejemplologin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import es.javiercarrasco.ejemplologin.data.model.LoginRequest
import es.javiercarrasco.ejemplologin.data.model.LoginResponse
import es.javiercarrasco.ejemplologin.data.model.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = MainViewModel::class.java.simpleName
    private val sessionManager = (application as CoffeeApp).sessionManager
    private val repository = (application as CoffeeApp).repository

    // Estado del login.
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _stateCoffee = MutableStateFlow<List<Any>>(emptyList())
    val stateCoffee: StateFlow<List<Any>> = _stateCoffee.asStateFlow()

    // Función para iniciar sesión y obtener el token.
    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            // Realiza la llamada al repositorio para iniciar sesión.
            val result = repository.login(request)

            result.onSuccess { token ->
                // Si el login fue exitoso, el token ya ha sido guardado en el SessionManager
                // por el Repository, así que solo actualizamos el estado a Success.
                _loginState.value = LoginState.Success(
                    LoginResponse(
                        ok = true,
                        token = token,
                        username = request.user,
                        message = "Login successful"
                    )
                )
            }.onFailure { e ->
                // Maneja cualquier error que ocurra durante el inicio de sesión.
                Log.e(TAG, "Error durante el login", e)
                _loginState.value = LoginState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // Función para obtener el flujo de la sesión.
    fun getSessionFlow() {
        viewModelScope.launch {
            sessionManager.sessionFlow.collect {
                Log.d(
                    "getSessionFlow",
                    "stateOptionLogin: Hola ${it.second}, your Token: ${it.first}"
                )
                if (it.first != null) {
                    // Si el token no es null, el usuario ha iniciado sesión y se puede
                    // recuperar el nombre de usuario y el token desde el flujo (DataStore).
                    _loginState.value = LoginState.Success(
                        LoginResponse(
                            ok = true,
                            token = it.first,
                            username = it.second!!,
                            message = "Already logged in"
                        )
                    )
                } else {
                    // Si el token es null, el usuario no ha iniciado sesión.
                    Log.d("getSessionFlow", "stateOptionLogin: No has iniciado sesión")
                    _loginState.value = LoginState.Idle // Estado inactivo
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _loginState.value = LoginState.Idle
            sessionManager.clearSession()
        }
    }

    // Función para obtener los cafés.
    fun getCoffees() {
        viewModelScope.launch {
            repository.fetchCoffees().let { coffees ->
                _stateCoffee.value = coffees
                Log.d(TAG, "Cafés obtenidos: ${_stateCoffee.value.size}")
            }
        }
    }
}