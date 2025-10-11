package es.javiercarrasco.documentationt9_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.javiercarrasco.documentationt9_1.data.repository.SecurePrefsRepository
import es.javiercarrasco.documentationt9_1.ui.DataStoreExample
import es.javiercarrasco.documentationt9_1.ui.PreferenceExample
import es.javiercarrasco.documentationt9_1.ui.SAFExample
import es.javiercarrasco.documentationt9_1.ui.SecurePrefsScreen
import es.javiercarrasco.documentationt9_1.ui.SecurePrefsViewModel
import es.javiercarrasco.documentationt9_1.ui.WriteToFileExample
import es.javiercarrasco.documentationt9_1.ui.theme.DocumentationT9_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DocumentationT9_1Theme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val contxt = LocalContext.current
    val repo = SecurePrefsRepository(contxt)
    val vm = SecurePrefsViewModel(repo)

    Scaffold(
        topBar = { TopAppBar({ Text(stringResource(R.string.app_name)) }) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(8.dp).verticalScroll(rememberScrollState())
        ) {
            WriteToFileExample()
            Spacer(modifier = Modifier.height(8.dp))
            PreferenceExample()
            Spacer(modifier = Modifier.height(8.dp))
            DataStoreExample()
            Spacer(modifier = Modifier.height(8.dp))
            SAFExample()
            Spacer(modifier = Modifier.height(8.dp))
            SecurePrefsScreen(vm)
        }
    }
}

// Preview de la pantalla principal
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DocumentationT9_1Theme {
        MainScreen()
    }
}