package es.javiercarrasco.examplet4_13

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Versión del ViewModel con Hilt para inyección de dependencias.
@HiltViewModel
class DetalleViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id = savedStateHandle.get<String>("id") ?: "No ID"
    val contenido = mutableStateOf("Contenido del ítem $id")
}