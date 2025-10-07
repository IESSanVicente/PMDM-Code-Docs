package es.javiercarrasco.documentationt84

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PermissionHandlerViewModel : ViewModel() {
    data class PermissionUiState(
        val granted: Boolean = false,
        val showRationale: Boolean = false,
        val permanentlyDenied: Boolean = false
    )

    // MutableStateFlow to hold the UI state, we use backing property.
    private val _uiState = MutableStateFlow(PermissionUiState())
    val uiState: StateFlow<PermissionUiState> = _uiState.asStateFlow()

    // Function to update the UI state based on permission results.
    fun onPermissionResult(granted: Boolean, shouldShowRationale: Boolean) {
        _uiState.update {
            it.copy(
                granted = granted,
                showRationale = !granted && shouldShowRationale,
                permanentlyDenied = !granted && !shouldShowRationale
            )
        }
    }
}