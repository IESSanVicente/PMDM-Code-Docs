package es.javiercarrasco.t2_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import es.javiercarrasco.t2_1.ui.theme.T2_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            T2_1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LayoutPersonalizadoDemo()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LayoutPersonalizadoDemo() {
    Box(
        modifier = Modifier
            .wrapContentHeight() // Se ajusta al contenido
            .background(Color(0xFFEFEFEF)) // Fondo gris claro
            .centroManual() // Modificador personalizado
    ) {
        Text(
            text = "Texto centrado con layout personalizado",
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}

fun Modifier.centroManual(): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        val width = constraints.maxWidth
        val height = constraints.maxHeight

        val x = (width - placeable.width) / 2
        val y = (height - placeable.height) / 2

        layout(width, height) {
            placeable.place(x, y)
        }
    }
)