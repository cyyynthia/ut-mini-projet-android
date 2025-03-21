package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun EmptyScreen(
	icon: ImageVector,
	text: String,
	content: @Composable () -> Unit = {},
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.fillMaxSize()
	) {
		Surface(
			color = MaterialTheme.colorScheme.primaryContainer,
			modifier = Modifier
				.clip(RoundedCornerShape(50))
				.background(MaterialTheme.colorScheme.primaryContainer)
				.padding(24.dp)
		) {
			Icon(
				icon,
				contentDescription = null,
				modifier = Modifier.size(40.dp),
				tint = MaterialTheme.colorScheme.onPrimaryContainer
			)
		}

		Text(text, style = MaterialTheme.typography.titleMedium)
		content()
	}
}

@Composable
fun EmptyScreenAction(
	icon: ImageVector,
	text: String,
	button: String,
	onAction: () -> Unit,
) {
	EmptyScreen(
		icon = icon,
		text = text,
	) {
		ExtendedFloatingActionButton(onClick = onAction) {
			Text(button)
		}
	}
}
