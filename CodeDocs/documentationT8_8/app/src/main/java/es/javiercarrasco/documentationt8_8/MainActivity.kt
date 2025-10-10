package es.javiercarrasco.documentationt8_8

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import es.javiercarrasco.documentationt8_8.ui.theme.DocumentationT8_8Theme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DocumentationT8_8Theme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Sensores") }) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AccelerometerSensor()
                    }
                }
            }
        }
    }
}

@Composable
fun AccelerometerSensor() {
    val contxt = LocalContext.current
    val sensorManager = contxt.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var x by remember { mutableStateOf(0f) }
    var y by remember { mutableStateOf(0f) }
    var z by remember { mutableStateOf(0f) }

    // Filtro paso-bajo simple, lowPass se usa para suavizar los valores del sensor,
    // evitando cambios bruscos en la UI.
    val alpha = 0.1f
    fun lowPass(new: Float, old: Float) = old + alpha * (new - old)

    // Registrar el listener del sensor cuando el Composable entre en composición.
    // DisposableEffect se asegura de que el listener se desregistre cuando el Composable se elimine.
    DisposableEffect(Unit) {
        if (accelerometer == null) {
            return@DisposableEffect onDispose {}
        }
        
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                    x = lowPass(event.values[0], x)
                    y = lowPass(event.values[1], y)
                    z = lowPass(event.values[2], z)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        sensorManager.registerListener(
            listener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // Asegurar que se desregistre el listener cuando el Composable se elimine.
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    Column {
        Text("Acelerómetro (m/s²):")
        Text("X: ${"%.2f".format(x)}   Y: ${"%.2f".format(y)}   Z: ${"%.2f".format(z)}")
        if (accelerometer == null) Text("No disponible en este dispositivo.")
    }
}