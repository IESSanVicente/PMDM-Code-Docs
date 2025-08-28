package es.javiercarrasco.documentationt7_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.javiercarrasco.documentationt7_1.model.Post
import es.javiercarrasco.documentationt7_1.ui.theme.DocumentationT7_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DocumentationT7_1Theme {
                PostScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(viewModel: MainViewModel = viewModel()) {
    val posts: List<Post> by viewModel.posts.collectAsState()
    val loading by viewModel.loading.collectAsState() // Estado de carga.
    val error by viewModel.error.collectAsState()

    // Estado del pull-to-refresh.
    val refreshState = rememberPullToRefreshState()

    LaunchedEffect(posts) {
        if (posts.isEmpty() && !loading && error == null) {
            viewModel.fetchPosts()
        }
    }

    Scaffold(
        topBar = { TopAppBar({ Text("Documentation T7.1") }) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
//            if (loading) {
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .align(Alignment.CenterHorizontally)
//                )
//            } else if (error != null) {
            if (error != null) {
                Text(text = "Error: $error", color = Color.Red, modifier = Modifier.padding(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = { viewModel.fetchPosts() }) {
                    Text("Actualizar")
                }
            } else {
                // Implementación de Pull to Refresh.
                PullToRefreshBox(
                    isRefreshing = loading, // Usa el estado de carga del ViewModel.
                    state = refreshState, // Estado del pull-to-refresh.
                    modifier = Modifier.fillMaxSize(),
                    onRefresh = { viewModel.fetchPosts() } // Acción al refrescar.
                ) {
                    // Contenido que se puede refrescar
                    LazyColumn {
                        items(posts){ post ->
                            Card(modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()) {
                                Text("Título: ${post.title}", Modifier.padding(8.dp))
                                Text("Cuerpo: ${post.body}", modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
