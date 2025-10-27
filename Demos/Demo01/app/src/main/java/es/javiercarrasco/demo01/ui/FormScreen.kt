package es.javiercarrasco.demo01.ui

import android.icu.text.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import es.javiercarrasco.demo01.R
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FormScreen(vm: FormViewModel = viewModel()) {
    // Estado de la UI observado desde el ViewModel.
    val ui by vm.uiState.collectAsStateWithLifecycle()

    var showDatePicker by rememberSaveable { mutableStateOf(false) } // estado efímero de UI
    val scroll = rememberScrollState() // para Column con verticalScroll

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.title_form)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = ui.name,
                onValueChange = vm::onNameChange,
                label = { Text(stringResource(R.string.label_name)) },
                isError = ui.attemptedSubmit && !ui.isNameValid,
                supportingText = {
                    if (ui.attemptedSubmit && !ui.isNameValid)
                        Text(stringResource(R.string.msg_empty_field))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ui.surname,
                onValueChange = vm::onSurnameChange,
                label = { Text(stringResource(R.string.label_surname)) },
                isError = ui.attemptedSubmit && !ui.isSurnameValid,
                supportingText = {
                    if (ui.attemptedSubmit && !ui.isSurnameValid)
                        Text(stringResource(R.string.msg_empty_field))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Fecha de nacimiento (lectura + diálogo con DatePicker)
            // Si prefieres el formato por defecto, utiliza Locale.getDefault() en lugar de Locale.forLanguageTag().
            val dateLabel = ui.birthDateMillis?.let {
                DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.forLanguageTag("es-ES"))
                    .format(Date(it))
            } ?: ""

            OutlinedTextField(
                value = dateLabel,
                onValueChange = {},
                label = { Text(stringResource(R.string.label_birthdate)) },
                isError = ui.attemptedSubmit && !ui.isBirthDateValid,
                supportingText = {
                    if (ui.attemptedSubmit && !ui.isBirthDateValid)
                        Text(stringResource(R.string.msg_empty_field))
                },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Carnet de conducir (radio)
            Text(
                text = stringResource(R.string.label_license),
                style = MaterialTheme.typography.titleMedium
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FilterChip(
                    selected = !ui.hasLicense,
                    onClick = { vm.onLicenseChange(false) },
                    label = { Text(stringResource(R.string.label_no)) }
                )
                FilterChip(
                    selected = ui.hasLicense,
                    onClick = { vm.onLicenseChange(true) },
                    label = { Text(stringResource(R.string.label_yes)) }
                )
            }

            // Turno de tarde (switch)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.label_evening_shift))
                Switch(checked = ui.eveningShift, onCheckedChange = vm::onEveningShiftChange)
            }

            // Módulos (checkbox list)
            Text(
                text = stringResource(R.string.label_modules),
                style = MaterialTheme.typography.titleMedium
            )

            for (module in stringArrayResource(R.array.modules_array)) {
                ModuleCheckbox(text = module, checked = module in ui.selectedModules) {
                    vm.toggleModule(module, it)
                }
            }

            // Botón validar
            Button(
                onClick = { vm.onAttemptSubmit() },
                modifier = Modifier.fillMaxWidth()
                //enabled = ui.isFormValid // Activa solo si el formulario es válido.
            ) {
                Text(stringResource(R.string.action_validate))
            }

            // Resumen (solo si válido)
            if (ui.attemptedSubmit && ui.isFormValid) {
                val turn = if (ui.eveningShift) stringResource(R.string.label_evening_shift)
                else stringResource(R.string.turn_morning)
                val license = if (ui.hasLicense) stringResource(R.string.label_yes)
                else stringResource(R.string.label_no)
                val summary1 = stringResource(
                    R.string.summary_person,
                    ui.name, ui.surname, dateLabel.ifBlank { "—" }
                )
                val summary2 = stringResource(R.string.summary_other, license, turn)
                val summary3 = stringResource(R.string.summary_modules, ui.selectedModules.joinToString())

                Spacer(Modifier.height(8.dp))

                // Tarjeta con el resumen.
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(summary1)
                        Text(summary2)
                        Text(summary3)
                    }
                }
            }

            // Mensaje de selección de módulos si falta
            if (ui.attemptedSubmit && !ui.isAtLeastOneModule) {
                AssistChip( // Similar a un OutlinedButton con icono.
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { // También podría ser leadingIcon.
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                    },
                    onClick = {},
                    label = { Text(stringResource(R.string.msg_select_modules)) }
                )
            }
        }
    }

    // DatePickerDialog de M3
    if (showDatePicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    vm.onBirthDateChange(state.selectedDateMillis)
                    showDatePicker = false
                }) { Text(text = "OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(text = "Cancel") }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

@Composable
private fun ModuleCheckbox(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text)
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
    }
}
