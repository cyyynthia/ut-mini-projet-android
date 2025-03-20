package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Restaurant

@Composable
fun RestoCard(restaurant: Restaurant, modifier: Modifier = Modifier) {
	OutlinedCard(modifier = modifier.fillMaxWidth()) {
		Row(
			modifier = Modifier.padding(16.dp),
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			verticalAlignment = Alignment.Top
		) {
			Column(modifier = Modifier.padding(top = 4.dp)) {
				restaurant.banner.HackyImage(
					contentDescription = null,
					modifier = Modifier
						.size(64.dp, 64.dp)
						.clip(RoundedCornerShape(8.dp))
				)
			}

			Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
				Row {
					Text(restaurant.name, style = MaterialTheme.typography.titleLarge)
				}

				ListItemDataRow(Icons.Default.LocationOn, restaurant.address)
				ListItemDataRow(Icons.Default.Phone, restaurant.telephone)
			}
		}
	}
}
