package es.javiercarrasco.demo01.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// FormViewModel.kt
class FormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(v: String) = _uiState.update { it.copy(name = v) }
    fun onSurnameChange(v: String) = _uiState.update { it.copy(surname = v) }
    fun onBirthDateChange(millis: Long?) = _uiState.update { it.copy(birthDateMillis = millis) }
    fun onLicenseChange(v: Boolean) = _uiState.update { it.copy(hasLicense = v) }
    fun onEveningShiftChange(v: Boolean) = _uiState.update { it.copy(eveningShift = v) }

    fun toggleModule(id: String, checked: Boolean) = _uiState.update {
        it.copy(selectedModules = if (checked) it.selectedModules.plus(id) else it.selectedModules.minus(id))
    }

    fun onAttemptSubmit() = _uiState.update { it.copy(attemptedSubmit = true) }
    fun resetForm() { _uiState.value = FormUiState() }
}