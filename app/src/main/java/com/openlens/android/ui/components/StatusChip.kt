package com.openlens.android.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openlens.android.ui.theme.*

@Composable
fun StatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (status.lowercase()) {
        "running" -> StatusRunning to Color.White
        "pending" -> StatusPending to Color.White
        "succeeded" -> StatusSucceeded to Color.White
        "failed" -> StatusFailed to Color.White
        "terminating" -> StatusTerminating to Color.White
        else -> StatusUnknown to Color.White
    }

    Surface(
        modifier = modifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}