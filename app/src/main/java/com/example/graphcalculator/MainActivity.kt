package com.example.graphcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.udojava.evalex.Expression
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GraphingApp()
        }
    }
}

@Composable
fun GraphingApp() {
    var equation by remember { mutableStateOf("x^2") }
    var graphData by remember { mutableStateOf(generateGraphData(equation)) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        BasicTextField(
            value = equation,
            onValueChange = {
                equation = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { graphData = generateGraphData(equation) }) {
            Text("Plot Graph")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                GraphCanvas(graphData)
            }
        }
    }
}

@Composable
fun GraphCanvas(graphData: List<Pair<Float, Float>>) {
    Canvas(modifier = Modifier.size(280.dp).background(Color.White)) {
        val path = Path()
        val scaleX = 140f / 100f // Scale X to fit within 140 pixels in each direction
        val scaleY = 140f / (graphData.maxOfOrNull { abs(it.second) } ?: 1f) // Scale Y dynamically
        val centerX = size.width / 2
        val centerY = size.height / 2

        graphData.forEachIndexed { index, point ->
            val scaledX = centerX + point.first * scaleX
            val scaledY = centerY - point.second * scaleY // Invert Y axis for proper graph orientation

            if (index == 0) {
                path.moveTo(scaledX, scaledY)
            } else {
                path.lineTo(scaledX, scaledY)
            }
        }
        drawPath(path, color = Color.Blue, style = Stroke(width = 2f))
    }
}

fun generateGraphData(equation: String): List<Pair<Float, Float>> {
    val points = mutableListOf<Pair<Float, Float>>()
    for (x in -100..100) {
        val y = evaluateEquation(equation, x.toFloat())
        points.add(Pair(x.toFloat(), y))
    }
    return points
}

fun evaluateEquation(equation: String, x: Float): Float {
    return try {
        val expression = Expression(equation.replace("x", x.toString()))
        expression.eval().toFloat()
    } catch (e: Exception) {
        0f // Return 0 if evaluation fails
    }
}


@Preview (showBackground = true)
@Composable
fun GraphingAppPreview() {
    GraphingApp()
}