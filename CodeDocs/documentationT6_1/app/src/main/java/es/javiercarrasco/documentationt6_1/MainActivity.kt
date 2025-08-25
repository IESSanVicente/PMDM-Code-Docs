package es.javiercarrasco.documentationt6_1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.javiercarrasco.documentationt6_1.model.Editorial
import es.javiercarrasco.documentationt6_1.model.SuperWithEditorial
import es.javiercarrasco.documentationt6_1.ui.theme.DocumentationT6_1Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DocumentationT6_1Theme {
                MyNavigation()
            }
        }
    }
}

@Composable
fun MyNavigation() {
    val navController = rememberNavController()
    val supersViewModel: SupersViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            MainScreen(navController, supersViewModel)
        }
        composable("superheroscreen/{id}") { backStackEntry ->
            val idSuper = backStackEntry.arguments?.getString("id")
            idSuper?.let {
                SuperHeroScreen(navController, supersViewModel, it.toInt())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController, viewModel: SupersViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Se recolecta el StateFlow del ViewModel para observar el flujo de datos
    // de los superhéroes y las editoriales. Se puede usar collectAsState() o
    // collectAsStateWithLifecycle() para obtener el estado actual.
    val currentSupers by viewModel.currentSupers.collectAsStateWithLifecycle()
    val currentEditorials by viewModel.currentEditorials.collectAsStateWithLifecycle()

    // Se observa el LiveData del ViewModel
//    val currentEditorialsLD by viewModel.currentEditorialLD.observeAsState()
//    val currentSupersLD by viewModel.currentSupersLD.observeAsState()

    // Al utilizar Unit como clave, se ejecuta solo una vez al entrar en composición.
//    LaunchedEffect(Unit) {
//        viewModel.demoData() // Cargar datos de ejemplo si es necesario.
//    }


    LaunchedEffect(currentEditorials.isEmpty()) {
        delay(1_000) // Se espera un segundo para dar tiempo a que se carguen los datos.
        if (currentEditorials.isEmpty()) {

            snackbarHostState.showSnackbar(
                message = "No hay editoriales disponibles, debe existir al menos una para poder añadir superhéroes.",
                duration = SnackbarDuration.Short
            )
        }
    }


    // Aquí se define la UI principal de la aplicación.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Documentation T6.1") },
                actions = { AppBarMenu() }
            )
        },
        floatingActionButton = {
            if (currentEditorials.isNotEmpty())
                SmallFloatingActionButton(
                    onClick = { navController.navigate("superheroscreen/0") },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Superhéroe")
                }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        if (currentSupers.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                state = rememberLazyListState(), // Estado de la lista para manejar el scroll
            ) {
                items(currentSupers) { oneSuper ->
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .animateItem()
                            .combinedClickable(
                                onClick = {
                                    // Acción al hacer clic en el superhéroe.
                                    navController.navigate("superheroscreen/${oneSuper.supers.idSuper}") // Navega a la pantalla de superhéroe.
                                },
                                onLongClick = {
                                    // Acción al mantener pulsado el superhéroe, se elimina el superhéroe y es necesario
                                    // el uso del objeto scope para lanzar la corutina.
                                    scope.launch {
                                        val superAux = oneSuper.supers
                                        println("Borrados: ${viewModel.delSuper(oneSuper.supers)}")
                                        try {
                                            // Se descarta cualquier Snackbar previo para evitar conflictos.
                                            snackbarHostState.currentSnackbarData?.dismiss()
                                            // Se muestra un Snackbar para confirmar la eliminación.
                                            snackbarHostState.showSnackbar(
                                                message = "¿Eliminar superhéroe: ${oneSuper.supers.superName}?",
                                                actionLabel = "Deshacer",
                                                duration = SnackbarDuration.Short
                                            ).let { result ->
                                                if (result == SnackbarResult.ActionPerformed) {
                                                    // Si se pulsa el botón de deshacer, se cancela la eliminación.
                                                    viewModel.saveSuper(superAux)
                                                }
                                            }
                                        } catch (e: Exception) {
                                            Log.e(
                                                "MainScreen",
                                                "Error al eliminar superhéroe",
                                                e
                                            )
                                        }
                                    }
                                }
                            )
                    ) {
                        ItemList(oneSuper, viewModel)
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemList(
    oneSuper: SuperWithEditorial,
    viewModel: SupersViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = oneSuper.supers.superName,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(
                        start = 8.dp,
                        top = 8.dp,
                        bottom = 4.dp
                    ),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.W800
                )
            )
            Text(
                text = oneSuper.editorial.name,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 8.dp, bottom = 8.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontStyle = FontStyle.Italic
                )
            )
        }
        // Icono de favorito que cambia según el estado del superhéroe.
        val favoriteIcon: Int =
            if (oneSuper.supers.favorite) R.drawable.ic_star_on
            else R.drawable.ic_star_off

        Image(
            painter = painterResource(id = favoriteIcon),
            contentDescription = "Superhéroe favorito",
            modifier = Modifier
                .padding(end = 8.dp)
                .width(48.dp)
                .clickable {
                    viewModel.saveSuper(
                        oneSuper.supers.copy(favorite = !oneSuper.supers.favorite)
                    )
                },
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun AppBarMenu(viewModel: SupersViewModel = viewModel()) {
    var showMenu by remember { mutableStateOf(false) }
    val openEditorialDialog = remember { mutableStateOf(false) }

    IconButton(onClick = { showMenu = !showMenu }) {
        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
    ) {
        DropdownMenuItem(
            text = { Text("Añadir Editorial") },
            leadingIcon = {
                IconButton(onClick = { /* acción principal */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Editorial")
                }
            },
            onClick = {
                openEditorialDialog.value = true
                showMenu = false
            }
        )
    }

    // Diálogo para añadir o editar una editorial.
    // Se muestra cuando openEditorialDialog es true.
    if (openEditorialDialog.value) {
        DialogEditorial(
            onDismiss = { openEditorialDialog.value = false },
            onAccept = {
                viewModel.saveEditorial(Editorial(0, it.trim())) // Se guarda la nueva editorial.
                openEditorialDialog.value = false
            })
    }
}