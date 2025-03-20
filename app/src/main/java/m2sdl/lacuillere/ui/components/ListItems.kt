package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Reservation
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.Review
import m2sdl.lacuillere.data.repository.RepositoryLocator
import m2sdl.lacuillere.ui.theme.DrawGreen
import m2sdl.lacuillere.ui.theme.DrawRed
import m2sdl.lacuillere.ui.theme.DrawYellow
import java.time.format.DateTimeFormatter

@Composable
fun ListItemDataRow(icon: ImageVector, data: String) {
	Row(
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(
			icon,
			contentDescription = null,
			modifier = Modifier.size(MaterialTheme.typography.bodyMedium.fontSize.value.dp),
		)
		Text(data, style = MaterialTheme.typography.bodyMedium)
	}
}

@Composable
fun StarRating(rating: Double, current: Int) {
	Icon(
		when {
			rating >= current + 1 -> Icons.Filled.Star
			rating >= current + 0.5 -> Icons.AutoMirrored.Filled.StarHalf
			else -> Icons.Filled.StarBorder
		},
		contentDescription = null,
		modifier = Modifier.size(MaterialTheme.typography.bodyMedium.fontSize.value.dp),
		tint = DrawYellow,
	)
}

@Composable
fun RestoListItem(
	restaurant: Restaurant,
	onClick: () -> Unit,
	contentPadding: PaddingValues = PaddingValues(),
	modifier: Modifier = Modifier,
) {
	val dtf = DateTimeFormatter.ofPattern("HH:mm")
	val restaurantRating = RepositoryLocator.getReviewRepository()
		.filterBy { it.restaurantId == restaurant.id }
		.map { it.note }
		.average()

	Row(
		horizontalArrangement = Arrangement.spacedBy(24.dp),
		modifier = modifier
			.clickable { onClick() }
			.padding(contentPadding)
	) {
		Column(modifier = Modifier.padding(top = 4.dp)) {
			restaurant.banner.HackyImage(
				contentDescription = null,
				modifier = Modifier
					.size(64.dp, 64.dp)
					.clip(RoundedCornerShape(8.dp))
			)
		}

		Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
			Column {
				Text(restaurant.name, style = MaterialTheme.typography.titleLarge)

				Row(
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					if (restaurant.isCurrentlyOpen()) {
						val closingHours = restaurant.nextClosingHours()
						Box(
							modifier = Modifier
								.size(8.dp)
								.clip(RoundedCornerShape(4.dp))
								.background(DrawGreen)
						)
						Text("Ouvert – ferme à ${dtf.format(closingHours)}")
					} else {
						val openingHours = restaurant.nextOpeningHours()
						Box(
							modifier = Modifier
								.size(8.dp)
								.clip(RoundedCornerShape(4.dp))
								.background(DrawRed)
						)
						Text("Fermé – ouvre à ${dtf.format(openingHours)}")
					}
				}

				Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(bottom = 2.dp)) {
					// TODO: fix?
					StarRating(restaurantRating, 0)
					StarRating(restaurantRating, 1)
					StarRating(restaurantRating, 2)
					StarRating(restaurantRating, 3)
					StarRating(restaurantRating, 4)
				}
			}

			ListItemDataRow(Icons.Default.LocationOn, restaurant.addressShort)
			ListItemDataRow(Icons.Default.Phone, restaurant.telephone)
		}
	}
}

@Composable
fun ReviewListItem(review: Review) {
	Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

	}
}

@Composable
fun ReservationListItem(reservation: Reservation) {
	Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

	}
}
