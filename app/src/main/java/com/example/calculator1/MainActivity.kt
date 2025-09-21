package com.example.calculator1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf("calculator") }
    var resultText by remember { mutableStateOf("") }

    when (currentScreen) {
        "calculator" -> CalculatorScreen(
            onResult = { result ->
                resultText = result
                currentScreen = "result"
            }
        )
        "result" -> ResultScreen(
            text = resultText,
            onBack = { currentScreen = "calculator" }
        )
    }
}

@Composable
fun CalculatorScreen(onResult: (String) -> Unit) {
    var mode by remember { mutableStateOf(1) }
    Column(Modifier.padding(16.dp)) {
        Text("Виберіть режим:", style = MaterialTheme.typography.titleMedium)
        Row {
            Button(
                onClick = { mode = 1 },
                modifier = Modifier.padding(end = 8.dp)
            ) { Text("Завдання 1") }

            Button(onClick = { mode = 2 }) { Text("Завдання 2") }
        }

        Spacer(Modifier.height(16.dp))

        if (mode == 1) {
            Task1Screen(onResult)
        } else {
            Task2Screen(onResult)
        }
    }
}

@Composable
fun Task1Screen(onResult: (String) -> Unit) {
    var h by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }
    var s by remember { mutableStateOf("") }
    var n by remember { mutableStateOf("") }
    var o by remember { mutableStateOf("") }
    var w by remember { mutableStateOf("") }
    var a by remember { mutableStateOf("") }

    Column {
        FuelInputField("H (%)", h) { h = it }
        FuelInputField("C (%)", c) { c = it }
        FuelInputField("S (%)", s) { s = it }
        FuelInputField("N (%)", n) { n = it }
        FuelInputField("O (%)", o) { o = it }
        FuelInputField("W (%)", w) { w = it }
        FuelInputField("A (%)", a) { a = it }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            try {
                val input = FuelCalculator.FuelInput(
                    H = h.toDouble(),
                    C = c.toDouble(),
                    S = s.toDouble(),
                    N = n.toDouble(),
                    O = o.toDouble(),
                    W = w.toDouble(),
                    A = a.toDouble()
                )
                val r = FuelCalculator.calculate(input)
                val result = """
                    КРС = ${"%.3f".format(r.krs)}
                    КРГ = ${"%.3f".format(r.krg)}

                    📌 Суха маса:
                    H=${"%.2f".format(r.dry["H"])}%
                    C=${"%.2f".format(r.dry["C"])}%
                    S=${"%.2f".format(r.dry["S"])}%
                    N=${"%.2f".format(r.dry["N"])}%
                    O=${"%.2f".format(r.dry["O"])}%
                    A=${"%.2f".format(r.dry["A"])}%

                    📌 Горюча маса:
                    H=${"%.2f".format(r.combustible["H"])}%
                    C=${"%.2f".format(r.combustible["C"])}%
                    S=${"%.2f".format(r.combustible["S"])}%
                    N=${"%.2f".format(r.combustible["N"])}%
                    O=${"%.2f".format(r.combustible["O"])}%

                    🔥 Теплота:
                    Qр = ${"%.3f".format(r.Qr)} МДж/кг
                    Qс = ${"%.3f".format(r.Qd)} МДж/кг
                    Qг = ${"%.3f".format(r.Qdaf)} МДж/кг
                """.trimIndent()
                onResult(result)
            } catch (e: Exception) {
                onResult("Помилка вводу!")
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Розрахувати")
        }
    }
}

@Composable
fun Task2Screen(onResult: (String) -> Unit) {
    var h by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }
    var s by remember { mutableStateOf("") }
    var o by remember { mutableStateOf("") }
    var qdaf by remember { mutableStateOf("") }
    var w by remember { mutableStateOf("") }
    var a by remember { mutableStateOf("") }
    var v by remember { mutableStateOf("") }

    Column {
        FuelInputField("H (%) (горюча маса)", h) { h = it }
        FuelInputField("C (%) (горюча маса)", c) { c = it }
        FuelInputField("S (%) (горюча маса)", s) { s = it }
        FuelInputField("O (%) (горюча маса)", o) { o = it }
        FuelInputField("Qdaf (МДж/кг)", qdaf) { qdaf = it }
        FuelInputField("W (%) (робоча маса)", w) { w = it }
        FuelInputField("A (%) (суха маса)", a) { a = it }
        FuelInputField("V (мг/кг)", v) { v = it }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            try {
                val input = FuelCalculator2.FuelInput2(
                    H = h.toDouble(),
                    C = c.toDouble(),
                    S = s.toDouble(),
                    O = o.toDouble(),
                    Qdaf = qdaf.toDouble(),
                    W = w.toDouble(),
                    A = a.toDouble(),
                    V = v.toDouble()
                )
                val r = FuelCalculator2.calculate(input)
                val result = """
                    📌 Робоча маса мазуту:
                    H=${"%.2f".format(r.Hr)}%
                    C=${"%.2f".format(r.Cr)}%
                    S=${"%.2f".format(r.Sr)}%
                    O=${"%.2f".format(r.Or)}%
                    A=${"%.2f".format(r.Ar)}%
                    V=${"%.2f".format(r.Vr)} мг/кг

                    🔥 Теплота:
                    Qр = ${"%.3f".format(r.Qr)} МДж/кг
                """.trimIndent()
                onResult(result)
            } catch (e: Exception) {
                onResult("Помилка вводу!")
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Розрахувати")
        }
    }
}

@Composable
fun ResultScreen(text: String, onBack: () -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Text(text)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}

@Composable
fun FuelInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
}
