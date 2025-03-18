package m2sdl.lacuillere.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import m2sdl.lacuillere.R
import m2sdl.lacuillere.applyTo
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.isNull
import m2sdl.lacuillere.toast
import m2sdl.lacuillere.ui.composables.RequestLocation
import m2sdl.lacuillere.viewmodel.MapViewModel

@Composable
fun HomeMapScreen(
	model: MapViewModel,
	restaurants: List<Restaurant>,
	onNavigateToRestaurant: (Restaurant) -> Unit,
	onMapUnavailable: (MapViewModel.LocationError) -> Unit,
) {
	val userLocation by model.userLocation
	val locationError by model.locationError
	val cameraPositionState = rememberCameraPositionState { userLocation?.applyTo(this) }
	val coroutineScope = rememberCoroutineScope()

	locationError?.let {
		when (it) {
			MapViewModel.LocationError.LocationUnavailable ->
				LocalContext.current.toast("Aucun service de localisation est activé.")

			MapViewModel.LocationError.PermissionDenied ->
				LocalContext.current.toast("Vous n'avez pas accordé la permission d'accéder à votre position.")
		}

		if (cameraPositionState.isNull()) {
			onMapUnavailable(it)
		}
	}

	if (cameraPositionState.isNull()) {
		userLocation?.applyTo(cameraPositionState)
			?: RequestLocation(model)
	} else {
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
						position = restaurant.position,
					),
					title = restaurant.name,
					snippet = "Cliquez ici pour voir le restaurant",
					onInfoWindowClick = { onNavigateToRestaurant(restaurant) },
					onClick = {
						coroutineScope.launch {
							cameraPositionState.animate(
								update = CameraUpdateFactory.newLatLng(restaurant.position),
								durationMs = 500
							)
						}

						false
					}
				)
			}
		}
	}
}
