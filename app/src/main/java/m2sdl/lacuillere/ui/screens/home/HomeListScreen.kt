package m2sdl.lacuillere.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import m2sdl.lacuillere.data.Restaurant

@Composable
fun HomeListScreen(restaurants: List<Restaurant>, onNavigateToRestaurant: (r: Restaurant) -> Unit) {
	var firstChecked by remember { mutableStateOf(true) }

	LazyColumn(
		modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(restaurants, key = { restaurant -> restaurant.id }) { restaurant ->
			ElevatedCard(
				elevation = CardDefaults.cardElevation(
					defaultElevation = 6.dp
				),
				colors = CardDefaults.cardColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
				),
				shape = RoundedCornerShape(16.dp),
				modifier = Modifier
					.fillMaxWidth()
					.height(200.dp)
					.clickable { onNavigateToRestaurant(restaurant) }

			) {
				Row(modifier = Modifier.padding(8.dp)) {
					AsyncImage(
						model = "https://pbs.twimg.com/media/FgagCUvWAAIhOgR.jpg",
						contentDescription = "Image du restauront",
						modifier = Modifier
							.size(200.dp, 200.dp)
							.padding(end = 8.dp)
							.clip(RoundedCornerShape(8.dp)),
						contentScale = ContentScale.Crop
					)
					Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

						Text(
							text = restaurant.name,
							style = MaterialTheme.typography.bodyLarge,
							fontSize = 26.sp,
							color = MaterialTheme.colorScheme.onPrimaryContainer
						)
						Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
							Text(
								text = "Note : 5 ",
								style = MaterialTheme.typography.bodyLarge,
								color = MaterialTheme.colorScheme.onPrimaryContainer
							)
							Text(
								text = "Open : Oui",
								style = MaterialTheme.typography.bodyLarge,
								color = MaterialTheme.colorScheme.onPrimaryContainer
							)
						}
						Text(
							text = "${restaurant.openingHours.first().first} - ${restaurant.openingHours.first().second}",
							style = MaterialTheme.typography.bodyLarge,
							color = MaterialTheme.colorScheme.onPrimaryContainer
						)
						Text(
							text = "Address : ${restaurant.addressShort}",
							style = MaterialTheme.typography.bodyLarge,
							color = MaterialTheme.colorScheme.onPrimaryContainer
						)
					}
					IconToggleButton(
						checked = firstChecked,
						onCheckedChange = { firstChecked = !firstChecked },
					) {

					}
				}
			}
		}
	}
}