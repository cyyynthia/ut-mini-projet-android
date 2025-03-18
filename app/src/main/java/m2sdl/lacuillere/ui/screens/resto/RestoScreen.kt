package m2sdl.lacuillere.ui.screens.resto

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import m2sdl.lacuillere.data.Restaurant

@Composable
fun RestoScreen(restaurant: Restaurant, onNavigateToBook: (Restaurant) -> Unit){
	var selectedTabIndex by remember { mutableStateOf(0) }
	var showDialog by remember { mutableStateOf(false) }
	val tabs = listOf("À propos", "Avis")


	Column(modifier = Modifier.fillMaxSize()) {


		Box {
			AsyncImage(
				model = "https://pbs.twimg.com/media/FgagCUvWAAIhOgR.jpg",
				contentDescription = "Image du restaurant",
				modifier = Modifier
					.fillMaxWidth()
					.height(200.dp)
					.clickable { showDialog = true },
				contentScale = ContentScale.Crop
			)
		}
		Text(
			text = restaurant.name,
			style = MaterialTheme.typography.headlineLarge,
			modifier = Modifier.padding(16.dp)
		)
		TabRow(selectedTabIndex = selectedTabIndex) {
			tabs.forEachIndexed { index, title ->
				Tab(
					selected = selectedTabIndex == index,
					onClick = { selectedTabIndex = index },
					text = { Text(title) })
			}
		}
		when (selectedTabIndex) {
			0 -> AboutTab(restaurant)
			1 -> ReviewsTab(restaurant)
		}
		Button(
			onClick = { onNavigateToBook(restaurant) },
			modifier = Modifier.padding(16.dp)
		) {
			Text("Réserver")
		}
	}

	if (showDialog) {
		Dialog(onDismissRequest = { showDialog = false }) {
			Box(modifier = Modifier.fillMaxSize()) {
				AsyncImage(
					model = "https://pbs.twimg.com/media/FgagCUvWAAIhOgR.jpg",
					contentDescription = "Image du restaurant",
					modifier = Modifier
						.fillMaxSize()
						.clickable { showDialog = false })
			}
		}
	}
}
