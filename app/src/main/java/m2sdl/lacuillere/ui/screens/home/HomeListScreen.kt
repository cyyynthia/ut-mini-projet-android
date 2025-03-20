package m2sdl.lacuillere.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.ui.components.RestoListItem

@Composable
fun HomeListScreen(restaurants: List<Restaurant>, onNavigateToRestaurant: (r: Restaurant) -> Unit) {
	Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
		Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.padding(start = 16.dp, end = 16.dp, top = 40.dp),
		) {
			item { Spacer(Modifier.height(56.dp)) }

			itemsIndexed(restaurants) { index, restaurant ->
				RestoListItem(restaurant, onClick = { onNavigateToRestaurant(restaurant) })

				if (index < restaurants.lastIndex)
					HorizontalDivider(Modifier.padding(vertical = 16.dp))
			}

			item { Spacer(Modifier.height(104.dp)) }
		}
	}
}
