package m2sdl.lacuillere.ui.screens.resto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Restaurant

@Composable
fun ReviewsTab(restaurant: Restaurant, onNavigateToSubmitReview: () -> Unit) {
	Scaffold(
		floatingActionButton = {
			FloatingActionButton(onClick = onNavigateToSubmitReview) {
				Icon(Icons.Filled.Reviews, null)
			}
		}
	) {
		Column(
			modifier = Modifier
				.padding(16.dp)
				.verticalScroll(rememberScrollState())
		) {

		}
	}

}
