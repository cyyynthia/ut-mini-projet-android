package m2sdl.lacuillere.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import m2sdl.lacuillere.R
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.ui.composables.RequestLocation
import m2sdl.lacuillere.viewmodel.MapViewModel

const val ZOOM_FACTOR = 15f

@Composable
fun HomeMapScreen(
	model: MapViewModel,
	restaurants: List<Restaurant>,
	onNavigateToRestaurant: (r: Restaurant) -> Unit,
) {
	val userLocation by model.userLocation
	val cameraPositionState = rememberCameraPositionState {
		position = CameraPosition.fromLatLngZoom(LatLng(51.5074, -0.1278), 10f)
	}

	RequestLocation(model)

	userLocation?.let {
		cameraPositionState.position = CameraPosition.fromLatLngZoom(it, ZOOM_FACTOR)
	}

	GoogleMap(
		modifier = Modifier.fillMaxSize(),
		cameraPositionState = cameraPositionState,
		properties = MapProperties(
			isBuildingEnabled = true,
			isTrafficEnabled = true,
			mapStyleOptions = MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.map_style)
		)
	) {
		restaurants.forEach { restaurant ->
			Marker(
				state = MarkerState(
					position = LatLng(
						restaurant.position.latitude, restaurant.position.longitude
					)
				),
				title = restaurant.name,
				onInfoWindowClick = { onNavigateToRestaurant(restaurant) }
			)
		}
	}
}
