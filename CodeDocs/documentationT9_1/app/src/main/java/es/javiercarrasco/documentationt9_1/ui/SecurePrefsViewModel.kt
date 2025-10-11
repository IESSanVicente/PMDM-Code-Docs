package es.javiercarrasco.documentationt9_1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.javiercarrasco.documentationt9_1.data.repository.SecurePrefsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SecurePrefsViewModel(private val repo: SecurePrefsRepository) : ViewModel() {

    // StateFlow para observar el token almacenado de forma segura.
    val token: StateFlow<String?> = repo.tokenFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // StateFlow para observar el token cifrado (iv + ciphertext).
    val encrypted: StateFlow<String?> = repo.encryptedFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Función para guardar el token de forma segura.
    fun saveToken(value: String) = viewModelScope.launch {
        repo.saveToken(value)
    }

    // Función para borrar el token almacenado de forma segura.
    fun clearToken() = viewModelScope.launch {
        repo.clearToken()
    }
}
