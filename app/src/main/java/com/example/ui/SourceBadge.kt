package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold600
import com.example.ui.theme.GlobalBlue600
import com.example.ui.theme.SlateNavy800
import com.example.ui.theme.SlateNavy900

@Composable
fun SourceBadge(
    sourceName: String,
    sourceCategory: String,
    modifier: Modifier = Modifier
) {
    val (badgeBg, textFg) = when {
        sourceName.contains("Reuters", ignoreCase = true) -> Color(0xFFFF6600).copy(alpha = 0.12f) to Color(0xFFD95300)
        sourceName.contains("Lianhe Zaobao", ignoreCase = true) || sourceName.contains("Zaobao", ignoreCase = true) -> Color(0xFFC8102E).copy(alpha = 0.12f) to Color(0xFFC8102E)
        sourceName.contains("Bloomberg", ignoreCase = true) -> Color(0xFF2A00FF).copy(alpha = 0.12f) to Color(0xFF1D00B3)
        sourceName.contains("South China Morning Post", ignoreCase = true) || sourceName.contains("SCMP", ignoreCase = true) -> Color(0xFF003366).copy(alpha = 0.12f) to Color(0xFF003366)
        sourceName.contains("Financial Times", ignoreCase = true) || sourceName.contains("FT", ignoreCase = true) -> Color(0xFFF4A880).copy(alpha = 0.35f) to Color(0xFF8B3A13)
        sourceName.contains("BBC", ignoreCase = true) -> Color(0xFFBB1919).copy(alpha = 0.12f) to Color(0xFFBB1919)
        sourceName.contains("Nikkei", ignoreCase = true) -> Color(0xFF005BAC).copy(alpha = 0.12f) to Color(0xFF005BAC)
        sourceName.contains("Straits Times", ignoreCase = true) -> Color(0xFF004B87).copy(alpha = 0.12f) to Color(0xFF004B87)
        sourceName.contains("Wall Street Journal", ignoreCase = true) || sourceName.contains("WSJ", ignoreCase = true) -> Color(0xFF000000).copy(alpha = 0.10f) to SlateNavy900
        else -> GlobalBlue600.copy(alpha = 0.12f) to GlobalBlue600
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = badgeBg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = "Verified Global Source",
                tint = textFg,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = sourceName,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = textFg
            )
            Text(
                text = " • ",
                style = MaterialTheme.typography.labelSmall,
                color = textFg.copy(alpha = 0.7f)
            )
            Text(
                text = if (sourceCategory.contains("Asia", ignoreCase = true)) "Asia Regional Major" else "Global Major",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = textFg.copy(alpha = 0.85f)
            )
        }
    }
}
