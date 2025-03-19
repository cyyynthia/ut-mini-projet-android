package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LinearLoadingOverlay(isLoading: Boolean, modifier: Modifier = Modifier) {
	if (!isLoading) return

	Column(
		modifier = modifier
			.fillMaxSize()
			.absoluteOffset(0.dp, 0.dp)
			.background(Color.Black.copy(alpha = 0.5f)),
	)  {
		LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
	}
}

@Composable
fun CircularLoadingOverlay(isLoading: Boolean, modifier: Modifier = Modifier) {
	if (!isLoading) return

	Column(
		modifier = modifier
			.fillMaxSize()
			.absoluteOffset(0.dp, 0.dp)
			.background(Color.Black.copy(alpha = 0.5f)),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	)  {
		CircularProgressIndicator()
	}
}
