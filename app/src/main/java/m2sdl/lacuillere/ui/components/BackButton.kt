package m2sdl.lacuillere.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BackButton(onBack: (() -> Unit), contentDescription: String?, modifier: Modifier = Modifier) {
	BackHandler(onBack = onBack)
	IconButton(onClick = onBack, modifier = modifier) {
		Icon(
			Icons.AutoMirrored.Default.ArrowBack,
			contentDescription = contentDescription,
		)
	}
}
