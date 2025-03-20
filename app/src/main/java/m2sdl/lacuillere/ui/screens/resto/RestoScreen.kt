package m2sdl.lacuillere.ui.screens.resto

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import m2sdl.lacuillere.data.Restaurant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoScreen(
	restaurant: Restaurant,
	onNavigateToBook: () -> Unit,
	onNavigateToSubmitReview: () -> Unit,
) {
	var selectedTabIndex by remember { mutableStateOf(0) }
	var showDialog by remember { mutableStateOf(false) }
	val tabs = listOf("À propos", "Avis")

	Column(modifier = Modifier.fillMaxSize()) {
		Box {
			restaurant.banner.HackyImage(
				contentDescription = null,
				modifier = Modifier
					.fillMaxWidth()
					.height(200.dp)
					.clickable { showDialog = true }
			)
		}
		Text(
			text = restaurant.name,
			style = MaterialTheme.typography.headlineLarge,
			modifier = Modifier.padding(16.dp)
		)

		PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
			tabs.forEachIndexed { index, title ->
				Tab(
					selected = selectedTabIndex == index,
					onClick = { selectedTabIndex = index },
					text = { Text(title) }
				)
			}
		}

		when (selectedTabIndex) {
			0 -> AboutTab(restaurant, onNavigateToBook)
			1 -> ReviewsTab(restaurant, onNavigateToSubmitReview)
		}
	}

	if (showDialog) {
		Dialog(onDismissRequest = { showDialog = false }) {
			Box(modifier = Modifier.fillMaxSize()) {
				// Change for show list of photos
				restaurant.banner.HackyImage(
					contentDescription = "Image du restaurant",
					modifier = Modifier
						.fillMaxSize()
						.clickable { showDialog = false }
				)
			}
		}
	}
}
