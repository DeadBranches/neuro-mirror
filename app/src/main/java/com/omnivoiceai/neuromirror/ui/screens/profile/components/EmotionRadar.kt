package com.omnivoiceai.neuromirror.ui.screens.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.radarChart.RadarChart
import com.aay.compose.radarChart.model.NetLinesStyle
import com.aay.compose.radarChart.model.Polygon
import com.aay.compose.radarChart.model.PolygonStyle
import com.omnivoiceai.neuromirror.data.database.note.EmotionDetected
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesViewModel

@Composable
fun EmotionRadar(notesViewModel: NotesViewModel) {
    var emotionCounts by remember { mutableStateOf<List<Pair<EmotionDetected, Int>>>(emptyList()) }

    LaunchedEffect(Unit) {
        emotionCounts = notesViewModel.getEmotionCounts()
    }

    if (emotionCounts.isNotEmpty()) {
        val values = emotionCounts.map { it.second.toDouble() }
        val maxValue = values.maxOrNull()?.takeIf { it > 0.0 } ?: 1.0
        val minNormalizedValue = 0.1

        val normalizedValues = values.map { value ->
            if (value <= 0.0) minNormalizedValue
            else (value / maxValue) * (1.0 - minNormalizedValue) + minNormalizedValue
        }

        val labels = emotionCounts.map { it.first }

        Column {
            RadarChart(
                modifier = Modifier.size(350.dp),
                radarLabels = labels.map { stringResource(it.getLabelRes()) },
                labelsStyle = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp
                ),
                netLinesStyle = NetLinesStyle(
                    netLineColor = Color(0x30FFFFFF),
                    netLinesStrokeWidth = 1f,
                    netLinesStrokeCap = StrokeCap.Butt
                ),
                scalarSteps = 4,
                scalarValue = 1.0,
                scalarValuesStyle = TextStyle(
                    color = Color.Transparent,
                    fontSize = 0.sp
                ),
                polygons = listOf(
                    Polygon(
                        values = normalizedValues,
                        unit = "",
                        style = PolygonStyle(
                            fillColor = Color(0xFFc2ff86),
                            fillColorAlpha = 0.9f,
                            borderColor = Color(0xFF7cb342),
                            borderColorAlpha = 1f,
                            borderStrokeWidth = 2f,
                            borderStrokeCap = StrokeCap.Butt
                        )
                    )
                )
            )
        }
    }
}
