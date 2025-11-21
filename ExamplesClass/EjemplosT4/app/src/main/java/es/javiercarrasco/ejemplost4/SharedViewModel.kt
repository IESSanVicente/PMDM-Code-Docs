package es.javiercarrasco.ejemplost4

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private var _texto = mutableStateOf("")
    val texto = _texto

    fun updateNombre(value: String) {
        if (value.isNotEmpty())
            _texto.value = value
    }
}